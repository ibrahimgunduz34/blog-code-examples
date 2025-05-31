FROM openjdk:17

COPY . /srv/.
WORKDIR /srv
RUN ./mvnw clean package -DskipTests
ENTRYPOINT java -cp "target/classes:target/lib/*" org.example.productbatch.Application