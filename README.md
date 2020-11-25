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



Spring Cloud Config Setting (RDB Backend)
스키마 정보
    https://github.com/spring-cloud/spring-cloud-config/blob/master/spring-cloud-config-server/src/test/resources/schema-jdbc.sql
    KEY 가 예약어 이므로 변경 
    ```
        CREATE TABLE PROPERTIES (
          `PROPERTY_KEY` VARCHAR(2048),  
          `PROPERTY_VALUE` VARCHAR(4096),  
          `APPLICATION` VARCHAR(128),  
          `PROFILE` VARCHAR(128),  
          `LABEL` VARCHAR(128)  
        );
    ```

cloud config server 의 8888 port를 사용하는 관례가 있어서 설정  
`spring.profiles.active=jdbc` jdbc로 설정 해줘야함 (jdbc backend 사용 시) 
