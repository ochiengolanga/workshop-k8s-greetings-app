FROM adoptopenjdk/openjdk8-openj9 AS TEMP_BUILD_IMAGE
ENV APP_HOME=/usr/app/
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME
COPY build.gradle settings.gradle gradlew gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build -x test || return 0
COPY . .
RUN ./gradlew build -x test

FROM openjdk:alpine
ENV ARTIFACT_NAME=greetings-2.0.1-RELEASE.jar
ENV APP_HOME=/usr/app/
ENV GREETINGS_SALUTATION=Hello
WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE /usr/app/build/libs/$ARTIFACT_NAME /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xms512m","-Xmx1024m","-Djava.security.egd=file:/dev/./urandom","-jar","-Dgreetings.salutation=${GREETINGS_SALUTATION}","/app.jar"]