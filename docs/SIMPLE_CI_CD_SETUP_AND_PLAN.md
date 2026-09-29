# Simplified CI/CD Setup, Plan & Interview Guide

This guide provides a clean, step-by-step overview of the beginner-friendly 2-job CI/CD pipeline for **`anchitchourasia/complete-ci-cd-v1`**.

---

## 1. Project & Application Overview

- **Application**: Spring Boot 4.0.8 (Java 17) Web Application
- **Packaging**: WAR Archive (`ci-cd-demo.war`)
- **Servlet Container**: External Apache Tomcat 10.1.28
- **Target URL**: `http://localhost:8090/ci-cd-demo/test/msg`
- **Expected Response**: `Hello Test Controller` (HTTP Status `200 OK`)

---

## 2. Environment Prerequisites

| Component | Path / Location | Purpose |
| :--- | :--- | :--- |
| **Java Development Kit** | `C:\Program Files\Java\jdk-17` | Java Compiler & Runtime (Java 17) |
| **Apache Maven** | `C:\maven\apache-maven-3.9.12\...\bin\mvn.cmd` | Build, test & package tool |
| **Maven Settings** | `C:\maven\settings.xml` | Repository mirror & dependency config |
| **Apache Tomcat** | `E:\apache-tomcat-10.1.28\webapps` | Servlet container (Port 8090) |
| **Corporate Proxy** | `http://192.168.9.112:808` | Network HTTP/HTTPS proxy |
| **Memory Limit** | `MAVEN_OPTS: '-Xmx512m -Xms256m'` | Caps JVM heap memory to prevent Windows pagefile errors |

---

## 3. How to Start the Self-Hosted Runner

1. Open a **Command Prompt (CMD)** window on your Windows PC.
2. Navigate to your runner directory:
   ```cmd
   cd /d E:\actions-runner-windows
   ```
3. Run the interactive runner script:
   ```cmd
   run.cmd
   ```
4. **Important**: Keep this CMD window open while running workflow builds.

---

## 4. Pipeline Structure (2 Jobs)

The workflow file `.github/workflows/cicd.yml` contains **2 simple jobs**:

```
[Push to main / m1]
        │
        ▼
┌─────────────────────────┐
│ 1. Build & Test         │ ──> Compiles code, runs unit tests, uploads ci-cd-demo.war
└─────────────────────────┘
        │
        ▼ (Push ONLY)
┌─────────────────────────┐
│ 2. Deploy to Tomcat     │ ──> Downloads WAR artifact, copies to Tomcat webapps/
└─────────────────────────┘
```

### Job 1: `build-and-test`
- **Triggers**: Runs on both `push` and `pull_request`.
- **Action**:
  - Clones source code (`actions/checkout@v4`).
  - Compiles Java 17 code and runs JUnit tests (`mvn clean verify`).
  - Uploads `target/ci-cd-demo.war` as a workflow artifact named `ci-cd-demo-war`.

### Job 2: `deploy`
- **Triggers**: Runs **only** on `push` events (needs `build-and-test`).
- **Action**:
  - Downloads `ci-cd-demo-war` into `./deploy-artifact/`.
  - Copies `ci-cd-demo.war` directly to `E:\apache-tomcat-10.1.28\webapps`.
  - Displays the deployed application URL (`http://localhost:8090/ci-cd-demo/test/msg`).

---

## 5. Technical Interview Questions & Answers

### Q1: What is the purpose of `MAVEN_OPTS: '-Xmx512m -Xms256m'`?
**Answer**: `MAVEN_OPTS` passes JVM heap memory limits to Maven. `-Xms256m` sets the starting memory (256 MB) and `-Xmx512m` caps the maximum memory (512 MB). On Windows, 64-bit Java 17 defaults to requesting large virtual memory blocks, which can cause Windows paging file errors (`errno 1455`). Capping heap usage at 512 MB prevents host memory exhaustion.

### Q2: Why is the deployment job divided into `build-and-test` and `deploy`?
**Answer**: Separating build/test from deployment adheres to CI/CD best practices. The `build-and-test` job validates code changes on both Pull Requests and commits. The `deploy` job runs only after build/tests pass and is restricted to `push` events to avoid deploying unmerged pull request code.

### Q3: How does Tomcat auto-deploy the updated WAR file?
**Answer**: Tomcat has a background `HostConfig` scanner thread running continuously. When `Copy-Item` overwrites `ci-cd-demo.war` in `E:\apache-tomcat-10.1.28\webapps`, Tomcat detects the file change, stops the previous context `/ci-cd-demo`, unpacks the new `.war` file, and binds the updated application automatically.

### Q4: Why are corporate proxy environment variables defined in the pipeline?
**Answer**: The corporate network routes outbound internet traffic through proxy `http://192.168.9.112:808`. Setting `HTTP_PROXY` and `HTTPS_PROXY` in the workflow environment enables GitHub Actions and Maven to download external dependencies, while `NO_PROXY` excludes local addresses like `localhost` and `127.0.0.1`.

---

## 6. 30-Second Summary for Your Lead / Sir

> *"We designed a clean, beginner-friendly 2-job CI/CD pipeline for our Spring Boot 17 WAR application using a self-hosted Windows GitHub Actions runner.
>
> Job 1 (`build-and-test`) compiles the code, executes JUnit tests with capped JVM memory (`-Xmx512m`), and packages the WAR artifact.
>
> Job 2 (`deploy`) runs on push events, downloads the built WAR artifact, and copies it to local Tomcat (`E:\apache-tomcat-10.1.28\webapps`). Tomcat automatically deploys the application live at `http://localhost:8090/ci-cd-demo/test/msg`."*
