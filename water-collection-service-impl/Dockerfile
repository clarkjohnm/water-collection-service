FROM anapsix/alpine-java:8_jdk_unlimited
MAINTAINER cybersapien.org
ARG SPRING_PROFILE
ARG IGNITE_HOME_SOURCE
ARG JAR_FILE
#
ENV IGNITE_HOME /opt/ignite
#
RUN mkdir -p /opt/water-collection-service/lib
RUN mkdir -p $IGNITE_HOME
#
COPY ${IGNITE_HOME_SOURCE} $IGNITE_HOME
ADD ${JAR_FILE} /opt/water-collection-service/lib/wcs.jar
ENV ACTIVE_SPRING_PROFILE ${SPRING_PROFILE}
#
ENTRYPOINT ["/bin/bash", "-c", "java -Dspring.profiles.active=${ACTIVE_SPRING_PROFILE} -Xms512m -Xmx512m -XX:MaxDirectMemorySize=1G -XX:+UseG1GC -XX:+DisableExplicitGC -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar /opt/water-collection-service/lib/wcs.jar"]
