# Spring Develop 01

## Spring Webflux 환경 구축
- Spring Webflux 관련 dependency 추가
```groovy
implementation("org.springframework.boot:spring-boot-starter-webflux:2.5.4")
```
- MariaDB(R2DBC) 관련 dependency 추가
````groovy
runtimeOnly("org.mariadb:r2dbc-mariadb:1.0.2")
````

- MariaDB 관련 property 추가
```properties
# datasource
spring.r2dbc.url=r2dbc:mariadb://maria-database-1.ciroumm1ym9r.ap-northeast-2.rds.amazonaws.com
spring.r2dbc.name=EMS
spring.r2dbc.username=admin
spring.r2dbc.password=admin2021
```

