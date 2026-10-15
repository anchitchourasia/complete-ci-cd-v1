# 01 — File Inventory & System Audit

This document inventories all repository source files, configuration assets, external dependencies, and local host files inspected during the architectural analysis of the **VPMS** project on branch `v1`.

---

## 1. Inspected Repository Files

| Relative File Path | Purpose / Responsibility | Informs Document / Diagram | Status |
| :--- | :--- | :--- | :--- |
| `.github/workflows/cicd.yml` | Master CI/CD workflow orchestrating Maven build, Nexus publishing, Tomcat deployment, Docker backend deployment, and calling frontend workflow. | `02-cicd-flow`, `03-deployment-architecture`, `05-configuration-and-ports` | Confirmed from source |
| `.github/workflows/frontend-cicd.yml` | Reusable workflow (`workflow_call`) executing Node setup, Angular production build, Docker frontend image build, Compose deployment, and verification. | `02-cicd-flow`, `03-deployment-architecture`, `05-configuration-and-ports` | Confirmed from source |
| `docker-compose.yml` | Root multi-container definition defining `frontend` and `backend` services, host/container port mappings, bind mount for `application.properties`, and `vpms-net` bridge network. | `03-deployment-architecture`, `05-configuration-and-ports` | Confirmed from source |
| `backend/Dockerfile` | Backend container specification based on `eclipse-temurin:17-jdk-alpine`. Copies `target/vpms.war` and sets `ENTRYPOINT ["java", "-jar", "vpms.war"]`. | `02-cicd-flow`, `03-deployment-architecture` | Confirmed from source |
| `Frontend/Dockerfile` | Frontend container specification based on `nginx:stable-alpine`. Copies compiled Angular browser assets and custom `nginx.conf`. | `02-cicd-flow`, `03-deployment-architecture`, `04-runtime-request-flow` | Confirmed from source |
| `Frontend/nginx.conf` | Custom Nginx server block listening on port 80, handling root 302 redirects to `/vpms-ui/`, and SPA fallback routing via `try_files $uri $uri/ /vpms-ui/index.html`. | `03-deployment-architecture`, `04-runtime-request-flow` | Confirmed from source |
| `backend/pom.xml` | Maven Project Object Model defining Spring Boot 3.3.2, Java 17, Oracle JDBC (`ojdbc11`), JJWT 0.12.6, WAR packaging (`<finalName>vpms</finalName>`), and Nexus snapshot/release distribution repositories. | `02-cicd-flow`, `03-deployment-architecture`, `05-configuration-and-ports` | Confirmed from source |
| `backend/src/main/resources/application.properties` | Spring Boot configuration: port 3031, context-path `/vpms`, HikariCP pool, Oracle database connection string, file upload directory, and JWT secret/expiration. | `03-deployment-architecture`, `04-runtime-request-flow`, `05-configuration-and-ports` | Confirmed from source |
| `backend/src/main/java/com/heg/VpmsApplication.java` | Main application entry point containing `@SpringBootApplication` and standard `main()` method. Does not extend `SpringBootServletInitializer`. | `03-deployment-architecture`, `04-runtime-request-flow`, `09-open-questions` | Confirmed from source |
| `backend/src/main/java/com/heg/config/SecurityConfig.java` | Spring Security filter chain: enables CORS, disables CSRF, sets stateless session creation, configures JWT filter, and defines public request matchers. | `03-deployment-architecture`, `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/config/CorsConfig.java` | Global WebMvcConfigurer CORS configuration allowing origin `http://localhost:4200` with standard HTTP methods and credentials. | `04-runtime-request-flow`, `05-configuration-and-ports` | Confirmed from source |
| `backend/src/main/java/com/heg/security/JwtAuthenticationFilter.java` | Servlet filter intercepting incoming requests, extracting Bearer tokens from `Authorization` header, validating claims, and populating Spring Security context. | `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/controller/AuthController.java` | Authentication REST endpoints for `/api/auth/register`, `/api/auth/login`, and `/api/auth/change-password`. | `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/controller/PassRegistryController.java` | REST controller under `/api/passes` handling pass registration, multipart file attachments, status updates, and static API key header verification (`x-api-key`). | `04-runtime-request-flow`, `07-gaps-and-improvements` | Confirmed from source |
| `backend/src/main/java/com/heg/controller/EmployeeReportController.java` | REST controller under `/api/reports` querying employee department reports. | `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/controller/HistoryController.java` | REST controller under `/api/history` providing vehicle pass audit history. | `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/controller/UserManagementController.java` | REST controller under `/api/admin/users` providing administrative user operations. | `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/repository/AppUserRepository.java` | JPA repository executing native SQL query against `VPMS_AUTHORITY` table to look up user credentials. | `04-runtime-request-flow` | Confirmed from source |
| `backend/src/main/java/com/heg/service/FileStorageServiceImpl.java` | File storage implementation resolving target directory from `app.upload.dir` and saving files using timestamps. | `03-deployment-architecture`, `05-configuration-and-ports`, `09-open-questions` | Confirmed from source |
| `backend/src/test/java/com/heg/VehiclePassManagementSystemApplicationTests.java` | Baseline `@SpringBootTest` test class with an empty `contextLoads()` test method. | `02-cicd-flow`, `07-gaps-and-improvements` | Confirmed from source |
| `Frontend/package.json` | Node project specification: Angular 21 (`@angular/core: ^21.2.0`), `@angular/build: ^21.2.12`, `bootstrap-icons`, `jspdf`, `xlsx`. | `02-cicd-flow`, `05-configuration-and-ports` | Confirmed from source |
| `Frontend/angular.json` | Angular CLI configuration using `@angular/build:application` builder, base routing, assets, styles, and budget limits. | `02-cicd-flow`, `04-runtime-request-flow` | Confirmed from source |
| `Frontend/.gitignore` | Frontend ignore rules explicitly excluding `/src/environments/*` while retaining `!/src/environments/environment.example.ts`. | `01-file-inventory`, `05-configuration-and-ports` | Confirmed from source |
| `Frontend/src/environments/environment.example.ts` | Committed template showing expected structure: `production`, `apiBaseUrl`, `apiKey`, and `cvpsBaseUrl`. | `05-configuration-and-ports` | Confirmed from source |
| `Frontend/src/environments/environment.ts` | Local active environment file on host: targets `apiBaseUrl: 'http://RUNNER_HOST:9092/vpms'`, `apiKey: '<REDACTED>'`. | `04-runtime-request-flow`, `05-configuration-and-ports`, `09-open-questions` | Observed from local file |
| `Frontend/src/app/core/api.config.ts` | Master client-side API endpoint dictionary mapping endpoints for passes, authorities, reports, and CVPS. | `04-runtime-request-flow` | Confirmed from source |
| `Frontend/src/app/core/auth.service.ts` | Angular authentication service managing user session in `sessionStorage`, handling HTTP headers (`x-api-key`), and dispatching login requests. | `04-runtime-request-flow` | Confirmed from source |
| `Frontend/src/app/core/auth.guard.ts` | Asynchronous route guard using `toObservable(auth.sessionReady)` to protect routes until authentication state resolves. | `04-runtime-request-flow` | Confirmed from source |
| `Frontend/src/app/core/http-error.interceptor.ts` | HTTP interceptor transforming HTTP status codes (0, 401, 403, 404, 500) into user-friendly error logs. | `04-runtime-request-flow`, `06-operations-and-troubleshooting` | Confirmed from source |
| `Frontend/src/app/app.config.ts` | Angular application bootstrap config providing `withHashLocation()` routing and registering HTTP interceptors. | `04-runtime-request-flow` | Confirmed from source |
| `Frontend/src/app/login/login.ts` | Component handling employee credentials, password reset, and role delegation. | `04-runtime-request-flow` | Confirmed from source |

