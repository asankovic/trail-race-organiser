SERVICE_NAME_PREFIX = race-application-
COMMAND_SERVICE = command-service
QUERY_SERVICE = query-service
SUBPROJECTS = $(COMMAND_SERVICE) $(QUERY_SERVICE)

GRADLE = ./gradlew
BUILD = build -x test
TEST = test
RUN = bootRun

PRODUCTION_PROFILE = prod
DEVELOPMENT_PROFILE = dev
SPRING_PROFILE ?= $(DEVELOPMENT_PROFILE)

DOCKER_BUILD_WITH_ARG = docker build --build-arg
DOCKER_COMPOSE = docker compose
SUBPROJECT_ARG = SUBPROJECT

.DEFAULT_GOAL := help

help:
	@echo "Makefile for Trail Race Application"
	@echo
	@echo "Available commands:"
	@echo "  make build                         - Build the entire project"
	@echo "  make build-<service-name>          - Build Race application for selected service"
	@echo "  make test                          - Run tests for the entire project"
	@echo "  make test-<service-name>           - Run tests for selected service"
	@echo "  make containerize                  - Builds all subprojects and uses each build to create the latest docker image"
	@echo "  make containerize-<service-name>   - Builds selected service and uses created build to create the latest docker image"
	@echo "  make infra-up                      - Runs docker compose to set up needed project infrastructure"
	@echo "  make infra-down                    - Runs docker compose to clean up needed project infrastructure"
	@echo "  make dev                           - Run the development environment for all subprojects (default is development profile, pass SPRING_PROFILE to override)."
	@echo "                                       Pass any additional properties via variable SPRING_PROPS (note that they apply for all projects the same)."
	@echo "                                       NOTE THAT THIS STARTS TWO SERVERS IN PARALLEL AS DETACHED PROCESSES, MAKE SURE YOU KNOW HOW TO STOP THEM."
	@echo "                                       All console output is redirected to log files created under ./logs folder in this directory (created via this task)"
	@echo "  make dev-<service-name>            - Run the development environment for selected service (default is development profile, pass SPRING_PROFILE to override)."
	@echo "                                       Pass any additional properties via variable SPRING_PROPS."
	@echo
	@echo "Available projects/services are:"
	@echo "  command-service"
	@echo "  query-service"
	@echo

.PHONY: all build validate-% build-% test test-% containerize containerize-% dev dev-% infra-up infra-down

all: build

build:
	@echo "Building all subprojects"; \
	$(GRADLE) $(BUILD)

build-%: validate-%
	@echo "Building $*"; \
	$(GRADLE) $(SERVICE_NAME_PREFIX)$*:$(BUILD)

test:
	@echo "Running tests for all subprojects"; \
	$(GRADLE) $(TEST)

test-%: validate-%
	@echo "Running tests for $*"; \
	$(GRADLE) $(SERVICE_NAME_PREFIX)$*:$(TEST)

containerize: build
	@for subproject in $(SUBPROJECTS); do \
		echo "Creating a docker image with the tag latest for $$subproject"; \
		$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$(SERVICE_NAME_PREFIX)$$subproject -t $(SERVICE_NAME_PREFIX)$$subproject:latest .; \
	done

containerize-%: validate-% build-%
	@echo "Creating a docker image with the tag latest for $*"; \
	$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$(SERVICE_NAME_PREFIX)$* -t $(SERVICE_NAME_PREFIX)$*:latest .; \

infra-up:
	@echo "Setting up development environment"; \
	$(DOCKER_COMPOSE) up -d

infra-down:
	@echo "Cleaning up development environment"; \
	$(DOCKER_COMPOSE) down

dev:
	@mkdir -p logs
	@for subproject in $(SUBPROJECTS); do \
		echo "Running development environment for $$subproject..."; \
		touch logs/$$subproject.txt; \
		$(GRADLE) :$(SERVICE_NAME_PREFIX)$$subproject:$(RUN) \
		--args='--spring.profiles.active=$(DEVELOPMENT_PROFILE) $(SPRING_PROPS)' \
		>> logs/$$subproject.txt 2>&1 & \
	done

#TODO add debug option
dev-%: validate-%
	@echo "Running development environment for $*..."; \
	$(GRADLE) :$(SERVICE_NAME_PREFIX)$*:$(RUN) --args='--spring.profiles.active=$(SPRING_PROFILE) $(SPRING_PROPS)'

validate-%:
	@PROJECT=$*; \
	if ! echo "$(SUBPROJECTS)" | grep -w -q "$$PROJECT"; then \
		echo "Error: Project '$$PROJECT' is not allowed!"; \
		exit 1; \
	fi;