FROM anapsix/alpine-java:8_jdk_unlimited
MAINTAINER cybersapien.org
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
#
ENTRYPOINT ["java", "-Xms512m", "-Xmx512m", "-XX:+DisableExplicitGC", "-Djava.security.egd=file:/dev/./urandom","-jar","/opt/water-collection-service/lib/wcs.jar"]
