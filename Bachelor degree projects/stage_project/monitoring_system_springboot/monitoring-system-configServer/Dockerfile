FROM openjdk:11

LABEL manteiner = "alessandro cattaneo"

ADD target/monitoring-system-configServer-0.0.1-SNAPSHOT.jar springboot-monitoring-system-config-server.jar

ENTRYPOINT ["java", "-jar", "springboot-monitoring-system-config-server.jar"]