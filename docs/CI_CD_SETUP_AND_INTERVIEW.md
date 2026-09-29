# Complete CI/CD Pipeline Setup & Technical Interview Guide

This document provides an end-to-end technical breakdown of the CI/CD pipeline for **`anchitchourasia/complete-ci-cd-v1`**, covering runner configuration, workflow architecture, memory tuning, deployment mechanics, limitations, troubleshooting, and interview Q&As.

---

## 1. Runner Setup: Registration vs. Execution

### A. One-Time Registration (Already Completed)
Registration connects the physical Windows machine to the GitHub repository. It requires downloading the GitHub Actions Runner package and executing `config.cmd` using a **one-time registration token** generated from GitHub Repository Settings (`Settings -> Actions -> Runners -> New self-hosted runner`).

> ⚠️ **Security Note**: Registration tokens are sensitive and short-lived. Never commit registration tokens or runner credentials into version control.

### B. Starting an Already Registered Runner (Daily Operation)
Once registered, the runner does not need to be re-registered. To start listening for incoming workflow jobs:

```cmd
cd /d E:\actions-runner-windows
run.cmd
```

#### Key Considerations:
- **Machine-Specific Path**: `E:\actions-runner-windows` is specific to this host PC setup.
- **Foreground Process**: Running `run.cmd` launches the runner in an interactive CMD window. **This CMD window must remain open** for the runner to remain active and process jobs. If the window is closed, the runner goes **Offline**.
- **Windows Service Alternative**: For production, runners can be installed as a background Windows Service (`cmds/service.cmd install` & `cmds/service.cmd start`), enabling automatic start on system boot without an open console window.

### C. Checking Runner Status & Labels in GitHub
To verify if your runner is active:
1. Navigate to **GitHub Repository -> Settings -> Actions -> Runners**.
2. **Status**:
   - 🟢 **Idle**: Runner is online, connected, and waiting for jobs.
   - 🔵 **Active**: Runner is currently executing a pipeline job.
   - 🔴 **Offline**: `run.cmd` is not running or the machine lost network connectivity.
3. **Labels**: Ensure the runner displays labels `self-hosted` and `Windows`. Workflow jobs specify `runs-on: [self-hosted, Windows]`.

---

## 2. Environment Prerequisites & Local Tooling

The pipeline executes commands against pre-installed local tooling on the self-hosted runner host:

| Tool | Path / Location | Purpose |
| :--- | :--- | :--- |
| **JDK 17** | `C:\Program Files\Java\jdk-17` | Java Compiler & Runtime |
| **Maven 3.9.12** | `C:\maven\apache-maven-3.9.12\...\bin\mvn.cmd` | Build & Test Tool |
| **Maven Settings** | `C:\maven\settings.xml` | Repository & Mirror Config |
| **Apache Tomcat 10.1** | `E:\apache-tomcat-10.1.28` | Application Server (Port 8090) |
| **Corporate Proxy** | `http://192.168.9.112:808` | HTTP/HTTPS Proxy |

### Proxy Environment Variables
Corporate network proxy settings are declared in `.github/workflows/cicd.yml` under `env:`:
- `HTTP_PROXY`: `http://192.168.9.112:808`
- `HTTPS_PROXY`: `http://192.168.9.112:808`
- `NO_PROXY`: `192.168.8.25,localhost,127.0.0.1`

---

## 3. Detailed Workflow Breakdown (`.github/workflows/cicd.yml`)

The pipeline comprises **3 sequential jobs** executed on a self-hosted Windows runner.

```
[Trigger: push/PR to main]
        │
        ▼
┌─────────────────────────┐
│ 1. build-and-test       │ ──> Compiles, runs unit tests, uploads target/ci-cd-demo.war
└─────────────────────────┘
        │
        ▼
┌─────────────────────────┐
│ 2. code-quality         │ ──> Runs `mvn verify -DskipTests` (Packaging check)
└─────────────────────────┘
        │
        ▼ (Push to main ONLY)
┌─────────────────────────┐
│ 3. deploy               │ ──> Downloads WAR artifact, copies to Tomcat webapps/
└─────────────────────────┘
```

