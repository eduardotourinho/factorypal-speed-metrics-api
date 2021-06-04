FROM gradle:jdk11-openj9 as build

## Build the project
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle installBootDist --no-daemon
COPY build/install/factorypal-speed-metrics-boot /home


FROM adoptopenjdk:11-jre-openj9

ARG DIST_DIR=/home/gradle/src/build/install/factorypal-speed-metrics-boot
COPY --from=build ${DIST_DIR} .

EXPOSE 8080

ARG FACTORYPAL_SPEED_METRICS_OPTS="-Dspring.active.profiles=prod"
ENTRYPOINT ["bin/factorypal-speed-metrics"]