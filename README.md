# OSINT Web Application

## Overview

This OSINT web application scans domains for open-source intelligence (OSINT) data using the **Amass** tool. The architecture consists of:

- **Frontend**: React (served using `serve`)
- **Backend**: Kotlin with Ktor
- **Amass API**: A small Flask app running Amass
- **Database**: PostgreSQL
- **Containerization**: Docker & Docker Compose

## Prerequisites

Ensure you have the following installed:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Environment Setup

### 1. Clone the Repository

```sh
git clone https://github.com/styxBeneath/osint-web-app.git
cd osint-app
```

### 2. Configure Environment Variables

All necessary environment variables are already defined in `docker-compose.yml`. Modify this file as needed to customize configurations such as database credentials or API endpoints.

## Running the Application

### 1. Start the Application Using Docker Compose

```sh
docker-compose up -d
```

This will automatically pull the required images from Docker Hub and run all containers. **The frontend, backend, and Amass API images are already pushed to Docker Hub**, so no manual image building is required.

### 2. Verify Running Containers

```sh
docker ps
```

Ensure all services are running correctly.

## Application Architecture

### 1. Frontend (React - Port 80)

- Provides UI for users to enter a domain and initiate a scan.
- Calls the backend API (`http://backend:8080/scan{domain}`).
- Runs inside a Docker container, served with `serve`.

### 2. Backend (Kotlin with Ktor - Port 8080)

- Handles API requests from the frontend.
- Sends domain scan requests to the OSINT API.
- Stores scan results in PostgreSQL.

### 3. OSINT API (Flask - Port 5000)

- Runs **Amass** as a subprocess.
- Supports only **`amass enum -d <domain> -active -max-depth 1`** for now but can be improved easily.
- Returns scan results as JSON.

### 4. PostgreSQL Database (Port 5432)

- Stores domain scan history.
- Runs as a separate Docker container.

## API Endpoints
### 1. Kotlin Backend API (Port 8080)
- **`POST /scan`** - Initiates a domain scan. Requires the `domain` parameter.
- **`GET /scans`** - Fetches the list of all domain scan results.

### 2. Flask OSINT API (Port 5000)
- **`GET /scan`** - Runs Amass scan for a specified domain, using the `domain` query parameter.

## Stopping the Application
```sh
docker-compose down
```

## Next Steps
- Implement authentication.
- Improve error handling and logging.
- Deploy to a cloud service.