### Job 1: `build-and-test`
- **Triggers**: Executed on both `push` and `pull_request` to `main`.
- **Pre-check**: Validates existence of `java.exe`, `mvn.cmd`, and `settings.xml`.
- **Build Command**:
  ```powershell
  & $env:MAVEN_BIN -s $env:SETTINGS_XML clean verify --no-transfer-progress
  ```
- **Tests Execution**: Surefire plugin executes `com.heg.CompleteCiCdV1ApplicationTests` with JVM heap limits `-Xmx512m -Xms256m`.
- **Artifact Upload**: `actions/upload-artifact@v4` uploads `target/ci-cd-demo.war` as artifact name `ci-cd-demo-war` (retention: 7 days).

### Job 2: `code-quality`
- **Triggers**: Executed on both `push` and `pull_request` to `main` (`needs: build-and-test`).
- **Command**:
  ```powershell
  & $env:MAVEN_BIN -s $env:SETTINGS_XML verify -DskipTests --no-transfer-progress
  ```
- **Nature of Job**: Re-runs Maven verification with unit tests explicitly skipped (`-DskipTests`). It validates packaging structure. **Note**: This job does NOT integrate SonarQube or external static analysis tools.

### Job 3: `deploy`
- **Triggers**: Conditioned via `if: github.event_name == 'push' && github.ref == 'refs/heads/main'` (`needs: [build-and-test, code-quality]`). Skipped during Pull Requests.
- **Concurrency**: `concurrency: group: local-tomcat-deploy` ensures only one deployment process runs at a time.
- **Artifact Download**: Downloads `ci-cd-demo-war` artifact to `./deploy-artifact/`.
- **Secret Fallback Behavior**:
  ```powershell
  $targetPath = $env:TOMCAT_WEBAPPS_PATH
  if ([string]::IsNullOrWhiteSpace($targetPath)) {
    $targetPath = 'E:\apache-tomcat-10.1.28\webapps'
    Write-Host "TOMCAT_WEBAPPS_PATH secret not set. Using local default: $targetPath"
  }
  ```
  > 📌 **Crucial Behavior**: If the GitHub Secret `TOMCAT_WEBAPPS_PATH` is missing or empty, the workflow **does NOT fail or skip deployment**. It logs a warning and falls back to `E:\apache-tomcat-10.1.28\webapps`.
- **Copy Step**: Copies `deploy-artifact\ci-cd-demo.war` directly to `$targetPath\ci-cd-demo.war` using `Copy-Item -Force`.

---

## 4. Deployment Mechanics & Endpoint Verification

### Context Path & Endpoint Derivation
1. `pom.xml` defines `<finalName>ci-cd-demo</finalName>`, producing `ci-cd-demo.war`.
2. Apache Tomcat automatically detects `ci-cd-demo.war` in `webapps/`, unpacks it into directory `webapps/ci-cd-demo/`, and registers context path `/ci-cd-demo`.
3. Source file `src/main/java/com/heg/controller/MainTestController.java` defines `@RequestMapping("/test")` and `@GetMapping("/msg")`.
4. Tomcat HTTP Connector port is configured as `8090` in `E:\apache-tomcat-10.1.28\conf\server.xml`.

### Verification Summary:
- **Full URL**: `http://localhost:8090/ci-cd-demo/test/msg`
- **Response**: `Hello Test Controller`
- **HTTP Status**: `200 OK`

### What Workflow Proves vs. Manual Test Required
- **Verified by Workflow**: Successful compilation, unit test execution, WAR packaging, artifact transfer, and file copy into `webapps/`.
- **Not Checked by Workflow**: The workflow contains **no automated post-deployment health check**. It prints `"Confirm Tomcat starts the app in its logs."` but does not perform HTTP requests to verify that Tomcat booted the application context successfully. Manual testing or an added HTTP health-check step is required to confirm live response.

---

## 5. Pipeline Limitations & Improvement Opportunities

1. **Redundant Build Work**: The `code-quality` job executes `mvn verify -DskipTests`, which recompiles and repackages the code from scratch rather than running static code analysis tools (Checkstyle/SpotBugs/SonarQube).
2. **Hardcoded Machine Paths**: Absolute paths (`C:\maven\...`, `C:\Program Files\Java\jdk-17`, `E:\apache-tomcat-10.1.28`) are hardcoded in environment variables, tying the workflow to a specific host machine layout.
3. **No Automated Health Check**: The deployment step ends immediately after copying the WAR file without checking if `http://localhost:8090/ci-cd-demo/test/msg` returns HTTP 200.
4. **Non-Atomic Deployment & Zero Rollback**: `Copy-Item -Force` overwrites the target WAR directly. If Tomcat fails to start the new WAR, there is no automatic rollback to the previous working WAR version.

