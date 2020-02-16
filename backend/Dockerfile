FROM openjdk:11-jdk as build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:11-jdk

ARG DEPENDENCY=/app/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8082

ENTRYPOINT ["java","-Xmx512M","-cp","app:app/lib/*","io.artyom.currencycalc.CurrencyCalcApplication", "--spring.config.location=classpath:/application-prd.yaml"]
