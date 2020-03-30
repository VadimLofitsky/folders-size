# Dockerfile for building the web-app image

FROM openjdk:12-alpine

WORKDIR /usr/dev/folders-size/lib

# Copy jar to work dir inside the container
COPY folders-size-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java"]
CMD ["-jar", "folders-size-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
