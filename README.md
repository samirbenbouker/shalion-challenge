# Challenge - Schools & Students API

REST API built with Spring Boot and PostgreSQL to manage schools and students.

## Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- OpenAPI / Swagger UI
- Docker

## Requirements
- Java 21
- Gradle (or Gradle Wrapper)
- PostgreSQL 13+

## Configuration
Update `src/main/resources/application.properties` if needed:

```properties
app.datasource.url=jdbc:postgresql://localhost:5433/postgres
app.datasource.username=postgres
app.datasource.password=
```

You can also use environment variables:
- `APP_DATASOURCE_URL`
- `APP_DATASOURCE_USERNAME`
- `APP_DATASOURCE_PASSWORD`

## Run Locally
```bash
./gradlew bootRun
```

## API Documentation (Swagger)
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Implemented Endpoints
### Schools
- `POST /schools`
- `PUT /schools/{id}`
- `DELETE /schools/{id}`
- `GET /schools?name=&page=&size=&sort=`
- `GET /schools/{id}`

### Students
- `POST /students`
- `PUT /students/{id}`
- `DELETE /students/{id}`
- `GET /students/{id}`
- `GET /schools/{schoolId}/students?name=&page=&size=&sort=`

## Pagination and Sorting
Collection endpoints are paginated.

Query parameters:
- `page`: zero-based page index (example: `0`)
- `size`: page size (example: `10`)
- `sort`: format `field,direction` (example: `name,asc`)

Examples:
- `GET /schools?name=academy&page=0&size=10&sort=name,asc`
- `GET /schools/1/students?name=ana&page=0&size=20&sort=id,desc`

## Business Rules Implemented
- `School.id` is unique.
- `School.name` is unique.
- `School.maxCapacity` must be between `50` and `2000`.
- Each `Student` must belong to exactly one school.
- A student cannot be enrolled in a school that has reached max capacity.
- School name duplication returns `409 Conflict`.
- Enrolling in a full school returns `409 Conflict`.
- Name search is partial and case-insensitive (`ContainingIgnoreCase`, functionally equivalent to `ILIKE`).

## Validation and Error Handling
Standard error payload:

```json
{
  "timestamp": "2026-04-15T15:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "School name already exists",
  "path": "/schools"
}
```

HTTP status codes:
- `400 Bad Request`: invalid input, malformed request, invalid query params
- `404 Not Found`: school or student not found
- `409 Conflict`: duplicated school name or school without available capacity

## Build and Test
```bash
./gradlew clean build
./gradlew test
```

## Docker
Build image:
```bash
docker build -t schools-api:latest .
```

Run container:
```bash
docker run --rm -p 8080:8080 \
  -e APP_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/postgres \
  -e APP_DATASOURCE_USERNAME=postgres \
  -e APP_DATASOURCE_PASSWORD= \
  schools-api:latest
```

## Notes
- The project is configured with Gradle (`build.gradle`) and Gradle Wrapper (`gradlew`, `gradlew.bat`).
- On every app startup, an idempotent seed script runs automatically:
  - `School 1` with `100` capacity and `50` students
  - `School 2` with `80` capacity and `80` students
  - `School 3` with `120` capacity and `1` student
