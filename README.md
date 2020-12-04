Ref
---
- https://kream.co.kr/about/
Bounded Context
---
- Search (api)  
- Product  
- User  

Search
---
- Elastic Search Api  
- 다의어, 금지어 검색 사전 (필요)  
- 데이터 저장소 (s3..?)
  
Product  
---
- Product  
    - Elastic Search 결과 값을 통해서 Product 객체 추출
    
- Object Relation
    - Product -> ProductGroup -> ProductOption

User
---
- My page
- Like
- Storage
- info


# Spring Cloud Config Setting (Git Backend)
```text
* 주의 사항
for me this caused due to my default branch 'main'. whenever you create a new branch the default branch is 'main' not 'master' anymore. but spring server config look for master branch.
git checkout -b master git push --set-upstream origin master
```

 
# Spring Cloud Config Setting (RDB Backend)
```text
- 스키마 정보
https://github.com/spring-cloud/spring-cloud-config/blob/master/spring-cloud-config-server/src/test/resources/schema-jdbc.sql
# KEY 가 예약어 이므로 변경


cloud config server 의 8888 port를 사용하는 관례가 있어서 설정  
`spring.profiles.active=jdbc` jdbc로 설정 해줘야함 (jdbc backend 사용 시) 

```
 
```sql
CREATE TABLE PROPERTIES (
          `PROPERTY_KEY` VARCHAR(2048),  
          `PROPERTY_VALUE` VARCHAR(4096),  
          `APPLICATION` VARCHAR(128),  
          `PROFILE` VARCHAR(128),  
          `LABEL` VARCHAR(128)  
        );
```

# Config 서버의 설정 확인
- http://{config server}/{application name}/{profile}

# Config 서버의 가용성
- Config 서버가 1대 라면 SPOF(single point of failure, 단일장애지점)가 된다. *spof는 라이브환경에서는 언제나 재앙의 대상이 된다.
- 하지만 Spring Cloud Config Server 는 일반적인 SPOF에 비해 그 부담이 다소 적다.
    - 운영중 가용성
        - Spring Cloud Config Server와 연동하는 Client는 최소 구동 시에 서버에 설정 값을 읽는다. 이후 Client 측에 로컬 캐시되어 더 이상 Config 서버와 통신하지 않는다.
        - 다만, 설정 값이 갱신될 경우 클라이언트의 `/actuator/refresh` 엔드포인트를 사용해서 새로운 값을 다시 읽어 온다. 이때 서버의 응답이 없으면 기존 로컬 캐시 값을 사용한다.
        - 즉 Config Server 가 응답이 없어도 Client의 큰 문제가 되지 않는다. (서버 변경 값을 동기화 못할 경우, 일관성 문제는 생길 수 있다.)
        
    - 최초 구동시 가용성
        - Client의 최초 구동될 때 Config Server가 응답이 없는 경우 가용성을 위해서 `fail-fast` 라는 설정 값(default=false)이 있다.
        - `fail-fast=true` 일 땐 Config Server와 연결하지 못하면 Client는 구동되지 않는다. (환경 셋팅 마다 다르게 설정할 수 있다.)  
        - Config Server의 정보 외에 Client가 동일한 설정을 자신의 설정 파일에 가지고 있으면 최소한의 가용성 확보를 할 수 있다.
    
    - 보다 나은 가용성
        - Config Server 자체를 이중화

# 설정 정보의 실시간 동기화
- Config Server의 설정 정보가 변경되면 의존하고 있는 Clients에 이벤트를 전파 해야한다.
- 이때 사용되는 것이 `@RefeshScope` 어노테이션과 `/actuator/refresh` 엔드포인트 이다. (이 두 작억은 클라이언트 측에서 수행)
- 위의 로직을 수행하면 로컬에 캐시된 설정 정보를 Config Server에서 가져온 값으로 갱신
- Config Server에 의존하는 Client가 많아지면 `/actuator/refresh` 엔드포인트를 명시적으로 호출해 주어야 하기 때문에 유지보수가 어려움
- Spring Cloud Bus를 사용하면 메시지 브로커를 통해 변경 이벤트를 구독하여 자동으로 모든 Client에 변경 정보를 자동으로 동기화 할 수 있다.
