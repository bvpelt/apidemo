# Versions

See https://www.baeldung.com/maven-dependency-latest-version

## Finding versions

See https://mvnrepository.com/

## Displaying Available Updates

```bash
mvn versions:display-dependency-updates
```

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