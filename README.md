# Docker & Project Setup Guide

This document contains a reference for common Docker commands, service configurations, and troubleshooting steps for the project.

## Docker Commands Reference

Here is a list of essential Docker commands and their usage descriptions:

| Command | Description |
| :--- | :--- |
| `docker pull <image>` | Downloads a Docker image from a registry (like Docker Hub) to your local machine. |
| `docker push <username/image>` | Uploads a local Docker image to a registry. |
| `docker run -it -d -p <host-port>:<container-port> --name <name> <image>` | Creates and starts a new container from an image. <br> Flags: `-it` (interactive tty), `-d` (detached mode), `-p` (port mapping), `--name` (assign name). |
| `docker stop <container-id/container-name>` | Stops a running container gracefully. |
| `docker start <container-id/container-name>` | Starts a stopped container. |
| `docker rm <container-id/container-name>` | Removes a stopped container. Add `-f` to force remove a running one. |
| `docker rmi <image-id/image-name>` | Removes a Docker image from the local machine. |
| `docker ps` | Lists all currently **running** containers. |
| `docker ps -a` | Lists **all** containers, including those that are stopped. |
| `docker images` | Lists all Docker images stored locally. |
| `docker exec -it <container-name/container-id> bash` | Opens an interactive shell (bash) inside a running container. |
| `docker build -t <username/image> .` | Builds a Docker image from a `Dockerfile` in the current directory (`.`). |
| `docker logs <container-name/container-id>` | Fetches and displays the logs of a container. |
| `docker inspect <container-name/container-id>` | Returns detailed low-level information about a container or image (JSON format). |

---

## Service Setup Instructions

### 1. Create Network
Create a custom bridge network to allow containers to communicate by name.
```sh
docker network create my-network
```

### 2. Manual Container Setup
Run PostgreSQL and pgAdmin manually on the created network.

**Start PostgreSQL:**
```sh
docker run -d \
  --name postgres \
  --network my-network \
  -e POSTGRES_PASSWORD=root \
  postgres:14
```

**Start pgAdmin:**
```sh
docker run -d \
  --name pgadmin \
  --network my-network \
  -e PGADMIN_DEFAULT_EMAIL=user@domain.com \
  -e PGADMIN_DEFAULT_PASSWORD=root \
  dpage/pgadmin4
```

**Verify Connection:**
You can test connectivity from pgAdmin to Postgres:
```sh
docker exec -it pgadmin ping postgres
```

---

## Docker Compose Setup

Alternatively, use `docker-compose` to manage services defined in `docker-compose.yml`.

**File:** `docker-compose.yml`
```yaml
version: '3.8'

services:
  postgres:
    container_name: postgres_container
    image: postgres:14
    environment:
      POSTGRES_USER: root
      POSTGRES_PASSWORD: root
      PGDATA: /data/postgres
    volumes:
      - postgres:/data/postgres
    ports:
      - "5432:5432"
    networks:
      - postgres
    restart: unless-stopped

  pgadmin:
    container_name: pgadmin_container
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: ${PGADMIN_DEFAULT_EMAIL:-pgadmin4@pgadmin.org}
      PGADMIN_DEFAULT_PASSWORD: ${PGADMIN_DEFAULT_PASSWORD:-root}
      PGADMIN_CONFIG_SERVER_MODE: 'False'
    volumes:
      - pgadmin:/var/lib/pgadmin
    ports:
      - "5050:80"
    networks:
      - postgres
    restart: unless-stopped

networks:
  postgres:
    driver: bridge

volumes:
  postgres:
  pgadmin:
```

**Run Command:**
```sh
docker-compose up -d
```
Access pgAdmin at `http://localhost:5050`

---

## Troubleshooting

### PostgreSQL TimeZone Error
**Issue:** Application fails to connect with `FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"`.

**Solution:**
1. **Update Application Properties:**
   Use the canonical timezone ID `Asia/Kolkata` in the JDBC URL.
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ecom-db?options=-c%20timezone=Asia/Kolkata
   ```

2. **Update Main Class:**
   Force the default timezone in `EcomApplication.java` to ensure the Java environment doesn't send the legacy ID.
   ```java
   public static void main(String[] args) {
       // Force timezone to Asia/Kolkata to avoid invalid "Asia/Calcutta" error from Postgres
       java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
       SpringApplication.run(EcomApplication.class, args);
   }
   ```
