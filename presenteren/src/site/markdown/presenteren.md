# Presenteren

After filling the database with Synchroniseren the presenteren module will show informatie for

- bestuurlijkegebieden
- openbarelichamen

The api is visible at http://localhost:8090/swagger-ui/index.html

Besides functional endpoints there also is a management endpoint http://localhost:8091/actuator

## Bestuurlijkegebieden

There are to options to retrieve information

- paged to view all bestuurlijkegebieden
- retrieve bestuurlijkgebied on identification
- for each of the above operations a timestamp may be included, to view the informatie at that moment for the complete
  list or the selected entry

### Paged retrieval

Example

```bash
curl -X 'GET' \
  'http://localhost:8090/api/bestuurlijkegebieden?page=0&size=10&sort=identificatie,desc' \
  -H 'accept: application/json'
```

### With identification

Example

```bash
curl -X 'GET' \
  'http://localhost:8090/api/bestuurlijkegebieden/GM0014' \
  -H 'accept: application/json'
```

## Openbarelichamen

There are to options to retrieve information

- paged to view all bestuurlijkegebieden
- retrieve bestuurlijkgebied on identification
- for each of the above operations a timestamp may be included, to view the informatie at that moment for the complete
  list or the selected entry

### Paged retrieval

Example

```bash
curl -X 'GET' \
  'http://localhost:8090/api/openbarelichamen?page=0&size=10&sort=code,asc&sort=oin,desc' \
  -H 'accept: application/hal+json'
```

### With identification

Example

```bash
curl -X 'GET' \
  'http://localhost:8090/api/openbarelichamen/GM0014' \
  -H 'accept: application/json'
```