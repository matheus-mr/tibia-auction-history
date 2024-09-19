FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

RUN mvn -f ./service.auctionsearch/pom.xml package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/service.auctionsearch/target/auctionsearch-*.jar ./auctionsearch.jar

CMD ["java", "-jar", "./auctionsearch.jar"]