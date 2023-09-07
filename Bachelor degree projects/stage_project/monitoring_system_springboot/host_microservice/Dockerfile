FROM openjdk:11

LABEL manteiner = "alessandro cattaneo"

#copia target/userAuth-0.0.1-SNAPSHOT.jar all'interno di una cartella nell'immagine
#chiamta springboot-userService.jar
ADD target/HostMicroservice-0.0.1-SNAPSHOT.jar springboot-host-microservice.jar

ENTRYPOINT ["java", "-jar", "springboot-host-microservice.jar"]
