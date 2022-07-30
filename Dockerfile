FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
COPY target/*.jar learntribe-inquisitve.jar
ENTRYPOINT ["java","-jar","learntribe-inquisitve.jar"]