
FROM openjdk:11

LABEL manteiner = "alessandro cattaneo"


ADD target/userAuth-0.0.1-SNAPSHOT.jar springboot-user-auth-microservice.jar

#COPY ./wait-for-it.sh /wait-for-it.sh

ENTRYPOINT ["java", "-jar", "springboot-user-auth-microservice.jar"]

#ENTRYPOINT ["/wait-for-it.sh", "User-db:3306", "--timeout=30", "--", "java", "-jar", "springboot-user-auth-microservice.jar"]



