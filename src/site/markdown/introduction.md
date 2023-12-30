# API Demo

This project is used as a vehicle to get impression of (im)possibilities of a multimodule project.

It includes:
- library, a library with generic functionality used by both synchroniseren en presenteren
- synchroniseren, get content from bestuurlijke grenzen and store it in a local database
- presenteren, retrieve content about bestuurlijke grenzen from the local database

## Building
To build the required modules (start at the project root location).

### Prerequisits:
- postgres active (local installation or as a docker image)
- in postgres a public schema 'ambtsgebied' defined with as user 'testuser' and password '12345'

### Start build
```bash
mvn package
```

## Running
After building the modules (start at the project root location)

### Start synchroniseren
```bash
pushd synchroniseren
mvn spring-boot:run
...
...
popd
```

To load data into the database goto the url's
- http://localhost:8080/bestuurlijkegebieden this loads bestuurlijkegebieden
- http://localhost:8080/openbarelichamen this load openbarelichamen

### Start presenteren
This only shows content if previous step [Start synchroniseren](Start synchroniseren) has been executed

```bash
pushd presenteren
mvn spring-boot:run
...
...
popd
```

- http://localhost:8090/api/bestuurlijkegebieden this retrieves bestuurlijkegebieden
- http://localhost:8090/api/openbarelichamen this load openbare lichamen