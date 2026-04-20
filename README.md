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
app.datasource.url=jdbc:postgresql://localhost:5432/postgres
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

## Notes
Can modify this initial startup in /config/InitialDataSeeder.java
- The project is configured with Gradle (`build.gradle`) and Gradle Wrapper (`gradlew`, `gradlew.bat`).
- On every app startup, an idempotent seed script runs automatically:
  - `School 1` with `100` capacity and `50` students
  - `School 2` with `80` capacity and `80` students
  - `School 3` with `120` capacity and `1` student

## Implemented Endpoints
### School Endpoints
| Method | Endpoint | Request Object | Response |
|---|---|---|---|
| `GET` | `/schools/{id}` | `None` | `200 OK` + `SchoolResponse` (includes students) |
| `GET` | `/schools?name=&page=&size=&sort=` | `None` (query params only) | `200 OK` + `Page<SchoolResponse>` |
| `POST` | `/schools` | `SchoolRequest` | `201 Created` + `SchoolResponse` |
| `PUT` | `/schools/{id}` | `SchoolRequest` | `200 OK` + `SchoolResponse` |
| `DELETE` | `/schools/{id}` | `None` | `204 No Content` |

### Student Endpoints
| Method | Endpoint | Request Object | Response |
|---|---|---|---|
| `GET` | `/students/{id}` | `None` | `200 OK` + `StudentResponse` |
| `POST` | `/students` | `StudentRequest` | `201 Created` + `StudentResponse` |
| `PUT` | `/students/{id}` | `StudentRequest` | `200 OK` + `StudentResponse` |
| `DELETE` | `/students/{id}` | `None` | `204 No Content` |

### School & Student Endpoints
| Method | Endpoint | Request Object | Response |
|---|---|---|---|
| `GET` | `/schools/{schoolId}/students?name=&page=&size=&sort=` | `None` (query params only) | `200 OK` + `Page<StudentResponse>` |

### Asynchronous Enlistment Endpoints
| Method | Endpoint | Request Object | Response |
|---|---|---|---|
| `POST` | `/enlistments` | `EnlistmentRequest` | `202 Accepted` + `EnlistmentAcceptedResponse` |
| `GET` | `/enlistments/{processId}` | `None` | `200 OK` + `EnlistmentStatusResponse` |
| `GET` | `/enlistments?page=&size=&sort=` | `None` (query params only) | `200 OK` + `Page<EnlistmentStatusResponse>` |


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

## cURL Examples
Use these commands directly against `http://localhost:8080`:

#### Create school
```bash
curl -X POST "http://localhost:8080/schools" \
  -H "Content-Type: application/json" \
  -d '{"name":"My School","maxCapacity":100}'
```

#### Update school
```bash
curl -X PUT "http://localhost:8080/schools/1" \
  -H "Content-Type: application/json" \
  -d '{"name":"My School Updated","maxCapacity":120}'
```

#### Delete school
```bash
curl -X DELETE "http://localhost:8080/schools/1"
```

#### Search schools (name + pagination + sort)
```bash
curl -X GET "http://localhost:8080/schools?name=school&page=0&size=10&sort=name,asc"
```

#### School detail (includes students)
```bash
curl -X GET "http://localhost:8080/schools/1"
```

#### Create student
```bash
curl -X POST "http://localhost:8080/students" \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","schoolId":1}'
```

#### Update student
```bash
curl -X PUT "http://localhost:8080/students/1" \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Updated","schoolId":1}'
```

#### Delete student
```bash
curl -X DELETE "http://localhost:8080/students/1"
```

#### Student detail
```bash
curl -X GET "http://localhost:8080/students/1"
```

#### Search students in a school (name + pagination + sort)
```bash
curl -X GET "http://localhost:8080/schools/1/students?name=ali&page=0&size=10&sort=id,asc"
```

#### Start asynchronous enlistment
```bash
curl -X POST "http://localhost:8080/enlistments" \
  -H "Content-Type: application/json" \
  -d '{"studentId":1,"schoolId":2}'
```

#### Get enlistment process status/result
```bash
curl -X GET "http://localhost:8080/enlistments/{processId}"
```

#### List all enlistment processes (paginated)
```bash
curl -X GET "http://localhost:8080/enlistments?page=0&size=10&sort=createdAt,desc"
```

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
  -e APP_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/postgres \
  -e APP_DATASOURCE_USERNAME=postgres \
  -e APP_DATASOURCE_PASSWORD= \
  schools-api:latest
```
