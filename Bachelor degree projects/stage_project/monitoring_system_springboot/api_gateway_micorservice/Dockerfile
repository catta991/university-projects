FROM openjdk:11

LABEL manteiner = "alessandro cattaneo"

#copia target/userAuth-0.0.1-SNAPSHOT.jar all'interno di una cartella nell'immagine
#chiamta springboot-userService.jar
ADD target/authorization-0.0.1-SNAPSHOT.jar springboot-api-gateway-microservice.jar

ENTRYPOINT ["java", "-jar", "springboot-api-gateway-microservice.jar"]