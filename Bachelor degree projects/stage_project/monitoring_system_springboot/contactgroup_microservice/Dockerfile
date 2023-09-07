FROM openjdk:11

LABEL manteiner = "alessandro cattaneo"

#copia target/userAuth-0.0.1-SNAPSHOT.jar all'interno di una cartella nell'immagine
#chiamta springboot-userService.jar
ADD target/ContactgroupMicroservice-0.0.1-SNAPSHOT.jar springboot-contactgroup-microservice.jar

ENTRYPOINT ["java", "-jar", "springboot-contactgroup-microservice.jar"]