---

## 6. Troubleshooting Guide

| Issue / Symptom | Root Cause | Resolution |
| :--- | :--- | :--- |
| **Windows Error 1455 (`errno=1455`)**<br>`os::commit_memory failed: paging file too small` | JVM forked by Surefire requested excessive virtual memory beyond host pagefile limits. | Configured Surefire plugin `<argLine>-Xmx512m -Xms256m</argLine>` in `pom.xml` and added `MAVEN_OPTS: '-Xmx512m -Xms256m'` in workflow `env:`. |
| **Runner Status Offline on GitHub** | The interactive CMD window running `run.cmd` was closed or host lost network connectivity. | Open CMD, navigate to `cd /d E:\actions-runner-windows`, and execute `run.cmd`. |
| **HTTP 404 on `http://localhost:8080/ci-cd-demo`** | Request sent to default Tomcat port 8080, but local Tomcat is configured on port 8090 in `server.xml`. | Use `http://localhost:8090/ci-cd-demo/test/msg`. |
| **`git commit` returns "nothing added to commit"** | Modified files were not staged into Git index before committing. | Run `git add .` (or specify files) prior to executing `git commit -m "..."`. |
| **Deployment Step Error: Target directory not found** | The default path `E:\apache-tomcat-10.1.28\webapps` does not exist on the runner machine. | Ensure Tomcat installation path exists or set `TOMCAT_WEBAPPS_PATH` secret in GitHub Repository Settings. |

---

## 7. Commands: Verified Runtime vs. Example Setup

### Verified Runtime Commands (Executed & Confirmed in Environment)
- **Maven Build & Test**:
  `C:\maven\apache-maven-3.9.12\...\bin\mvn.cmd -s C:\maven\settings.xml clean verify --no-transfer-progress`
- **Foreground Runner Start**:
  `cd /d E:\actions-runner-windows` then `run.cmd`
- **HTTP Endpoint Verification**:
  `Invoke-WebRequest -Uri "http://localhost:8090/ci-cd-demo/test/msg" -UseBasicParsing`

### Example Setup Commands (Administrative Setup)
- **Runner Registration Example**:
  `.\config.cmd --url https://github.com/anchitchourasia/complete-ci-cd-v1 --token <REGISTRATION_TOKEN>`
- **Windows Service Installation Example**:
  `.\cmds\service.cmd install` then `.\cmds\service.cmd start`

---

## 8. Technical Interview Questions & Answers

### Q1: Can you explain the end-to-end flow of this CI/CD pipeline?
**Answer**: The pipeline is triggered on push and pull requests to the `main` branch. It runs on a self-hosted Windows runner across three jobs: `build-and-test` compiles Java 17 source code, runs JUnit 5 unit tests with Maven Surefire, and uploads `ci-cd-demo.war` as an artifact. `code-quality` verifies packaging integrity. On push to `main`, the `deploy` job downloads the WAR artifact and copies it to Tomcat's `webapps` folder (`E:\apache-tomcat-10.1.28\webapps`), where Tomcat auto-deploys it under context path `/ci-cd-demo`.

### Q2: How does a self-hosted GitHub Actions runner work on Windows?
**Answer**: A self-hosted runner is a application daemon installed on a Windows machine that polls GitHub for queued workflow jobs matching its labels (`[self-hosted, Windows]`). When a job is received, it executes the defined PowerShell steps within the host environment, utilizing local JDK, Maven, and file system resources.

### Q3: What caused the Windows DOS Error 1455 (`errno=1455`) during test execution and how was it solved?
**Answer**: DOS Error 1455 occurs when Windows cannot allocate sufficient paging file (virtual memory) space when a process attempts to commit memory. During `mvn clean verify`, Maven Surefire forked a new JVM process for `@SpringBootTest` without memory restrictions, exceeding the OS pagefile limit. The fix was to restrict heap allocation by adding `<argLine>-Xmx512m -Xms256m</argLine>` to `maven-surefire-plugin` in `pom.xml` and setting `MAVEN_OPTS: '-Xmx512m -Xms256m'` in the workflow `env:`.

