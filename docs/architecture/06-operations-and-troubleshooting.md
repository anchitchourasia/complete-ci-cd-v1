# 06 — Operations & Troubleshooting Guide

This guide provides operational runbooks, diagnostic commands, and triage procedures tailored to the **VPMS** production environment, covering Docker Compose, local Apache Tomcat 10.1.28, Nginx, and Spring Boot.

---

## 1. Safe Read-Only Diagnostics

The following commands inspect system state without altering running containers, configurations, or processes:

### A. View VPMS Containers (Active & Stopped)
```powershell
# List containers associated with the 'vpms' Compose project
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms ps -a

# View all containers with VPMS filter, formatted cleanly
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' ps -a `
  --filter "name=vpms" `
  --format "table {{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"
```

### B. View Service Logs
```powershell
# Follow backend Spring Boot logs in real-time
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' logs -f --tail 200 vpms-container

# Inspect frontend Nginx access and error logs
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' logs --tail 100 vpms-frontend

# View Compose logs with timestamps for both services
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms logs -t --tail 50
```

### C. Safely Inspect Volume Mounts
```powershell
# Confirm that application.properties is mounted correctly without exposing secrets
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' inspect vpms-container `
  --format "{{range .Mounts}}Source: {{.Source}} -> Target: {{.Destination}} (RW: {{.RW}}){{println}}{{end}}"
```

### D. Verify Published Network Ports & Listeners
```powershell
# Inspect active TCP listeners on ports 4200 (frontend), 8092 (backend), 8090 (Tomcat), and 8081 (Nexus)
Get-NetTCPConnection -LocalPort 4200, 8092, 8090, 8081 -State Listen `
  | Select-Object LocalAddress, LocalPort, OwningProcess, State

# Verify Docker port mapping using Docker CLI
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' port vpms-container
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' port vpms-frontend
```

### E. Check Git Status & Ignored Files
```powershell
# Check tracked status of critical environment files
git check-ignore -v Frontend/src/environments/environment.ts
git check-ignore -v Frontend/src/environments/environment.example.ts

# Inspect working tree status including ignored items
git status --ignored --short
```

---

## 2. State-Changing Administrative Commands

> [!WARNING]
> The following commands modify container state, restart services, or destroy data buffers. **Do not execute these in a production cluster during peak operational hours without prior notification.**

### A. Controlled Service Restart
```powershell
# Restart backend without rebuilding (reloads mounted application.properties)
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms restart backend

# Restart frontend container
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms restart frontend
```

### B. Rebuild & Recreate Containers
```powershell
# Recreate containers with zero dependency caching
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms up -d --no-deps --force-recreate backend
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms up -d --no-deps --force-recreate frontend
```

### C. Teardown / Cleanup
```powershell
# Stop and remove containers and bridge network
& 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' compose -p vpms down
```

---

## 3. Targeted Triage Runbooks

### Runbook 1: Diagnosing Missing `pom.xml` Failure
* **Symptom**: Pipeline Job 1 (`build-and-test`) fails immediately with `The goal you specified requires a project to execute but there is no POM in this directory`.
* **Root Cause**: The step working directory is incorrectly set, or `actions/checkout@v4` checked out into an unexpected path.
* **Triage Steps**:
  1. Verify the `working-directory` directive in [`.github/workflows/cicd.yml#L89`](file:///D:/frontend%20pipeline/VPMS_Frontend/.github/workflows/cicd.yml#L89) is set to `backend`.
  2. Confirm `backend/pom.xml` exists in the branch being deployed:
     ```powershell
     Test-Path "$env:GITHUB_WORKSPACE\backend\pom.xml"
     ```

---

### Runbook 2: Diagnosing Missing Angular `environment.ts`
* **Symptom**: Angular build fails with `Error: Cannot find module '../environments/environment'` or Job 4 fails at step `Copy local Angular environment configuration`.
* **Root Cause**: `environment.ts` is gitignored ([`Frontend/.gitignore#L47`](file:///D:/frontend%20pipeline/VPMS_Frontend/Frontend/.gitignore#L47)). The workflow expects it at host path `D:\frontend pipeline\VPMS_Frontend\Frontend\src\environments\environment.ts`.
* **Triage Steps**:
  1. Confirm the template exists in Git:
     ```powershell
     Test-Path "Frontend\src\environments\environment.example.ts"
     ```
  2. Verify the host configuration file is in place on the runner:
     ```powershell
     Test-Path "D:\frontend pipeline\VPMS_Frontend\Frontend\src\environments\environment.ts"
     ```
  3. If missing, copy `environment.example.ts` to `environment.ts` and populate sanitized values:
     ```powershell
     Copy-Item "Frontend\src\environments\environment.example.ts" "Frontend\src\environments\environment.ts"
     ```

---

### Runbook 3: Diagnosing Nginx JavaScript / `text/html` (MIME Mismatch) Errors
* **Symptom**: Browser loads `index.html`, but console logs `Failed to load module script: Expected a JavaScript module script but the server responded with a MIME type of "text/html"`.
* **Root Cause**: Nginx's `try_files` rule fallbacks to `index.html` whenever an asset is missing. This occurs if `--base-href` was omitted during `ng build` or the file was copied to an unexpected directory in Nginx.
* **Triage Steps**:
  1. Check the Nginx configuration in [`Frontend/nginx.conf#L16-L18`](file:///D:/frontend%20pipeline/VPMS_Frontend/Frontend/nginx.conf#L16-L18):
     ```nginx
     location /vpms-ui/ {
         try_files $uri $uri/ /vpms-ui/index.html;
     }
     ```
  2. Inspect the compiled directory inside the running container:
     ```powershell
     & 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' exec vpms-frontend ls -la /usr/share/nginx/html/vpms-ui/
     ```
  3. Confirm the browser URL matches `http://<HOST>:4200/vpms-ui/` including the trailing slash.

---

### Runbook 4: Diagnosing Missing Datasource / DB Connection Failures
* **Symptom**: Container starts, but exits after 10–15 seconds. `docker logs vpms-container` displays `HikariPool-1 - Exception during pool initialization: IO Error: The Network Adapter could not establish the connection`.
* **Root Cause**:
  1. Oracle Database at `DATABASE_HOST:1521` is offline or unreachable from the container network.
  2. Bind-mounted `application.properties` path is invalid or empty.
* **Triage Steps**:
  1. Test TCP reachability from the host to Oracle:
     ```powershell
     Test-NetConnection -ComputerName 192.168.9.21 -Port 1521
     ```
  2. Verify the mount is populated inside the container:
     ```powershell
     & 'C:\Program Files\Docker\Docker\resources\bin\docker.exe' exec vpms-container cat /config/application.properties | Select-String "spring.datasource.url"
     ```

---

### Runbook 5: Diagnosing External Tomcat Startup Failure
* **Symptom**: Pipeline Job 2 (`deploy`) throws `Tomcat did not start on port 8090 within 45 seconds`.
* **Root Cause**: Port 8090 is in use by another process, Java path is invalid, or `startup.bat` encountered a syntax error.
* **Triage Steps**:
  1. Check what is holding port 8090:
     ```powershell
     Get-NetTCPConnection -LocalPort 8090 -ErrorAction SilentlyContinue | Format-Table -Property LocalPort, OwningProcess, State
     ```
  2. Review Tomcat catalina logs:
     ```powershell
     Get-Content 'E:\apache-tomcat-10.1.28\logs\catalina.*.log' -Tail 50
     ```
  3. Verify `setenv.bat` syntax:
     ```powershell
     Get-Content 'E:\apache-tomcat-10.1.28\bin\setenv.bat'
     ```
