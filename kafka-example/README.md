# Kafka

### Kafka 구성

- publisher(producer, sender), consumer(receiver)
    - pub/sub방식
    - publisher는 메시지를 topic을 통해 카테고리화, consumer는 원하는 topic를 subscribe
    - publisher, subscriber는 서로를 모르고 topic에 대한 정보만 알고 있다.
- 토픽
    - 메시지를 구분하는 논리적 단위 (ex. 주문에 대한 메시지 → order라는 Topic)
    - 토픽은 여러개의 파티션으로 구성 가능
- 파티션
    - 분산 처리를 위한 브로커 내부에 메시지가 저장되는 물리적 단위
    - 하나의 파티션 내에서는 메시지 순서가 보장
        - 하나의 토픽이 여러 파티션으로 분산되는 경우 토픽 단위의 메시지 순서는 보장되지 않음
    - Kafka옵션의 replica만큼 파티션이 각 서버들에게 복제되고, 복제된 파티션 중에서 하나의 리더가 모든 읽기, 쓰기를 담당한다. 리더 파티션에 쓰기가 완료되면 각 파티션의 replica로 복제를 수행한다.
- 클러스터
    - 확장성, 고가용성을 위해 broker들이 클러스터로 구성되어 동작
    - broker에 대한 분산 처리는 zooKeeper를 통해 관리
- 메시지
    - Key, Value로 구성
- 커밋(commit)
    - consumer group이 어디까지 메시지를 읽었는지 다음 읽어야 할 offset 업데이트 하는 동작

### Kafka 동작 방식

- publisher는 메시지를
- broker는 publisher로 부터 받은 메시지를 디스크에 저장 (append-only)
    - RabbitMQ는 Memory에 저장하는 방식이다.
    - 디스크에 메시지를 저장하기 때문에 Memory저장 방식보다 유실확률이 적다.
    - 디스크의 연속된 공간을 사용하기 때문에 속도면에서 생길 수 있는 차이를 극복한다.
- 메시지를 저장할 때 내용과 오프셋이 저장
    - 오프셋은 메시지를 구분하는 식별자
- consumer가 poll()을 호출에 아직 읽지 않은 메시지를 가져온다.

### Kafka docker-compose

```java
version: '2'
services:
  zookeeper:
    container_name: zookeeper
    image: wurstmeister/zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ADVERTISED_PORT: 9092
      KAFKA_CREATE_TOPICS: "testTopic"
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

### Publisher(Sender)

- application.yml

    ```java
    spring:
      kafka:
      bootstrap-servers: 127.0.0.1:9092
    ```

- RabbitMQ와 비슷하게 KafkaTemplate를 통해 전송할 수 있다.
- 지금 예제에서는 단순 String을 전송하지만 만약 객체를 전송하는 경우 다음 세 가지 방법을 사용하면 될 것 같다.
    1. Spring에서 ProducerFactory를 사용해 Serializer등록

        ```java
        @Bean
        public ProducerFactory<String, TestDto> producerFactory() {
        	Map<String,Object> configs = new HashMap<>();
        	configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        	configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        	configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        	return new DefaultKafkaProducerFactory(configs);
        }

        @Bean
        public KafkaTemplate<String, TestDto> kafkaTemplate() {
        	return new KafaTemplate<>(producerFactory());
        }
        ```

    2. application.yml에 key-serializer, value-serializer을 작성해서 설정

        ```java
        spring:
          kafka:
            producer:
              bootstrap-servers: 127.0.0.1:9092
              key-serializer: org.apache.kafka.common.serialization.StringSerializer
              value-serializer: org.apache.kafka.common.serialization.JsonSerializer
        ```

    3. Controller나 send를 수행하는 메서드에서 직접 객체를 Json으로 변환한 뒤 KakfaTemplate<String, String>을 사용해 전송

### Consumer(Receiver)

- application.yml

    ```java
    spring:
      kafka:
        consumer:
          bootstrap-servers: 127.0.0.1:9092
          group-id: test
          enable-auto-commit: true
          auto-commit-interval: 500ms
          auto-offset-reset: latest

    server:
      port: 8081
    ```

    - group-id : consumer Group Id
    - enable-auto-commit : auto-commit-interval마다 commit
        - 매 poll()마다 시간을 확인해 commit
    - auto-commit-interval : commit interval
    - auto-offset-reset : Kafka 서버의 초기 offset이 없거나 offset이 더 이상 없는 경우에 대한 옵션
        - latest : 가장 최근에 생산된 메시지로 offset reset
        - earliest : 가장 오래된 메시지로 offset reset
        - none : Exception 발생
- Deserialize
    - Publisher와 마찬가지로 설정이 가능하다.
