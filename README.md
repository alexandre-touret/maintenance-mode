[![Java CI with Maven](https://github.com/alexandre-touret/maintenance-mode/actions/workflows/maven.yml/badge.svg)](https://github.com/alexandre-touret/maintenance-mode/actions/workflows/maven.yml)

# API Maintenance Mode with Spring Boot
This project illustrates how to easily manage a maintenance mode on Spring boot based APIs 

## Requirements

This demo has been developed with the following piece of software:

* OpenJDK 11.0.10
* Spring Boot 2.5.0
* Maven 3.8.1


## How to run it

Build your application using this command:

```shell
mvn clean install
```

Then, run this command

```shell
java -jar ./target/maintenance-mode-0.0.1-SNAPSHOT.jar
```

Check that  is ready:
```shell
curl -s http://localhost:8080/actuator/health/readiness 
```

We can see 2 live probes:

* one relative to the database connection automatically provided by Spring
* an other one related to an application specific maintenance flag.

By default the maintenance flag is set to false. That means that the service is ready.

There is an endpoint enabling to read and set that flag.

Let's put the service in maintenance:

```shell
curl -X 'PUT' \
  'http://localhost:8080/api/maintenance' \
  -w "\n" \
  -H 'Content-Type: text/plain' \
  -d 'true' -v
```
We get an HTTP 204 answer (no-content).

We can check now that our API is no longer ready:
```shell
curl -s http://localhost:8080/q/health/ready
```

Let's inspect the code of [MaintenanceController.retreiveInMaintenance()](src/main/java/info/touret/spring/maintenancemode/controller/MaintenanceController.java) to see how the HealthCheck probe is developed.

Let's try to read a random url:

```shell
curl -s \
  -w "\n" \
  'localhost:8080/api/random' 
```
We get an answer from our API:

```shell
{
  "reason": "Service currently in maintenance"
}
```

Let's browse the code of [CheckMaintenanceFilter.java](src/main/java/info/touret/spring/maintenancemode/filter/CheckMaintenanceFilter.java) and see how this answer is provided using a good old servlet filter.


```shell
curl -X 'PUT' \
  'http://localhost:8080/api/maintenance' \
  -w "\n" \
  -H 'Content-Type: text/plain' \
  -d 'false' -v
```

We can now check now that our API is ready again:
```shell
curl -s http://localhost:8080/actuator/health/readiness | jq
```