---

## 2. External Host Files (Inspected & Verified)

The pipeline workflows and startup scripts reference files residing on the self-hosted Windows machine outside the Git repository root. These were inspected using read-only techniques:

| External Path | System / Tool | Description & Findings (Sanitized) | Status |
| :--- | :--- | :--- | :--- |
| `C:\maven\settings.xml` | Apache Maven 3.9.12 | Contains authentication credentials for `nexus-releases` and `nexus-snapshots` under user `admin` (`<password>` redacted). Defines an active HTTP proxy `http://192.168.9.112:808` with non-proxy hosts `localhost\|127.0.0.1`. | Observed from local file |
| `E:\apache-tomcat-10.1.28\bin\startup.bat` | Apache Tomcat 10.1.28 | Windows batch script executed by `cicd.yml` to launch Tomcat in background via `cmd.exe /c start "" /b startup.bat` if port 8090 has no listener. | Observed from local file |
| `E:\apache-tomcat-10.1.28\bin\setenv.bat` | Apache Tomcat 10.1.28 | Startup environment script setting JVM argument: `CATALINA_OPTS=%CATALINA_OPTS% -Dspring.config.additional-location=file:///D:/frontend%%20pipeline/VPMS_Frontend/backend/src/main/resources/application.properties`. | Observed from local file |
| `E:\apache-tomcat-10.1.28\conf\server.xml` | Apache Tomcat 10.1.28 | Tomcat server configuration defining an active HTTP/1.1 connector on port `8090` (`<Connector port="8090" protocol="HTTP/1.1" ... />`), matching the port verified in `cicd.yml`. | Observed from local file |
| `E:\apache-tomcat-10.1.28\webapps` | Apache Tomcat 10.1.28 | Target deployment directory where `cicd.yml` copies `vpms.war`. Creates context path `/vpms`. | Observed from local file |
| `C:\Program Files\Docker\Docker\resources\bin\docker.exe` | Docker Desktop | Fixed CLI binary path referenced in both workflows to prevent PATH variable resolution conflicts on Windows. | Confirmed from configuration |
| `C:\Program Files\Java\jdk-17` | JDK 17 Runtime | Java Development Kit 17 installation folder prepended to `$env:PATH` in `cicd.yml`. | Confirmed from configuration |

