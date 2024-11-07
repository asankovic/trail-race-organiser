# Trail Race Organiser
Event driven CQRS microservice architecture allowing CRUD operations for organising trail races.
***

## Architecture

***

## Run instructions

Please make sure that you have at least Java 21 and Docker set up on your machine.

For more details like running different spring profiles or options, please look
at the [list of available Makefile commands](#makefile-commands)

```shell
# 1. Check out the project
git clone https://github.com/asankovic/trail-race-organiser.git
# 2. Set up the infrastructure (DB and Redis for messaging)
make infra-up
# 3. Run the services
# Option a) run each service in separate shell session to keep the output
make dev-command-service
make dev-query-service
# Option b) run both services with one command 
# (NOTE that processes are started in background, see Makefile command descriptions for more details)
# make dev
```

***

## Makefile commands

Makefile for Trail Race Application Development

### Available Commands

| Command                            | Description                                                                                                                                                              |
|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `make build`                       | Build the entire project.                                                                                                                                                |
| `make build-<service-name>`        | Build the project for a specific service (replace `<service-name>` with `command-service` or `query-service`).                                                           |
| `make test`                        | Run tests for the entire project.                                                                                                                                        |
| `make test-<service-name>`         | Run tests for a specific service (replace `<service-name>` with `command-service` or `query-service`).                                                                   |
| `make containerize`                | Builds all subprojects and creates Docker images for each build.                                                                                                         |
| `make containerize-<service-name>` | Builds a specific service and creates its Docker image (replace `<service-name>` with `command-service` or `query-service`).                                             |
| `make infra-up`                    | Set up project infrastructure using Docker Compose.                                                                                                                      |
| `make infra-down`                  | Clean up project infrastructure using Docker Compose.                                                                                                                    |
| `make dev`                         | Run the development environment for all subprojects (default is `development` profile, can be overridden with `SPRING_PROFILE`). Logs output to the `./logs` folder.     |
| `make dev-<service-name>`          | Run the development environment for a specific service (replace `<service-name>` with `command-service` or `query-service`). Can override profile with `SPRING_PROFILE`. |

### Notes

- **Profiles**: Use `SPRING_PROFILE` to set a profile other than the default `development` when running `make dev` or
  `make dev-<service-name>`.
- **Properties**: Additional properties can be passed using the `SPRING_PROPS` variable, applied to all services.
- **Log Output**: Development mode (`make dev` or `make dev-<service-name>`) logs are redirected to files in the
  `./logs` directory.
- **Available Services**:
  - `command-service`
  - `query-service`

***

## Using the application

For details on endpoints there is a detailed OpenAPI documentation on http://localhost:8081/swagger-ui/index.html and
http://localhost:8082/swagger-ui/index.html for Query and Command services respectively.

Please note the mentioned ports are default for development environment (dev spring profile) and adjust them accordingly
if needed.

Valid JWT is required for accessing the mutating Command Service endpoints. For simplicity only expiry date is checked.
Please use existing secret stored in `jwt.secret` property for `dev` profile, or provide your own.

Here is an example of a [valid JWT for
testing](https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NjI0NjEzMzR9.wcpt9p5DwYvgGwvOy-_jTIcDYsCpqUCJqSwpq0AFzLw)
using the default dev profile secret in this repo:

`eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NjI0NjEzMzR9.wcpt9p5DwYvgGwvOy-_jTIcDYsCpqUCJqSwpq0AFzLw`
***

## Available endpoints

Short summary of available endpoints per application, for more details and usage please visit OpenAPI documentation.

#### Command Service API Endpoints

| Endpoint                       | Method | Description                             | Authentication | Required Fields                                                                 |
|--------------------------------|--------|-----------------------------------------|----------------|---------------------------------------------------------------------------------|
| `/v1/runners`                  | POST   | Creates a new track runner              | Bearer Token   | `firstName`, `lastName`, `distance`                                             |
| `/v1/runners/{publicRunnerID}` | DELETE | Deletes an existing runner by public ID | Bearer Token   | `publicRunnerID` (UUID format)                                                  |
| `/v1/runners/{publicRunnerID}` | PATCH  | Updates an existing runner              | Bearer Token   | `publicRunnerID` and optionally `firstName`, `lastName`, `club`, `distanceCode` |

#### Query Service API Endpoints

| Endpoint                       | Method | Description                               | Authentication | Required Fields                |
|--------------------------------|--------|-------------------------------------------|----------------|--------------------------------|
| `/v1/runners`                  | GET    | Gets all registered track runners         | None           | None                           |
| `/v1/runners/{publicRunnerID}` | GET    | Gets a specific track runner by public ID | None           | `publicRunnerID` (UUID format) |