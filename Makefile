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

DOCKER_BUILD_WITH_ARG = docker build --build-arg
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
	@echo "  make dev                           - Run the development environment for all subprojects (development profile)"
	@echo "  make dev-<service-name>            - Run the development environment for selected service (development profile)"
	@echo "  make run                           - Run the main application for all subprojects (production profile)"
	@echo "  make dev-<service-name>            - Run the main application for selected service (production profile)"
	@echo
	@echo "Available projects/services are:"
	@echo "  command-service"
	@echo "  query-service"
	@echo

.PHONY: all build validate-% build-% test test-% containerize containerize-% run run-% dev dev-%

all: build

build:
	@echo "Building all subprojects"
	$(GRADLE) $(BUILD)

build-%: validate-%
	@echo "Building $*"
	$(GRADLE) $(SERVICE_NAME_PREFIX)$*:$(BUILD)

test:
	@echo "Running tests for all subprojects"
	$(GRADLE) $(TEST)

test-%: validate-%
	@echo "Running tests for $*"
	$(GRADLE) $(SERVICE_NAME_PREFIX)$*:$(TEST)

containerize: build
	@for subproject in $(SUBPROJECTS); do \
		@echo "Creating a docker image with the tag latest for $$subproject"; \
		$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$$subproject -t $$subproject:latest .; \
	done

containerize-%: validate-% build-%
	@echo "Creating a docker image with the tag latest for $*"; \
	$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$(SERVICE_NAME_PREFIX)$* -t $(SERVICE_NAME_PREFIX)$*:latest .; \

run:
	@for subproject in $(SUBPROJECTS); do \
		@echo "Running application for $$subproject (production profile)..."; \
		$(GRADLE) :$$subproject:$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'; \
	done

run-%: validate-%
	@echo "Running application for $* (production profile)..."
	$(GRADLE) :$(SERVICE_NAME_PREFIX)$*:$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'

dev:
	@for subproject in $(SUBPROJECTS); do \
		@echo "Running development environment for $$subproject (development profile)..."; \
		$(GRADLE) :$$subproject:$(RUN) --args='--spring.profiles.active=$(DEVELOPMENT_PROFILE)'; \
	done

dev-%: validate-%
	@echo "Running development environment for $* (development profile)..."; \
	$(GRADLE) :$(SERVICE_NAME_PREFIX)$*:$(RUN) --args='--spring.profiles.active=$(DEVELOPMENT_PROFILE)'

validate-%:
	@PROJECT=$*; \
	if ! echo "$(SUBPROJECTS)" | grep -w -q "$$PROJECT"; then \
		echo "Error: Project '$$PROJECT' is not allowed!"; \
		exit 1; \
	fi;