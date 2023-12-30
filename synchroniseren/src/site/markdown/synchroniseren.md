# Synchroniseren

At startup the database will be created using flyway. 

The synchroniseren module retrieves information from https://brk.basisregistraties.overheid.nl/api/bestuurlijke-grenzen/v2 to load bestuurlijkegebieden en openbarelichamen.

There also is  a management endpoint http://localhost:8081/actuator

## Bestuurlijkegebieden
To load bestuurlijkegebieden

```bash
http://localhost:8080/bestuurlijkegebieden
```

## Openbarelichamen
To load openbarelichamen

```bash
http://localhost:8080/openbarelichamen
```