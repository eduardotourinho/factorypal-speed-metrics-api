FROM gradle:7-jdk11-openj9

## Build the project
RUN mkdir -p /app
COPY --chown=gradle:gradle . /app
WORKDIR /app

ENTRYPOINT ["gradle", "test", "-Dkarate.env=docker"]
