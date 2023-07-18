FROM openjdk:17.0.2-jdk
ENV APP_HOME=/home/ubuntu/Buzzzzing-Server
WORKDIR $APP_HOME
COPY build/libs/*.jar buzzing-server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","buzzing-server.jar"]