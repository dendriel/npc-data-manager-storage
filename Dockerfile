FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD

# Build
WORKDIR /build

COPY pom.xml .
#RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package


# Package target image
#FROM openjdk:11-jre-alpine3.9
FROM adoptopenjdk/openjdk11:alpine

COPY --from=MAVEN_BUILD /build/target/npc-data-manager-storage-1.0-SNAPSHOT.jar /npc-data-manager-storage.jar

ENV MYSQL_DB=the_quest \
    MYSQL_HOST="10.10.10.5" \
    MYSQL_PORT=3306 \
    MYSQL_USER="root" \
    MYSQL_PASS="password"

CMD ["java", "-jar", "/npc-data-manager-storage.jar"]
