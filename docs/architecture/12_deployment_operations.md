# 12. 배포 및 운영

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

AMS는 안정적인 서비스 제공을 위해 반복 가능한 배포 절차, 자동화된 품질 검증, 환경별 구성 관리 체계를 확립합니다. 본 절은 [2.1 고수준 아키텍처](02_architecture_diagrams.md#21-고수준-아키텍처-다이어그램-시스템-전체-구성도)의 인프라 구성, [7. 비동기 처리 전략](07_async_processing.md)의 백그라운드 작업, [10. 확장성 및 성능](10_scalability_performance.md)의 오토스케일 전략, [11. 모니터링 및 로깅](11_monitoring_logging.md)에서 수집한 지표와 연동되어 운영 효율을 극대화합니다.

### 12.1 배포 전략

- **전략 비교**:

  | 전략 | 개요 | 장점 | 고려 사항 |
  |------|------|------|-----------|
  | Blue-Green | 기존(Blue)과 신규(Green) 환경을 동시에 유지하고 트래픽을 전환 | 즉시 롤백 가능, 배포 위험 최소화 | 두 환경을 유지할 인프라 비용 발생 |
  | Canary | 전체 트래픽의 일부를 신규 버전에 점진 할당 | 문제 발생 범위 최소화, 실운영 데이터 검증 | 모니터링 지표(SLO) 기반 빠른 판단 필요 |
  | Rolling | 인스턴스를 순차 교체 | 추가 인프라 없이 점진 배포 | 일부 사용자 영향 가능, 세션 관리 주의 |

- **AMS 도입 로드맵**:
  1. **v1.0 (현재)**: Rolling 업데이트(Kubernetes Deployment 기본 전략) + Pre/Post 배포 체크리스트.
  2. **v1.1**: Blue-Green 환경 구성(EKS 또는 K8s 네임스페이스 이중화) 및 트래픽 스위칭(Ingress 가중치 조정) 자동화.
  3. **v1.2 이후**: Argo Rollouts 기반 Canary 전략 도입, 단계별 트래픽 비율(5% → 20% → 50% → 100%)과 자동 중단 조건을 정의.
  4. **장기**: 서비스 메시(Istio) 도입 시 동적 라우팅과 A/B 테스트를 연계.

- **관련 도구**: Kubernetes Deployment/Service, AWS ALB Weighted Target Group, Argo Rollouts, Helm, Terraform(IaC)으로 인프라 및 배포 구성을 표준화합니다.

### 12.2 CI/CD 파이프라인 개요

- **브랜치 전략**: `main`(배포용), `develop`(통합), 기능별 `feature/*` 브랜치를 운영하며, PR 머지 전 필수 리뷰와 체크를 거칩니다.
- **CI 단계 (GitHub Actions)**:
  1. **Build**: Gradle 빌드, Kotlin 컴파일, Docker 이미지 생성.
  2. **Test**: 단위 테스트(Kotest), 통합 테스트, DB 마이그레이션 Dry-Run.
  3. **Quality Gate**: Ktlint/Detekt, SonarQube, SCA(Dependabot, Trivy) 실행.
  4. **Artifact Publish**: 성공 시 컨테이너 이미지를 AWS ECR(or GHCR)에 Push하고, 버전 태그와 SBOM을 첨부.
- **CD 단계**:
  - Argo CD가 GitOps 방식으로 Kubernetes Manifest(Helm) 변경을 감지하고 Sync합니다.
  - 배포 전 Postgres/Mongo 마이그레이션 스크립트는 Liquibase/Flyway, Mongock으로 사전 실행합니다.
  - Canary/Blue-Green 배포 시 Argo Rollouts와 [11. 모니터링 및 로깅](11_monitoring_logging.md) 지표를 연동해 자동 중단 조건을 설정합니다.

### 12.3 무중단 배포 및 롤백 방안

- **무중단 전제 조건**:
  - 인스턴스는 Stateless([10.1 수평 확장](10_scalability_performance.md#101-수평-확장-전략-멀티-서버--멀티-인스턴스))하게 유지하고, 세션/캐시는 Redis에 저장.
  - Readiness/Liveness Probe(`/actuator/health/readiness`, `/actuator/health/liveness`)를 정의하여 트래픽 전환 전 헬스 확인.
  - 비동기 작업([7. 비동기 처리 전략](07_async_processing.md))은 재시도 가능하도록 멱등 설계를 적용.
- **트래픽 전환 절차**:
  1. 신규 버전 Pod 배포 → Readiness 성공 확인.
  2. 트래픽 가중치 조정(Blue-Green/Canary).
  3. [11. 모니터링 및 로깅](11_monitoring_logging.md) 대시보드에서 오류율, 응답시간, 메시지 큐 적체량을 실시간 확인.
  4. 안정화 기간 동안 알림 채널(Slack/On-call) 모니터링.
- **롤백 정책**:
  - 지표 임계치 초과 또는 에러 알림 발생 시 자동으로 이전 버전 Manifest로 Rollback.
  - 데이터 마이그레이션 실패 시 트랜잭션 로그를 기준으로 되돌리며, 필요 시 Read Replica로 Failover.
  - 배포 이력과 릴리스 노트를 `docs/change-log.md`와 Git Tag로 관리하여 추적성을 확보.

### 12.4 설정 관리 및 환경 전략

- **구성/비밀 관리**:
  - Spring Cloud Config(또는 AWS Parameter Store)로 애플리케이션 설정을 중앙화하고, 환경별 Profile(`application-dev.yml`, `-staging`, `-prod`)로 분리.
  - 비밀 값은 AWS Secrets Manager/Kubernetes Secrets로 저장하며, Sealed Secrets로 Git 저장소 내 암호화 상태 유지.
- **환경 분리 전략**:
  - Dev → Staging → Prod 3계층 파이프라인을 유지하며, 각 환경은 독립 Kubernetes 네임스페이스/DB 인스턴스를 사용.
  - Staging은 Prod와 동일한 IaC 템플릿을 사용해 구성 차이를 최소화하고, Canary 테스트는 Staging에서 실사용 데이터 샘플을 Masking 후 활용.
- **릴리즈 토글 및 실험 관리**:
  - Feature Flag 서비스(LaunchDarkly, Unleash)로 신규 기능을 점진적으로 노출.
  - 플래그 상태는 로그/모니터링 지표에 포함하여 장애 원인 추적을 용이하게 합니다.
- **운영 거버넌스**:
  - 설정 변경은 Git PR → 리뷰 → Merge → Argo CD Sync 절차를 거치며, 변경 감사 로그를 남깁니다.
  - 월간 운영 회의에서 배포 빈도, 실패율, Mean Time To Recovery(MTTR)를 리뷰하고 개선 과제를 정의합니다.

---

