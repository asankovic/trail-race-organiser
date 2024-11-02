FROM eclipse-temurin:21-jre

ARG SUBPROJECT

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    adduser \
    && addgroup --system track-race && adduser --system --ingroup track-race track-race \
    && rm -rf /var/lib/apt/lists/* /var/cache/apt/archives/*

COPY ${SUBPROJECT}/build/libs/${SUBPROJECT}-*.jar app.jar

VOLUME /tmp

EXPOSE 8080

USER track-race:track-race

ENTRYPOINT ["java", "-jar", "app.jar"]