# games-store-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/games-store-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- OpenShift ([guide](https://quarkus.io/guides/deploying-to-openshift)): Generate OpenShift resources from annotations
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- OpenAPI Generator - REST Client Generator ([guide](https://docs.quarkiverse.io/quarkus-openapi-generator/dev/index.html)): Generation of Rest Clients based on OpenAPI specification files
- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- MongoDB with Panache ([guide](https://quarkus.io/guides/mongodb-panache)): Simplify your persistence code for MongoDB via the active record or the repository pattern
- REST Client ([guide](https://quarkus.io/guides/rest-client)): Call REST services
- OpenTelemetry ([guide](https://quarkus.io/guides/opentelemetry)): Use OpenTelemetry to trace services
- MongoDB with Panache for Kotlin ([guide](https://quarkus.io/guides/mongodb-panache-kotlin)): Simplify your persistence code for MongoDB in Kotlin via the active record or the repository pattern

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)



### OpenAPI Generator Codestart

Start to code with the OpenAPI Generator extension.

[Related guide section...](https://docs.quarkiverse.io/quarkus-openapi-generator/dev/index.html)

## Requirements

If you do not have added the `io.quarkus:quarkus-rest-client-jackson` or `io.quarkus:quarkus-rest-client-reactive-jackson` extension in your project, add it first:

Remember, you just need to add one of them, depending on your needs.

### REST Client Jackson:

Quarkus CLI:

```bash
quarkus ext add io.quarkus:quarkus-rest-client-jackson
```

Maven:
```bash
./mvnw quarkus:add-extension -Dextensions="io.quarkus:quarkus-rest-client-jackson"
```

Gradle:

```bash
./gradlew addExtension --extensions="io.quarkus:quarkus-rest-client-jackson"
```

or

### REST Client Reactive Jackson:

Quarkus CLI:

```bash
quarkus ext add io.quarkus:quarkus-rest-client-reactive-jackson
```

Maven:

```bash
./mvnw quarkus:add-extension -Dextensions="io.quarkus:quarkus-rest-client-reactive-jackson"
```

Gradle:

```bash
./gradlew addExtension --extensions="io.quarkus:quarkus-rest-client-reactive-jackson"
```
### REST Client

Invoke different services through REST with JSON

[Related guide section...](https://quarkus.io/guides/rest-client)

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
