# Cabinet Medical Microservices

The project contains three independently deployable Spring Boot services. Each service owns its API and database; the Nginx gateway exposes them on one local port.

| Service | Responsibility | Local port | API |
|---|---|---:|---|
| `auth-service` | Account registration, login, and JWT issuance | 8081 | `/api/auth` |
| `patient-service` | Patient records | 8082 | `/api/patients` |
| `appointment-service` | Appointment scheduling and status | 8083 | `/api/appointments` |
| `gateway` | Routes API requests to services | 8080 | `/api/*` |

## Run the complete stack

Install Docker Desktop, then from the repository root run:

```powershell
docker compose up --build
```

The gateway is available at `http://localhost:8080`. PostgreSQL stores the three services' data in separate databases on a shared local database container. Data is retained in the `postgres_data` volume.

The Compose defaults are for local development only. Before exposing this stack, set strong, unique values for `JWT_SECRET` and `POSTGRES_PASSWORD`, configure TLS at the gateway, and replace `DDL_AUTO=update` with a managed schema migration strategy.

To run a service without Docker, run `.\mvnw.cmd spring-boot:run` from that service's directory. It uses an in-memory H2 database by default. Local ports are 8081 (auth), 8082 (patient), and 8083 (appointment).

## API examples

Register a user and capture the returned token:

```powershell
$token = (Invoke-RestMethod -Method Post http://localhost:8080/api/auth/register `
  -ContentType 'application/json' `
  -Body '{"email":"doctor@example.com","password":"ChangeMe123"}').accessToken
```

Login uses the same JSON shape as registration:

```http
POST /api/auth/login
Content-Type: application/json

{"email":"doctor@example.com","password":"ChangeMe123"}
```

Patient operations and appointment operations require `Authorization: Bearer <accessToken>`.

Create a patient:

```http
POST /api/patients
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "nom": "Alami",
  "prenom": "Sara",
  "dateNaissance": "1990-04-12",
  "email": "sara@example.com",
  "telephone": "0600000000",
  "adresse": "Casablanca",
  "groupeSanguin": "O_POSITIF"
}
```

Create an appointment (`dateHeure` must be in the future):

```http
POST /api/appointments
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "patientId": 1,
  "dateHeure": "2030-04-12T10:30:00",
  "motif": "Consultation"
}
```

Appointments store `patientId` as a service-owned reference, not a database foreign key. This avoids coupling the appointment database to patient-service storage; clients should use the patient API to ensure a patient exists.

`GET /actuator/health` is exposed by each service for health checks. OpenAPI UI is available directly on patient and appointment service instances at `/swagger-ui.html`.

## Build and test

Run tests independently from each service directory:

```powershell
.\mvnw.cmd test
```
