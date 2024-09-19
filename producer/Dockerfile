FROM openjdk

COPY --chown=1001 ./target/producer.jar .

EXPOSE 8080
USER 1001

ENTRYPOINT [ "java","-jar", "producer.jar" ]