FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER wang(867452708@qq.com)
ARG JAR_FILE
ENV PROFILE native
ENV JAVA_OPTS='-Xmx512m -Xms256m'
ADD target/${JAR_FILE} /opt/app.jar
EXPOSE 8095 9999
# ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Dspring.profiles.active=${PROFILE} -Djava.rmi.server.hostname=119.3.77.153 -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.rmi.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar /opt/app.jar"]
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Shanghai -Dspring.profiles.active=${PROFILE} -Djava.rmi.server.hostname=119.3.77.153 -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.rmi.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar /opt/app.jar
