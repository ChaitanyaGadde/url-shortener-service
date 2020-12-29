# url-shortener-service

# in memory h2 db 
    http://localhost:8080/h2-console
    url=jdbc:h2:mem:testdb
    username=sa
    password=password

# Git hub 
    git clone 
    git cd project dir
    git pull

## Running the project locally
### Running locally in the IDE

- Import to Intellij `mvn clean install`
- Start the project

####Running in local docker

- open project `docker-compose build` and then `docker-compose up` 

####Running in local kube with docker desktop

- Kubernetes along with docker desktop please use command  `kubectl apply -f shortener-deployment.yaml` 
(do not forget to set the context to docker desktop).

# postman request
    ...
    curl --location --request POST 'http://localhost:8080/shorten/generate' \
     --header 'X-Client: divya-client1' \
     --header 'Content-Type: application/json' \
     --data-raw '{
     "url":"https://spring.io/guides/gs/spring-boot-docker/",
     "maxLength":10,
     "daysToPersist":2,
     "isRest":false
     }'
     ...