# Versions

See https://www.baeldung.com/maven-dependency-latest-version

## Finding versions

See https://mvnrepository.com/

## Displaying Available Updates

```bash
mvn versions:display-dependency-updates
```

## Releases

Assuming there is a main branch the following steps are made:

- create a develop branch (from the main branch), this is used for all developers

For each feature (based on develop branch)

- git checkout -b \<feature\>
- ... do some work and **test**
- git add . # from feature branch
- git commit -m "\<feature\>"
- git push # to feature brance
- git checkout develop # switch to develop branch
- git pull # to get changes from others
- git merge \<feature\> # <feature> -\> develop incorporate changes from others
- \# ... test and resolve merge conflicts in branch develop ...
- git add . # to update develop branch
- git commit -m \<feature\> # to develop branch
- git push # to develop
- git checkout main # switch to main branch
- git merge develop # develop -\> main
- git push # to main
- git tag -a \<version\> -m "<feature>" main
- git push --tags

## API

- get a page of bestuurlijkgebieden: http://localhost:8080/api/bestuurlijkegebieden?pageNumber=5
- get a single bestuurlijkgebied: http://localhost:8080/api/bestuurlijkegebieden/GM0221

## Usefull queries

```sql
-- retrieve all bestuurlijkgebied

select identificatie,
       domein,
       gebiedtype,
       md5hash,
       begingeldigheid,
       eindgeldigheid,
       beginregistratie,
       eindregistratie
from bestuurlijkgebied

-- retrieve area from each bestuurlijkgebied

select identificatie, Round(ST_Area(geometrie)) area
from bestuurlijkgebied
order by area;

-- find openbaarlichaam without bestuurlijk gebied

select o.code
from openbaarlichaam o
         left join bestuurlijkgebied b on (o.code = b.identificatie)
where b.md5hash is null;

```

## Resources

- mapstruct https://mapstruct.org/
- postgres https://computingforgeeks.com/install-and-configure-postgresql-on-ubuntu/
- postgis https://unixcop.com/how-to-install-the-postgis-extension-for-postgresql/
- functional interfaces https://www.geeksforgeeks.org/functional-interfaces-java/
- flyway https://www.baeldung.com/database-migrations-with-flyway
- postgis https://postgis.net/docs/reference.html#Measurement_Functions