COMMAND_SERVICE = race-application-command-service
QUERY_SERVICE = race-application-query-service
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
	@echo "Makefile for Race Application"
	@echo
	@echo "Available commands:"
	@echo "  make build                         - Build the entire project"
	@echo "  make test                          - Run tests for the entire project"
	@echo "  make containerize                  - Builds all subprojects and uses each build to create the latest docker image"
	@echo "  make run                           - Run the main application for all subprojects (production profile)"
	@echo "  make dev                           - Run the development environment for all subprojects (development profile)"
	@echo "  make build-query-service           - Build Race application query service"
	@echo "  make build-command-service         - Build Race application command service"
	@echo "  make test-query-service            - Run tests for Race application query service"
	@echo "  make test-command-service          - Run tests for Race application command service"
	@echo "  make containerize-command-service  - Builds command service and uses created build to create the latest docker image"
	@echo "  make containerize-query-service    - Builds query service and uses created build to create the latest docker image"
	@echo "  make run-query-service             - Run the main application for Race application query service (production profile)"
	@echo "  make run-command-service           - Run the main application for Race application command service (production profile)"
	@echo "  make dev-query-service             - Run the development environment for Race application query service (development profile)"
	@echo "  make dev-command-service           - Run the development environment for Race application command service (development profile)"

#TODO parameterize running suproject specific tasks

all: build

build:
	@echo "Building all subprojects"
	$(GRADLE) $(BUILD)

build-command-service:
	@echo "Building Race application command service"
	$(GRADLE) $(COMMAND_SERVICE):$(BUILD)

build-query-service:
	@echo "Building Race application query service"
	$(GRADLE) $(QUERY_SERVICE):$(BUILD)

test:
	@echo "Running tests for all subprojects"
	$(GRADLE) $(TEST)

test-command-service:
	@echo "Running tests for Race application command service"
	$(GRADLE) $(COMMAND_SERVICE):$(TEST)

test-query-service:
	@echo "Running tests for Race application query service"
	$(GRADLE) $(QUERY_SERVICE):$(TEST)

containerize: build
	@for subproject in $(SUBPROJECTS); do \
		echo "Creating a docker image with the tag latest for $$subproject"; \
		$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$$subproject -t $$subproject:latest .; \
	done

containerize-command-service: build-command-service
	echo "Creating a docker image with the tag latest for Race application command service"; \
	$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$(COMMAND_SERVICE) -t $(COMMAND_SERVICE):latest .; \

containerize-query-service: build-query-service
	echo "Creating a docker image with the tag latest for Race application query service"; \
	$(DOCKER_BUILD_WITH_ARG) $(SUBPROJECT_ARG)=$(QUERY_SERVICE) -t $(QUERY_SERVICE):latest .; \

run:
	@for subproject in $(SUBPROJECTS); do \
		echo "Running application for $$subproject (production profile)..."; \
		$(GRADLE) :$$subproject:$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'; \
	done

run-command-service:
	echo "Running application for Race application command service (production profile)..."
	$(GRADLE) :$(COMMAND_SERVICE):$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'

run-query-service:
	echo "Running application for Race application query service (production profile)..."
	$(GRADLE) :$(QUERY_SERVICE):$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'

dev:
	@for subproject in $(SUBPROJECTS); do \
		echo "Running development environment for $$subproject (development profile)..."; \
		$(GRADLE) :$$subproject:$(RUN) --args='--spring.profiles.active=$(DEVELOPMENT_PROFILE)'; \
	done

dev-command-service:
	echo "Running development environment for Race application query service (development profile)..."; \
	$(GRADLE) :$(COMMAND_SERVICE):$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'

dev-query-service:
	echo "Running development environment for Race application query service (development profile)..."; \
	$(GRADLE) :$(QUERY_SERVICE):$(RUN) --args='--spring.profiles.active=$(PRODUCTION_PROFILE)'