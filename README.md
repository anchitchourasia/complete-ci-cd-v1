# Complete CI/CD Pipeline v1 (Spring Boot + GitHub Actions + Local Tomcat)

A complete CI/CD setup for a Spring Boot 4.0.8 (Java 17) web application packaged as a `.war` file, built and tested via a self-hosted Windows GitHub Actions runner, and deployed automatically to a local Apache Tomcat 10.1 web server.

---

## 🚀 Application Overview

- **Framework**: Spring Boot 4.0.8 (Java 17)
- **Packaging**: WAR (`ci-cd-demo.war`)
- **Deployment Target**: Apache Tomcat 10.1 on local host
- **Deployed Endpoint**: `http://localhost:8090/ci-cd-demo/test/msg`
- **Response**: `"Hello Test Controller"`

---

## ⚙️ GitHub Actions Workflow (3 Jobs)

The pipeline is defined in [.github/workflows/cicd.yml](.github/workflows/cicd.yml):

1. **Build & Test (`build-and-test`)**:
   - Compiles source code using Java 17 and Maven 3.9.12.
   - Runs unit tests (`SpringBootTest`) with tuned JVM memory limits (`-Xmx512m -Xms256m`).
   - Packages and uploads `target/ci-cd-demo.war` as a workflow artifact (`ci-cd-demo-war`).

2. **Code Quality (`code-quality`)**:
   - Runs `mvn verify -DskipTests` to ensure module packaging integrity.
   - Depends on `build-and-test`. Runs on both `push` and `pull_request` events.

3. **Deploy (`deploy`)**:
   - Conditioned to run **only on push to `main`**.
   - Downloads `ci-cd-demo.war` from build artifacts.
   - Copies the `.war` package directly into Tomcat's `webapps` directory (`E:\apache-tomcat-10.1.28\webapps`).
   - Tomcat automatically unpacks and deploys the application under context path `/ci-cd-demo`.

---

## 📋 Prerequisites

- **Host OS**: Windows
- **Java**: OpenJDK / JDK 17 installed at `C:\Program Files\Java\jdk-17`
- **Maven**: Apache Maven 3.9.12 at `C:\maven\apache-maven-3.9.12\...` with settings at `C:\maven\settings.xml`
- **Tomcat**: Apache Tomcat 10.1.28 installed at `E:\apache-tomcat-10.1.28` (listening on port 8090)
- **GitHub Runner**: Self-hosted Windows runner configured with labels `[self-hosted, Windows]`

---

## 📖 Documentation & Interview Guide

For complete details regarding environment setup, runner registration vs. execution, proxy configurations, troubleshooting, interview questions, and a 60-second summary for your lead/sir:

👉 **[Read the Full CI/CD Setup & Interview Guide](docs/CI_CD_SETUP_AND_INTERVIEW.md)**
