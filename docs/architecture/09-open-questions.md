# 09 — Open Architectural Questions & Discrepancies

This document highlights unresolved technical discrepancies and ambiguities discovered in the codebase that directly impact system architecture and runtime diagrams.

---

## 1. Port Discrepancy: `9092` in `environment.ts` vs. `8092` in Docker Compose

* **The Discrepancy**:
  - In [`Frontend/src/environments/environment.ts#L3`](file:///D:/frontend%20pipeline/VPMS_Frontend/Frontend/src/environments/environment.ts#L3), the active configuration specifies:
    ```typescript
    apiBaseUrl: 'http://192.168.9.130:9092/vpms'
    ```
  - In [`docker-compose.yml#L14`](file:///D:/frontend%20pipeline/VPMS_Frontend/docker-compose.yml#L14) and [`.github/workflows/cicd.yml#L64`](file:///D:/frontend%20pipeline/VPMS_Frontend/.github/workflows/cicd.yml#L64), the backend container is published to host port **`8092`**:
    ```yaml
    ports:
      - "8092:3031"
    ```
* **Why It Matters**:
  Because Angular runs in client browsers and communicates directly with `apiBaseUrl`, if a browser attempts to reach port `9092` while Docker is listening on port `8092`, all HTTP requests will fail with `ERR_CONNECTION_REFUSED` unless an external network router or NAT rule maps port 9092 to 8092.
* **Information Needed to Resolve**:
  Confirmation from the network administrator or development team whether port `9092` is forwarded externally on the corporate router, or if it is a configuration typo in `environment.ts`.
* **Diagrams Affected**:
  - [03-deployment-architecture.md](./03-deployment-architecture.md) (Host port mapping and remote boundary).
  - [04-runtime-request-flow.md](./04-runtime-request-flow.md) (Browser API destination port).

---

## 2. Authentication Endpoint Mismatch: `/api/authority/login` vs. `/api/auth/login`

* **The Discrepancy**:
  - In [`Frontend/src/app/core/api.config.ts#L9`](file:///D:/frontend%20pipeline/VPMS_Frontend/Frontend/src/app/core/api.config.ts#L9) and [`Frontend/src/app/core/auth.service.ts#L82`](file:///D:/frontend%20pipeline/VPMS_Frontend/Frontend/src/app/core/auth.service.ts#L82), the frontend dispatches login requests to:
    ```typescript
    `${API_CONFIG.AUTHORITY_BY_EMP}/login` // Evaluates to: /vpms/api/authority/login
    ```
  - In the Spring Boot backend, the authentication controller ([`AuthController.java#L26-L54`](file:///D:/frontend%20pipeline/VPMS_Frontend/backend/src/main/java/com/heg/controller/AuthController.java#L26-L54)) is annotated with:
    ```java
    @RequestMapping("/api/auth")
    @PostMapping("/login") // Endpoint is: /vpms/api/auth/login
    ```
  - No controller in the backend codebase maps to `/api/authority`.
* **Why It Matters**:
  If the frontend sends login requests to `/api/authority/login`, Spring Boot will return HTTP 404 (Not Found).
* **Information Needed to Resolve**:
  Verification of whether an external API Gateway rewrites `/api/authority` to `/api/auth`, or whether `api.config.ts` has an outdated endpoint definition.
* **Diagrams Affected**:
  - [04-runtime-request-flow.md](./04-runtime-request-flow.md) (Sequence Diagram Step 2: Login URL).

---

## 3. Standalone Tomcat WAR Bootstrapping (`SpringBootServletInitializer`)

* **The Discrepancy**:
  - The pipeline copies `vpms.war` into external Apache Tomcat 10.1.28 (`E:\apache-tomcat-10.1.28\webapps`).
  - In Spring Boot 3, deploying an executable WAR into an external servlet container requires the main application class to extend `org.springframework.boot.web.servlet.support.SpringBootServletInitializer` and override `configure()`.
  - [`VpmsApplication.java`](file:///D:/frontend%20pipeline/VPMS_Frontend/backend/src/main/java/com/heg/VpmsApplication.java#L7-L11) does not extend this class, and `backend/src/main/webapp/` contains no `web.xml`.
* **Why It Matters**:
  Tomcat may unpack `vpms.war` into its `webapps/` folder, but fail to instantiate the Spring application context, leaving the external Tomcat deployment non-functional despite the CI job succeeding.
* **Information Needed to Resolve**:
  Log output from `E:\apache-tomcat-10.1.28\logs\catalina.*.log` confirming whether the application context successfully starts on port 8090.
* **Diagrams Affected**:
  - [03-deployment-architecture.md](./03-deployment-architecture.md) (External Tomcat container state).

---

## 4. Windows File Storage Path in Linux Alpine Container

* **The Discrepancy**:
  - In [`backend/src/main/resources/application.properties#L52`](file:///D:/frontend%20pipeline/VPMS_Frontend/backend/src/main/resources/application.properties#L52), the upload path is defined as:
    ```properties
    app.upload.dir=D:/cvps/uploads
    ```
  - The backend Docker container runs Alpine Linux (`eclipse-temurin:17-jdk-alpine`).
  - [`docker-compose.yml`](file:///D:/frontend%20pipeline/VPMS_Frontend/docker-compose.yml#L15-L16) only mounts `application.properties`; it does not mount any volume for uploads.
* **Why It Matters**:
  Inside the Linux container, Java creates a directory path relative to the container root (`/app/D:/cvps/uploads`). Any vehicle pass documents or images uploaded by users are saved to the container's ephemeral writable layer and will be permanently lost whenever the container is recreated during a deployment.
* **Information Needed to Resolve**:
  Decision on whether a shared Docker volume or host bind-mount should be added to `docker-compose.yml` (e.g. `/uploads`), with a corresponding environment variable override `APP_UPLOAD_DIR: /uploads`.
* **Diagrams Affected**:
  - [03-deployment-architecture.md](./03-deployment-architecture.md) (Storage and volume architecture).

---

## 5. Single-Runner Hardcoding & Lack of Agent Portability

* **The Discrepancy**:
  - [`.github/workflows/cicd.yml`](file:///D:/frontend%20pipeline/VPMS_Frontend/.github/workflows/cicd.yml) and [`.github/workflows/frontend-cicd.yml`](file:///D:/frontend%20pipeline/VPMS_Frontend/.github/workflows/frontend-cicd.yml) contain hardcoded host filesystem paths:
    - `C:\maven\apache-maven-3.9.12\...`
    - `C:\maven\settings.xml`
    - `E:\apache-tomcat-10.1.28\bin`
    - `D:\frontend pipeline\VPMS_Frontend\...`
* **Why It Matters**:
  The workflow assumes all runs take place on one specific physical machine. Registering a second runner or moving to a cloud VM will break all pipeline jobs.
* **Information Needed to Resolve**:
  Migration plan to use relative workspace paths (`${{ github.workspace }}`), environment-injected tool paths, and containerized Maven/Tomcat steps.
* **Diagrams Affected**:
  - [02-cicd-flow.md](./02-cicd-flow.md) and [03-deployment-architecture.md](./03-deployment-architecture.md) (Runner host boundaries).
