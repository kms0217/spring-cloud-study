# Spring Cloud Gateway(SGC)
- SCG는 MSA환경에서 사용되는 API Gateway 구현체중 하나이다.
- API Gateway는 Client의 요청에 따라 유연하게 대처하기 위해서 필요하다.
- API Gateway는 인증/인가, Load Balancing, 로깅등의 역할을 수행한다.

### 실습
- API Server 
  - port : 8080
  
  |Method|Endpoint|response|
  |---|---|---|
  |GET|/v1.0/tests/hello|"Hello World"|
  |GET|/v1.0/time/hi|"Hi World|
- SGC Server
  - port : 9090

### Dependency
- spring cloud, gateway 추가
```
ext {
  set('springCloudVersion', "2020.0.3")
}

dependencies {
  implementation 'org.springframework.cloud:spring-cloud-stater-gateway
  ...
}

dependencyManagement {
  imports {
    mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}
  }
}
```

### SGC Setting
- application.yml에 gateway 설정이 가능하다.
- spring.cloud.gateway 
- filter의 경우 AbstractGatewayFilterFactory를 상속 받도록 작성해 Class명을 넣어주고, 인자또한 지정할 수 있다.
- route 설정의 predicates를 통해 Path를 지정해 요청을 route할 수 있으며 Route별로 filter적용이 가능하다.
- 별도의 filter없이 predicates를 통해 특정 시간 이후 혹은 특정 헤더를 포함한 요청만 허용하도록 설정할 수 있다.
- After : 특정 시간 이후부터 라우팅 처리
  ```
  routes:
    - id: time-service
      uri: http://localhost:8080/
      predicates:
        - Path=/v1.0/time/**
        - After=2021-08-31T16:46:00.000+09:00[Asia/Seoul]
  ```
- Before : 특정 시간 이전까지만 라우팅 처리 (해당 시간 이후부터 라우팅 처리하지 않음)
- Between : 특정 시간 사이에만 라우팅 처리
  ```
  routes:
    - id: time-service
      uri: http://localhost:8080/
      predicates:
        - Path=/v1.0/time/**
        - Between=2021-08-31T00:00:00.000+09:00[Asia/Seoul], 2021-09-05T00:00:00.000+09:00[Asia/Seoul]
  ```
- Header : 특정 헤더 포함한 요청만 라우팅 처리
  ```
  routes:
    - id: time-service
      uri: http://localhost:8080/
      predicates:
        - Path=/v1.0/header/**
        - Header=X-TEST-API-VERSION, 1.0
  ```

### Filter
- AbstractGatewayFilterFactory을 상속받아 구현

### Error Handler
- After, Header 혹은 잘못된 요청의 경우 Error Page가 반환된다. 
- Error에 대해서 Json으로 응답을 내려주기 위한 Error Handler를 작성해야 더 좋은 서비스가 가능하다.