---

## 3. Missing, Ignored, or Inaccessible Files

| File / Component | Expected Location | Consequence / Architectural Impact | Status |
| :--- | :--- | :--- | :--- |
| `backend/.dockerignore` | `backend/.dockerignore` | Without this file, `docker build` sends the entire `backend/` directory (including `.settings/`, `.mvn/`, and any local build artifacts) to the Docker daemon. | Not configured |
| `Frontend/.dockerignore` | `Frontend/.dockerignore` | Without this file, `docker build` transmits the entire `Frontend/` folder, including `node_modules/` (hundreds of megabytes) into the Docker build context. | Not configured |
| `Frontend/src/environments/environment.ts` | In Git repository | Ignored by `Frontend/.gitignore`. The workflow overcomes this by running a custom PowerShell step copying the file from the host's absolute path `D:\frontend pipeline\VPMS_Frontend\Frontend\src\environments\environment.ts` into the workspace. | Local only (untracked) |
| `SpringBootServletInitializer` | `backend/src/main/java/` | Spring Boot WAR packaging requires a class extending `SpringBootServletInitializer` to configure the `WebApplicationContext` when deployed to an external Tomcat container. This class is missing. | Not configured |
| `AuthorityController.java` | `backend/src/main/java/com/heg/controller/` | `AuthService.ts` calls `${environment.apiBaseUrl}/api/authority/login`, but no controller handles `/api/authority` (only `AuthController` at `/api/auth`). | Not configured |

---

## 4. Discovered Non-Relevant / Auxiliary Files

During the workspace inspection, the following auxiliary and IDE-specific files were discovered. They do not alter pipeline logic or deployment topology:

* `data.txt`: Contains a single scratch command `.\mvnw.cmd clean spring-boot:run -DskipTests`. Not referenced by any CI/CD workflow.
* `Frontend/-tree --name-only HEAD`: Artifact resulting from a misdirected Git terminal command.
* `backend/.classpath`, `backend/.project`, `backend/.settings/`: Eclipse IDE project metadata.
* `backend/[Help`: Artifact resulting from a shell escape command.
* `backend/src/main/model/.LCKVehicle.java~`: Lock file created by a text editor.
* `Frontend/resourses.txt`: Developer notes regarding UI assets and font configurations.
