FROM adoptopenjdk/openjdk11:alpine-jre

COPY /build/libs/throttling*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
