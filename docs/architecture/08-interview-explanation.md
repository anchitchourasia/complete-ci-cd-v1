# 08 — Interview Guide & Architectural Walkthrough

This document prepares you to explain the **VPMS** architecture in a technical interview. It explains each tool's distinct responsibility, walks through the pipeline in plain English, and provides direct answers to common questions.

---

## 1. The 60-Second Elevator Pitch

> "VPMS (Vehicle Pass Management System) is an enterprise full-stack web application built for HEG to regulate and issue vehicle gate passes. The backend is a Java 17 Spring Boot service connected to an Oracle Database, and the frontend is an Angular 21 Single Page Application.
> 
> I automated the CI/CD pipeline using GitHub Actions on a self-hosted Windows runner. When code is pushed, the pipeline compiles the Spring Boot WAR using Maven, publishes it to a local Nexus repository, and deploys it to both a standalone Apache Tomcat server and an isolated Docker container managed via Docker Compose. Simultaneously, the frontend workflow builds the Angular app with Node 24 and packages it into an Nginx Alpine container.
> 
> In this setup, Nginx serves the compiled Angular static files to the client's browser, and the browser communicates directly with the published Spring Boot backend API over HTTP, with security enforced through a combination of JWT validation and API keys."

---

## 2. The 3-to-5 Minute Technical Deep-Dive

"If we look under the hood at how the pipeline and runtime operate together, there are several distinct stages:

### Phase 1: Build & Artifact Storage
The workflow begins on our self-hosted Windows runner. We use Maven to compile and package the Java backend into an executable WAR named `vpms.war`. We run `mvn clean deploy`, which publishes the snapshot WAR directly into our local Sonatype Nexus repository so other internal services or teams can consume the tested build. Maven also uploads the WAR file to GitHub workflow storage as an artifact so downstream jobs can consume the exact same binary without recompiling.

### Phase 2: Dual Deployment (Tomcat & Docker)
Our architecture currently maintains two deployment targets:
1. **Host-Level Apache Tomcat**: The pipeline downloads the WAR artifact, checks if port 8090 is active, starts Tomcat via PowerShell if needed, and copies `vpms.war` into Tomcat's `webapps/` folder. Tomcat dynamically explodes and serves the application under the context path `/vpms`.
2. **Containerized Spring Boot**: The next job downloads that same WAR, builds a Docker image (`vpms:1.0`) on top of an Eclipse Temurin Java 17 Alpine base, removes the old container, and spins up `vpms-container` using Docker Compose. We bind-mount an external `application.properties` file into the container so database credentials and connection pool settings can be updated without rebuilding the image.

### Phase 3: Frontend Build & Nginx Serving
Once the backend is healthy, the pipeline triggers a reusable frontend workflow. Angular 21 requires build-time configuration, so the workflow copies our local `environment.ts` into the workspace, installs dependencies via `npm ci`, and runs an optimized production build. The compiled static HTML, CSS, and JavaScript files are copied into an Nginx Alpine image (`vpms-frontend:1.0`). We deploy it using Docker Compose, publishing port 4200 on the host to port 80 in the container.

### Phase 4: Runtime Request Architecture
At runtime, an end-user navigates to `http://<HOST>:4200/vpms-ui/`. The Nginx container simply delivers the static Angular files. Once loaded in the user's browser, Angular's HTTP client sends REST API requests directly to the backend published at host port 8092. The Spring Boot backend handles CORS preflight requests, inspects incoming `x-api-key` headers and JWT tokens, executes business logic, and queries our enterprise Oracle database."

---

## 3. Tool Responsibility Matrix: Why We Use Each Tool

In an interview, candidates often struggle to explain why multiple DevOps tools are used together. Use this matrix to explain why each tool has a non-overlapping role:

