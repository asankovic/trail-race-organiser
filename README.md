# trail-race-organiser
Event driven CQRS microservice architecture allowing CRUD operations for organising trail races.

## List of available Makefile commands

Makefile for Trail Race Application

### Available Commands

| Command                             | Description                                                                              |
|-------------------------------------|------------------------------------------------------------------------------------------|
| `make build`                        | Build the entire project                                                                 |
| `make build-<service-name>`         | Build Race application for the selected service                                          |
| `make test`                         | Run tests for the entire project                                                         |
| `make test-<service-name>`          | Run tests for the selected service                                                       |
| `make containerize`                 | Builds all subprojects and uses each build to create the latest Docker image            |
| `make containerize-<service-name>`  | Builds the selected service and uses the created build to create the latest Docker image |
| `make dev`                          | Run the development environment for all subprojects (development profile)                |
| `make dev-<service-name>`           | Run the development environment for the selected service (development profile)           |
| `make run`                          | Run the main application for all subprojects (production profile)                        |
| `make run-<service-name>`           | Run the main application for the selected service (production profile)                   |

### Available Projects/Services

- `command-service`
- `query-service`
