# trail-race-organiser
Event driven CQRS microservice architecture allowing CRUD operations for organising trail races.

## List of available Makefile commands


| Command                             | Description                                                                              |
|-------------------------------------|------------------------------------------------------------------------------------------|
| `make build`                        | Build the entire project                                                                 |
| `make test`                         | Run tests for the entire project                                                         |
| `make containerize`                 | Builds all subprojects and uses each build to create the latest Docker image             |
| `make run`                          | Run the main application for all subprojects (production profile)                        |
| `make dev`                          | Run the development environment for all subprojects (development profile)                |
| `make build-query-service`          | Build Race application query service                                                     |
| `make build-command-service`        | Build Race application command service                                                   |
| `make test-query-service`           | Run tests for Race application query service                                             |
| `make test-command-service`         | Run tests for Race application command service                                           |
| `make containerize-command-service` | Builds command service and uses created build to create the latest Docker image          |
| `make containerize-query-service`   | Builds query service and uses created build to create the latest Docker image            |
| `make run-query-service`            | Run the main application for Race application query service (production profile)         |
| `make run-command-service`          | Run the main application for Race application command service (production profile)       |
| `make dev-query-service`            | Run the development environment for Race application query service (development profile) |
| `make dev-command-service`          | Run the development environment for Race application command service (development profile) |
