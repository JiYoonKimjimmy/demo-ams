# 11. 모니터링 및 로깅

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

AMS는 다중 테넌트 환경에서 발생하는 애플리케이션·인프라 이벤트를 실시간으로 가시화하고, 장애를 조기에 감지하여 서비스 품질을 유지합니다. 본 절의 전략은 [2.1 고수준 아키텍처](02_architecture_diagrams.md#21-고수준-아키텍처-다이어그램-시스템-전체-구성도)의 공통 인프라 계층, [7. 비동기 처리 전략](07_async_processing.md)의 이벤트 흐름, [10. 확장성 및 성능](10_scalability_performance.md)의 SLO 지표와 긴밀히 연계되어 운영 자동화와 비용 효율성을 동시에 추구합니다.

### 11.1 로깅 전략

- **구조화 로그(JSON) 채택**: Spring Boot 기본 로거를 Logback + Logstash 인코더로 확장하여 JSON 포맷을 출력합니다. 모든 로그에는 공통 필드가 포함됩니다.

  | 필드 | 설명 |
  |------|------|
  | `timestamp` | ISO-8601 UTC 기준 발생 시각 |
  | `level` | TRACE/DEBUG/INFO/WARN/ERROR |
  | `traceId` / `spanId` | OpenTelemetry 연계 분산 추적 식별자 |
  | `tenantId` | 멀티 테넌트 식별자 (요청 헤더 `X-AMS-Tenant`) |
  | `actor` | 최종 사용자 또는 시스템 계정 ID |
  | `useCase` | 실행 중인 UseCase/핸들러 명칭 |
  | `elapsedMs` | 처리 시간(ms) |
  | `message` / `detail` | 로그 메시지 및 추가 데이터(Map) |

- **로그 레벨 및 포인트**:
  - `INFO`: 주요 비즈니스 단계(UseCase 입·출력, 외부 연동 성공) 기록.
  - `WARN`: 재시도/폴백 발생, 임계치 근접 상황(캐시 미스 폭증 등).
  - `ERROR`: 예외 발생 시 에러 코드, Trace ID, 사용자 영향 범위를 포함. [13. 에러 처리](13_error_handling.md)의 글로벌 예외 처리기에서 중앙 집중 기록.
  - `DEBUG/TRACE`: 로컬 및 Staging에서만 활성화하며, 운영 환경에서는 환경 변수로 토글.

- **민감 정보 보호 및 규제 준수**: 개인정보(전화번호, 주소, 인증 토큰)는 마스킹/해시 처리하며, 요청·응답 본문은 샘플링 비율(기본 5%)에 따라 부분 저장합니다. 운영 로그는 30일(Hot storage), 180일(Cold storage) 보관 후 파기하며, 접근 권한은 RBAC 기반으로 제한합니다.

- **로그 수집 파이프라인**: Fluent Bit → Kafka(Log Topic) → OpenSearch/Elastic Stack 경로로 전송합니다. 장애 대비를 위해 S3에 원본 로그를 적재하고, Athena/Trino로 Ad-hoc 분석이 가능하도록 구성합니다.

### 11.2 모니터링 도구 및 메트릭 수집

- **관측성 스택 구성**: OpenTelemetry SDK로 애플리케이션을 계측하고, OTLP Exporter를 통해 Datadog/New Relic APM과 Prometheus 게이트웨이로 동시에 전송합니다. 시계열 데이터는 Prometheus, 시각화는 Grafana 대시보드를 사용합니다.

- **애플리케이션 메트릭**:
  - 요청 처리량, 지연 시간(p50/p95/p99), 오류율을 엔드포인트/테넌트 기준으로 집계.
  - Coroutine 디스패처 큐 길이, JVM Heap/GC 지표(G1GC/ZGC), Redis 연결 수 등 런타임 메트릭을 수집.
  - Outbound 연동 성공률, 타임아웃 횟수는 [9.2 외부 시스템 연동](09_communication.md#92-외부-시스템-연동-방식-async--non-blocking)과 연계하여 지표화합니다.

- **데이터베이스 및 스토리지 메트릭**: PostgreSQL(쿼리 TPS, Lock, Replication Lag), MongoDB(컬렉션별 Ops, WiredTiger 캐시 Hit Ratio), Redis(적중률, 메모리 사용률), MQ(큐 적체량). 이상 징후 발생 시 자동으로 `WARN` 로그를 발생시켜 로그/메트릭 상호 참조를 지원합니다.

- **인프라 및 배포 지표**: Kubernetes HPA 지표(CPU, 메모리, 네트워크 I/O), 노드 헬스, 배포 이벤트(롱러닝 파드, 실패한 롤아웃)를 모니터링합니다. [12. 배포 및 운영](12_deployment_operations.md)의 배포 전략과 연결하여 Blue-Green/Canary 전환율을 대시보드에서 추적합니다.

- **SLO 연계 및 경보 기준**: [10.5 성능 최적화 방안](10_scalability_performance.md#105-성능-최적화-방안)에서 정의한 SLO(월 가용성 99.5%, p95 300ms, 오류율 0.5%)를 기준으로 경보 룰을 설정합니다. SLO 위반 예측(예: 예측 모델로 1시간 내 위반 가능성 60% 이상) 시 사전 알림을 발송하고, 위반 발생 시 자동 Postmortem 템플릿을 생성합니다.

### 11.3 에러 추적 및 알림 시스템

- **에러 트래킹 도구 연동**: Sentry(또는 Rollbar)를 도입하여 예외 스택, 사용자 컨텍스트, Breadcrumb을 수집합니다. 백엔드 Trace ID와 Sentry 이벤트 ID를 상호 링크하여 로그-메트릭-트레이스 간 Drill-down을 지원합니다.

- **알림 체계**:
  - Slack Incident 채널(Severity별), 이메일, 온콜(Opsgenie/PagerDuty) 연동.
  - 심각도 기준: Sev-1(전 서비스 중단) 5분 내 온콜, Sev-2(주요 기능 장애) 15분 내 대응, Sev-3(부분 기능 장애) 영업시간 내 대응.
  - 알림 메시지는 영향 범위, 고객 영향도, 대응 담당자, 롤백/재시작 옵션을 포함합니다.

- **장애 대응 프로세스**: 에러 발생 시 Runbook 링크와 함께 대응 절차를 자동 공유하고, 재시도 큐(메시지 DLQ)와 [9.2 외부 시스템 연동](09_communication.md#92-외부-시스템-연동-방식-async--non-blocking)에서 정의한 폴백 로직을 확인합니다. 장애 해결 후 `Postmortem` 문서를 48시간 내 작성하여 재발 방지 액션 아이템을 추적합니다.

- **품질 개선 사이클**: 에러 빈도/재발률을 월간 품질 회의에서 검토하고, 반복되는 예외는 [13. 에러 처리](13_error_handling.md) 개선 과제 및 코드 리팩터링 백로그로 이관합니다. 알림의 민감도를 정기적으로 조정하여 경보 피로(Alert Fatigue)를 예방합니다.

---

