FROM adoptopenjdk/openjdk11:alpine-jre

VOLUME /tmp

# Add Certs
# COPY target/security.crt security.crt

# RUN keytool -importcert -alias smilebat -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -trustcacerts -file security.crt

COPY target/learntribe-inquisitve-*.jar learntribe-inquisitve.jar
ENTRYPOINT ["java","-jar","learntribe-inquisitve.jar"]