### Q4: Why is Spring Boot packaged as a WAR instead of an executable JAR?
**Answer**: The application extends `SpringBootServletInitializer` and defines `<packaging>war</packaging>` in `pom.xml` with `spring-boot-starter-tomcat` set to `<scope>provided</scope>`. This configuration allows the application to be deployed into an external, standalone Apache Tomcat servlet container alongside other enterprise web applications.

### Q5: What happens if the GitHub Secret `TOMCAT_WEBAPPS_PATH` is missing?
**Answer**: The deployment PowerShell script contains fallback logic: if `$env:TOMCAT_WEBAPPS_PATH` is empty, it assigns `$targetPath = 'E:\apache-tomcat-10.1.28\webapps'` and outputs a warning. The workflow does NOT fail; it successfully deploys using the local default path.

### Q6: How does Tomcat know to deploy the app under `/ci-cd-demo`?
**Answer**: In Apache Tomcat, the default context path for an unexploded WAR file corresponds to its filename. Since `pom.xml` defines `<finalName>ci-cd-demo</finalName>`, Maven outputs `ci-cd-demo.war`. When Tomcat auto-deploys `ci-cd-demo.war` from `webapps/`, it registers the application under `http://localhost:<port>/ci-cd-demo`.

### Q7: Why is the `deploy` job skipped on Pull Requests?
**Answer**: The `deploy` job contains an explicit condition: `if: github.event_name == 'push' && github.ref == 'refs/heads/main'`. This prevents unmerged pull request code from overwriting the live Tomcat deployment.

### Q8: Does this pipeline include an automated health check after deployment?
**Answer**: No. The workflow copies the WAR file to Tomcat's `webapps` folder and outputs a log message instructing the operator to verify Tomcat logs. It does not perform an automated HTTP request to confirm Tomcat expanded the WAR or returned HTTP 200.

### Q9: What is the difference between `build-and-test` and `code-quality` in this workflow?
**Answer**: `build-and-test` compiles code, runs unit tests, and uploads the WAR artifact. `code-quality` runs `mvn verify -DskipTests` to verify packaging without re-running unit tests. It does not run SonarQube or external static analysis tools.

### Q10: How are corporate proxies handled in this workflow?
**Answer**: Proxy environment variables (`HTTP_PROXY`, `HTTPS_PROXY`, `NO_PROXY`) are declared in the workflow `env:` section. Additionally, Maven receives `-s C:\maven\settings.xml` to ensure artifact resolution passes through internal corporate mirror repositories.

### Q11: How does `concurrency` work in the deployment job?
**Answer**: The `deploy` job defines `concurrency: group: local-tomcat-deploy`. This ensures that if multiple pushes occur in rapid succession, GitHub Actions queues them sequentially so that simultaneous file copy operations do not corrupt `ci-cd-demo.war` on Tomcat.

### Q12: How would you improve this pipeline for production readiness?
**Answer**: 
1. Replace local machine paths with environment variables or runner secrets.
2. Add an automated post-deployment HTTP health check step (`Invoke-WebRequest http://localhost:8090/ci-cd-demo/test/msg`).
3. Implement automated backup and rollback steps before overwriting `ci-cd-demo.war`.
4. Integrate genuine static code analysis (SonarQube/SpotBugs) into the `code-quality` job instead of repeating Maven build goals.

---

## 9. 60-Second Overview for Lead / Interviewer

> *"We established an automated CI/CD pipeline for a Spring Boot 17 WAR application deployed to local Apache Tomcat 10.1 using a self-hosted Windows GitHub Actions runner.*
>
> *The pipeline runs across three jobs: `build-and-test` compiles the project, executes JUnit 5 tests with explicit JVM memory limits (`-Xmx512m`) to prevent OS paging memory errors, and uploads the WAR artifact. `code-quality` validates project packaging. On push to `main`, the `deploy` job safely downloads the built WAR artifact and deploys it to Tomcat's `webapps` folder using concurrency locks. We verified the live deployment at `http://localhost:8090/ci-cd-demo/test/msg`, returning HTTP 200 `Hello Test Controller`."*
