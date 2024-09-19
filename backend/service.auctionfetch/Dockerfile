FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

RUN mvn -f ./service.auctionfetch/pom.xml package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/service.auctionfetch/target/auctionfetch-*.jar ./auctionfetch.jar

CMD ["java", "-jar", "./auctionfetch.jar"]