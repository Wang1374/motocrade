FROM anapsix/alpine-java:8_server-jre_unlimited
MAINTAINER yangyu(867452708@qq.com)
ARG JAR_FILE
ENV PROFILE native
ENV JAVA_OPTS='-Xmx512m -Xms256m'
ADD target/${JAR_FILE} /opt/app.jar
EXPOSE 8769
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 -Dspring.profiles.active=${PROFILE} -jar /opt/app.jar

# JAVA_OPS配置，如：JAVA_OPS='-Xmx512m -Xms256m'