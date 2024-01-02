# Library

The library module contains functionality used by presenteren and synchroniseren.

The functionality consists of:
- retrieving informatie from https://brk.basisregistraties.overheid.nl/api/bestuurlijke-grenzen/v2 to get bestuurlijkegrenzen and openbarelichamen (in order to get this working the api definition is slightly changed from bestuurlijkegrenzenv2.0.0.yaml to bestuurlijkegrenzenv2.0.1.yaml). OpenAPI tools are used to generate the model of the bestuurlijkegrenzen API.
- store retrieved information in a local postgres database using jpa repositories
- retrieve stored information

## Database
Precondition:
- a postgres database is active (local or as a docker image)
- the postgis extension is loaded
- there is a user 'testuser' with password '123456' defined
- there is a public schema ambtsgrenzen
- the user 'testuser' has read/write/create/modify access to the ambtsgrenzen database

During startup the database definition will be loaded using flyway and the provided database scripts.