| Tool | Core Responsibility in VPMS | Why Another Tool Cannot Replace It |
| :--- | :--- | :--- |
| **GitHub Actions** | **Orchestrator**: Detects Git events, evaluates conditionals, enforces execution order, and triggers commands. | Docker and Maven execute tasks, but they cannot listen to Git pushes or coordinate multi-stage workflows across tools. |
| **Apache Maven** | **Build Tool & Dependency Manager**: Resolves Java libraries from remote repositories, compiles Java code, and packages the WAR. | Docker only packages what Maven creates; Docker does not compile Java source code or manage Java dependency trees in this pipeline. |
| **Sonatype Nexus** | **Binary Repository**: Long-term versioned storage for compiled artifacts (`vpms.war`). | GitHub Actions artifacts expire after a retention window (typically 90 days); Nexus provides permanent enterprise artifact governance. |
| **Docker (Dockerfile)** | **Packaging**: Bundles the application binary with its specific operating system, Java runtime, and dependencies into an immutable image. | Solves the 'works on my machine' problem by standardizing runtime environments across systems. |
| **Docker Compose** | **Multi-Container Coordinator**: Manages port bindings, volume bind mounts, bridge networks, and service lifecycle via declarative YAML. | Dockerfiles only build single images; Compose manages how multiple running containers relate to the host and each other. |
| **Nginx** | **High-Performance Web Server**: Serves compiled static Angular assets with gzip compression, caching headers, and SPA fallback routing (`try_files`). | Angular is client-side code; it does not contain a built-in web server for production. Node.js is unnecessary overhead for serving static files. |
| **External Tomcat** | **Legacy / Standalone Servlet Container**: Runs directly on the Windows host on port 8090, serving legacy enterprise integrations. | Maintained alongside Docker to support environments where container runtimes are not yet standard. |

---

## 4. Tough Interview Questions & Clear Answers

### Q1: Why do you have both Tomcat and Docker in the same pipeline?
**Answer**:
"In enterprise migrations, you rarely switch from standalone application servers to containers overnight. In our project, external Apache Tomcat (port 8090) represents the established hosting model, while Docker Compose (port 8092) represents the modern containerized environment. By deploying the identical tested WAR artifact to both targets in the same pipeline, we ensure parity between environments and allow teams to validate containerized behavior before decommissioning standalone Tomcat."

### Q2: Why does the frontend build need a PowerShell copy step for `environment.ts`?
**Answer**:
"`environment.ts` contains environment-specific settings like the backend API URL and API keys, so it is deliberately excluded from Git via `.gitignore` to prevent leaking secrets. When the GitHub Actions runner performs a clean checkout, `environment.ts` is missing. The workflow copies the verified local configuration file from the host runner into the checked-out workspace immediately before running `npm run build` so the Angular compiler can bake the URLs into the JavaScript bundles."

### Q3: How does the Angular frontend communicate with Spring Boot across Docker containers?
**Answer**:
"Common misconception: people assume Nginx proxies API calls to Spring Boot over the Docker bridge network. In our current code, **it does not**. Nginx only serves static files. The Angular application runs entirely inside the end-user's web browser. Therefore, all HTTP requests originate from the user's browser and connect directly to the published host port of the Spring Boot container (port 8092). Because the requests cross different ports (4200 to 8092), Spring Boot handles CORS preflight `OPTIONS` requests using `SecurityConfig` and `CorsConfig`."

### Q4: What is the difference between Nexus and Docker Registry?
**Answer**:
"Nexus is configured here as a **Maven repository** that stores raw Java binaries (`.war` / `.jar` files) categorized by Maven coordinates (`groupId:artifactId:version`). Docker Registry stores container **images** containing an entire OS filesystem layer and runtime environment. Nexus stores the compiled application artifact; Docker Registry stores the runnable container image."

---

## 5. Pipeline Integrity: What Is Verified vs. What Requires Manual Testing

Being honest about pipeline limitations demonstrates senior engineering maturity. Here is the realistic assessment:

### What the Pipeline AUTOMATICALLY Proves:
1. Java source code compiles successfully without syntax errors under JDK 17.
2. The WAR package deploys successfully to Nexus and external Tomcat.
3. The Docker images build without syntax errors in the Dockerfiles.
4. The backend container enters a `running` status and does not exit immediately.
5. Angular compiles successfully with zero TypeScript or bundling errors.
6. The Nginx web server returns an HTTP 200 OK status on `http://127.0.0.1:4200/vpms-ui/`.

### What STILL REQUIRES Manual or Integration Testing:
1. **Unit & Integration Logic**: Tests are currently skipped (`-DskipTests`), meaning business logic and database mapping errors are not validated automatically.
2. **Database Connectivity**: The pipeline verifies container running status, not whether HikariCP successfully connected to Oracle.
3. **Frontend-to-Backend Integration**: CI checks Nginx availability on port 4200, but does not test whether Angular can authenticate with backend port 8092.
4. **File Storage**: The file upload path `D:/cvps/uploads` inside the Linux container must be manually tested to confirm write permissions.
