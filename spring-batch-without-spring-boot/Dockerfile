FROM openjdk:17

COPY . /srv/.
WORKDIR /srv
RUN ./mvnw clean package -DskipTests &&\
    ./mvnw dependency:build-classpath -Dmdep.outputFile=classpath.txt

ENTRYPOINT ["bash", "-c", "java -cp \"$(cat classpath.txt):target/classes\" org.example.productbatch.Application"]
