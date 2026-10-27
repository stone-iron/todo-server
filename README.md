# TodoList 백엔드 서버

React 할 일 목록(TodoList) 화면에서 사용하는 REST API 서버입니다.

## 기술 스택
- Java 17, Spring Boot 3.4.1, Gradle
- MyBatis, MySQL
- PageHelper (페이징), Springdoc OpenAPI (Swagger UI)
- 테스트: JUnit 5, MockMvc, H2 (MySQL 호환 모드)

## 프로젝트 구조
```
src/main/java/com/aloha/todo
├── TodoApplication.java
├── config/SwaggerConfig.java        API 문서 설정
├── controller/TodoController.java   REST API
├── controller/HomeController.java   / → Swagger UI
├── domain/Todos.java                할 일
├── domain/Pagination.java           페이징 정보
├── mapper/TodoMapper.java           MyBatis 매퍼
└── service/TodoService(Impl).java   비즈니스 로직
src/main/resources
├── application.properties
└── mybatis/mapper/TodoMapper.xml    SQL
SQL/todos.sql                        테이블 생성 + 샘플 데이터
```

## 실행 방법
1. MySQL에 데이터베이스와 테이블을 만듭니다.
   ```sql
   CREATE DATABASE IF NOT EXISTS aloha;
   USE aloha;
   -- SQL/todos.sql 실행
   ```
2. `src/main/resources/application.properties`의 DB 주소, 계정, 비밀번호를 환경에 맞게 수정합니다.
3. 서버를 실행합니다.
   ```bash
   ./gradlew bootRun        # Windows: gradlew.bat bootRun
   ```
4. 브라우저에서 `http://localhost:8080` 에 접속하면 Swagger UI로 이동해 API를 바로 실행해 볼 수 있습니다.

## API
| 메서드 | 주소 | 설명 | 응답 |
|---|---|---|---|
| GET | `/todos?page=1&size=10` | 목록 (페이징) | `{ list, pagination }` |
| GET | `/todos/{id}` | 단건 조회 | 할 일, 없으면 404 |
| POST | `/todos` | 등록 `{ "name": "할일" }` | 201 `SUCCESS`, 내용이 비면 400 |
| PUT | `/todos` | 수정 `{ "id": "...", "status": true }` | 200 `SUCCESS`, 없는 id면 400 |
| PUT | `/todos` | **전체 완료** `{ "id": null }` | 200 `SUCCESS` |
| DELETE | `/todos/{id}` | 단건 삭제 | 200 `SUCCESS`, 없는 id면 400 |
| DELETE | `/todos` | **전체 삭제** | 200 `SUCCESS` |

- 목록은 페이지 안에서 **미완료 → 완료**, 같은 상태면 **순서(seq)** 오름차순으로 정렬됩니다.
- 등록 시 `seq`를 보내지 않으면 마지막 순서 + 1로 저장됩니다.
- 모든 출처의 요청을 허용(`@CrossOrigin("*")`)하므로 React 개발 서버에서 바로 호출할 수 있습니다.

## 테스트
MySQL 없이 H2 메모리 DB로 전체 API를 검증합니다.
```bash
./gradlew test
```
