FROM gradle:jdk11-openj9 as build

## Build the project
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN ./gradlew installBootDist -x test --no-daemon


FROM adoptopenjdk:11-jre-openj9

ARG BUILD_DIST=/home/gradle/src/build/install/factorypal-speed-metrics-boot

RUN mkdir -p /app
WORKDIR /app

COPY --from=build /home/gradle/src/docs/seed ./resources/seed
COPY --from=build ${BUILD_DIST} .

EXPOSE 8080

ENTRYPOINT ["bin/factorypal-speed-metrics"]