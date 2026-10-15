# VPMS Architecture & CI/CD Documentation

This directory contains technical documentation for the **Vehicle Pass Management System (VPMS)** repository, capturing the current Continuous Integration and Continuous Deployment (CI/CD) pipelines, container topology, host-level deployments, and runtime request flows.

All documentation in this directory is derived directly from the active codebase on branch `v1` and verified local configuration files on the self-hosted Windows runner.

---

## Document Index & Reading Order

For the most effective understanding, read the documents in the following sequence:

| Order | Document | Purpose & Target Audience |
| :--- | :--- | :--- |
| **01** | [01-file-inventory.md](./01-file-inventory.md) | Comprehensive audit of all inspected files, external dependencies, missing files, and sanitized configuration sources. |
| **02** | [02-cicd-flow.md](./02-cicd-flow.md) | End-to-end pipeline execution flow: triggers, Maven build, Nexus publish, Tomcat deployment, Docker image creation, and frontend deployment. |
| **03** | [03-deployment-architecture.md](./03-deployment-architecture.md) | Complete topology diagram covering GitHub, Windows Runner host, Docker network, Tomcat, Nexus, Oracle DB, and corporate proxies. |
| **04** | [04-runtime-request-flow.md](./04-runtime-request-flow.md) | In-depth request flows: static SPA delivery via Nginx, direct browser-to-backend API routing, CORS preflights, and authentication. |
| **05** | [05-configuration-and-ports.md](./05-configuration-and-ports.md) | Master tables of environment variables, network ports, volume mounts, context paths, and build-time vs runtime boundaries. |
| **06** | [06-operations-and-troubleshooting.md](./06-operations-and-troubleshooting.md) | Safe read-only operational commands, log inspection commands, and diagnosis guides for common operational failures. |
| **07** | [07-gaps-and-improvements.md](./07-gaps-and-improvements.md) | Objective gap analysis of test skipping, health checks, secrets management, and image tagging, categorized by priority. |
| **08** | [08-interview-explanation.md](./08-interview-explanation.md) | 60-second elevator pitch, 5-minute technical deep-dive, architectural role justifications, and anticipated interview Q&A. |
| **09** | [09-open-questions.md](./09-open-questions.md) | Open technical discrepancies (port mismatches, endpoint discrepancies, path anomalies) affecting runtime diagrams. |

---

## Project Overview

**VPMS (Vehicle Pass Management System)** is an enterprise application designed for HEG to regulate, issue, approve, track, and report on vehicle entry and gate passes.

The system is organized into two primary tiers:
1. **Backend (`backend/`)**: A Java 17 Spring Boot 3.3.2 application packaged as a WAR file (`vpms.war`). It interacts with an enterprise Oracle Database using Spring Data JPA and Hibernate, exposes REST APIs for authentication, reports, pass management, and document handling, and includes JWT validation and API key security.
2. **Frontend (`Frontend/`)**: An Angular 21 Single Page Application (SPA) configured with standalone components, Angular Signals, and client-side routing. It is compiled to static assets and served through an Nginx Alpine container.

The deployment infrastructure is automated through GitHub Actions executing on a self-hosted **Windows runner**, simultaneously maintaining two backend hosting models:
- A standalone, host-level **Apache Tomcat 10.1.28** servlet container.
- An isolated containerized environment managed via **Docker Compose** on a dedicated bridge network.

---

## Scope & Evidence Standards

To maintain absolute architectural integrity, all statements in this documentation adhere to strict evidentiary criteria:

* **[Confirmed from source/configuration]**: Verified directly by inspecting version-controlled files in the repository (e.g., workflows, Dockerfiles, Java classes, TypeScript services, `pom.xml`, `package.json`).
* **[Observed from read-only runtime check]**: Verified by inspecting live local runtime processes and files on the host system without modifying state (e.g., `docker compose ps`, `server.xml`, `setenv.bat`, `settings.xml`).
* **[Inferred but not verified]**: Architecturally probable based on conventions or partial references, but lacking definitive proof in the local workspace.
* **[Not configured]**: Capabilities, settings, or safeguards that do not exist in the code or configuration (e.g., unit tests in CI, frontend health check endpoints).
* **[File unavailable]**: Referenced external files that could not be located or inspected.

---

## Glossary of Core Terms

* **Workflow (`.github/workflows/*.yml`)**: An automated GitHub Actions procedure configured as a YAML file that defines triggers, runner requirements, environment variables, and sequential or parallel jobs.
* **Job**: A set of steps inside a workflow that executes on a specified runner. Jobs run in parallel by default unless configured with `needs`.
* **Runner**: The host system running the GitHub Actions runner agent. In this repository, the runner is a **self-hosted Windows machine** with access to local drive paths (`C:`, `D:`, `E:`).
* **Artifact**: A binary or collection of files produced during a workflow job (such as `vpms.war`) uploaded to GitHub storage via `actions/upload-artifact` so subsequent jobs or human operators can download them.
* **Image**: An immutable, read-only template built from a Dockerfile containing the OS layer, runtime libraries, and compiled application binaries.
* **Container**: A running, stateful instance of an image executed isolated inside the Docker Engine.
* **Service**: A logical container definition inside `docker-compose.yml` specifying the image, ports, networks, environment variables, and volumes to run.
* **Network**: An isolated software bridge inside Docker allowing containers on the same network to resolve each other by container/service name.
* **Bind Mount**: A direct mapping of a file or directory from the host filesystem into a specific path inside a container.
* **Host Port**: The network port opened on the host machine OS that receives external traffic and forwards it into a container or process.
* **Container Port**: The internal network port that a process inside a container listens on (e.g., port 80 for Nginx, port 3031 for Spring Boot).
