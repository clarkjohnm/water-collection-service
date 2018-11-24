FROM anapsix/alpine-java:8_jdk_unlimited
MAINTAINER cybersapien.org
ARG SPRING_PROFILE
ARG IGNITE_HOME_SOURCE
ARG JAR_FILE
ARG ENV_JASYPT_ENCRYPTOR_PASSWORD
#
ENV IGNITE_HOME /opt/ignite
ENV ACTIVE_SPRING_PROFILE ${SPRING_PROFILE}
ENV JASYPT_ENCRYPTOR_PASSWORD ${ENV_JASYPT_ENCRYPTOR_PASSWORD}
#
RUN mkdir -p /opt/water-collection-service/lib
RUN mkdir -p $IGNITE_HOME
RUN apk --no-cache add curl
#
COPY ${IGNITE_HOME_SOURCE} $IGNITE_HOME
ADD ${JAR_FILE} /opt/water-collection-service/lib/wcs.jar
#
ENTRYPOINT ["/bin/bash", "-c", "java -Dspring.profiles.active=${ACTIVE_SPRING_PROFILE} -server -Xms512m -Xmx2g -XX:MaxDirectMemorySize=2g -XX:+AlwaysPreTouch -XX:+UseG1GC -XX:+ScavengeBeforeFullGC -XX:+DisableExplicitGC -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar /opt/water-collection-service/lib/wcs.jar"]
