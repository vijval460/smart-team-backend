# Teamforge Backend - Team Matching REST API

A Spring Boot REST API that powers **Teamforge**, an intelligent project team formation and skill matching platform. It matches required project skills with member capabilities and experience to recommend optimal team compositions.

---

## 🛠 Tech Stack & Requirements

- **Java**: OpenJDK 17 or higher (tested with Java 17 and Java 26)
- **Framework**: Spring Boot 3.4.4
- **Database**: MySQL 8.0+ (supports local MySQL, TiDB, Aiven, Railway, Supabase MySQL)
- **Security**: Spring Security 6 with session management and BCrypt password hashing
- **Build Tool**: Apache Maven (Maven Wrapper included via `./mvnw` / `mvnw.cmd`)

---

## 🚀 Getting Started Locally

### 1. Prerequisites
- Java 17+ installed and configured on your `PATH`.
- MySQL Server 8.0+ running locally.

### 2. Database Setup
Create the MySQL database:
```sql
CREATE DATABASE IF NOT EXISTS teamforge CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Environment Variables
Copy `.env.example` to `.env` or set environment variables:

| Variable | Description | Default (Local) |
|---|---|---|
| `PORT` | HTTP port for the backend server | `8081` |
| `SPRING_DATASOURCE_URL` | JDBC URL for MySQL database | `jdbc:mysql://localhost:3306/teamforge?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true` |
| `SPRING_DATASOURCE_USERNAME` | MySQL database username | `root` |
| `SPRING_DATASOURCE_PASSWORD` | MySQL database password | `123456` |
| `TEAMFORGE_CORS_ALLOWED_ORIGINS` | Comma-separated allowed frontend origins | `http://localhost:5173,http://127.0.0.1:5173` |
| `SESSION_COOKIE_SAMESITE` | Cookie SameSite policy (`lax` or `none`) | `lax` (`none` for cross-site HTTPS) |
| `SESSION_COOKIE_SECURE` | Secure cookie flag | `false` (`true` for HTTPS) |
| `TEAMFORGE_DEMO_EMAIL` | Default seeded demo user email | `demo@teamforge.dev` |
| `TEAMFORGE_DEMO_PASSWORD` | Default seeded demo user password | `teamforge123` |

### 4. Running the Application
Using the Maven wrapper (no pre-installed Maven required):

**On Windows (PowerShell / Command Prompt):**
```powershell
.\mvnw.cmd spring-boot:run
```

**On Linux / macOS:**
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8081`.

### 5. Running Tests
Run the automated test suite:
```powershell
.\mvnw.cmd test
```

---

## 📡 API Endpoints

### Health & Monitoring
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/` or `/api/health` | Public | Service health check |

### Authentication
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register a new user account and initiate session |
| `POST` | `/api/auth/login` | Public | Authenticate user credentials and return profile |
| `GET` | `/api/auth/me` | Authenticated | Retrieve currently authenticated user profile |
| `POST` | `/api/auth/logout` | Public / Authenticated | Invalidate session and log out |

### Team Members & Recommendations
| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/members` | Authenticated | List all team members sorted by name |
| `POST` | `/api/projects/recommend` | Authenticated | Generate recommended team composition based on project brief |

#### Example Recommendation Request:
```json
{
  "name": "Customer Insights Platform",
  "description": "Build an analytics dashboard with real-time feedback processing",
  "requiredSkills": ["React", "Spring Boot", "Python", "UI/UX"],
  "teamSize": 4,
  "priority": "balanced"
}
```

---

## 🐳 Docker Build

A multi-stage Dockerfile is provided for containerized deployment:

```bash
docker build -t teamforge-backend .
docker run -p 8081:8081 -e SPRING_DATASOURCE_URL=... -e SPRING_DATASOURCE_USERNAME=... -e SPRING_DATASOURCE_PASSWORD=... teamforge-backend
```

---

## ☁️ Deployment Guide (Render / Railway)

1. Connect the repository to your hosting platform (e.g. Render Web Service or Railway).
2. Set Environment / Runtime to **Java** or **Docker**.
3. Build command: `./mvnw clean package -DskipTests`
4. Start command: `java -jar target/team-match-0.0.1-SNAPSHOT.jar`
5. Configure the environment variables:
   - `SPRING_DATASOURCE_URL`: Managed MySQL connection URL
   - `SPRING_DATASOURCE_USERNAME`: Database username
   - `SPRING_DATASOURCE_PASSWORD`: Database password
   - `TEAMFORGE_CORS_ALLOWED_ORIGINS`: Deployed frontend domain (e.g., `https://teamforge-frontend.vercel.app`)
   - `SESSION_COOKIE_SAMESITE`: `none`
   - `SESSION_COOKIE_SECURE`: `true`
