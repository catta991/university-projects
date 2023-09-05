# progetto-architetture-soa

The application shows differents way for users to authenticate them in a secure way 

It has its own authentication and registering mechanisms. Following authentication, also via OAuth2.0 or Keycloack, a jwt token will be issued to authorize requests made to the ResourceServerCustom.
Within the system there will be 2 type of users:
- User who will be able to access the /dashboard resource and the /user/resources resource of the ResourceServerCustom.
- Admin who, in addition to being able to access all the resources of users with the User role, will also be able to access the /admin and /admin/resources resources of the ResourceServerCustom service


## authors
catta991 federicaGraglia


## used languages
- java (with springboot framework for both services)
- html
- css

### installation
- setup keycloack (https://www.baeldung.com/spring-boot-keycloak)
- modify the application.properties file
- set the correct url in the LoginKeycloackController
