# Versions

See https://www.baeldung.com/maven-dependency-latest-version

## Finding versions

See https://mvnrepository.com/

## Multimodule project

- See https://dzone.com/articles/maven-multi-module-project-with-versioning

## Dependencies

- See https://maven.apache.org/plugins/maven-dependency-plugin/usage.html

### Options

The following options are possible

- dependency:analyze
- dependency:analyze-dep-mgt
- dependency:analyze-report
- dependency:build-classpath
- dependency:list-repositories
- dependency:purge-local-repository
- dependency:resolve
- dependency:resolve-plugins
- dependency:sources
- dependency:tree

#### dependency:analyze

This goal performs byte code analysis to determine missing or unused dependencies. This goal is meant to be launched
from the command line. It will fork the build and execute test-compile so there are class files to analyze.

#### dependency:analyze-dep-mgt

This goal looks at the dependencies after final resolution and looks for mismatches in your dependencyManagement
section.

#### dependency:analyze-report

This goal is used to include a report of the dependencies in the output of the maven-site-plugin.

#### dependency:build-classpath

This goal will output a classpath string of dependencies from the local repository to a file or log and optionally
attach and deploy the file.

#### dependency:list-repositories

This goal lists all the repositories that this build depends upon. It shows repositories defined in your settings, poms,
and declared in transitive dependency poms.

#### dependency:purge-local-repository

This goal is meant to delete all of the dependencies for the current project (or projects, in the case of a multimodule
build) from the local repository.

#### dependency:resolve

This goal simply tells maven to resolve all test scope (includes compile) dependencies and then displays the resolved
versions. This is intended to help ensure all dependencies are downloaded to the local repository. This is useful when
troubleshooting or during intermittent remote repository failures when repeatedly building multiproject modules is
undersirable and the build is failing on dependency resolution.

#### dependency:resolve-plugins

This is the same as the resolve goal except it resolves plugins and optionally their dependencies.

#### dependency:sources

This is the same as the resolve goal except it includes the source attachments if they exist. This is useful when you
want to download source attachments to your local repository.

#### dependency:tree

This goal is used to view the dependency hierarchy of the project currently being built. It will output the resolved
tree of dependencies that the Maven build process actually uses.

Options:

- \-DoutputFile=/path/to/file
- \-DoutputType=\<format\>
    - text
    - dot
    - graphml
    - tgf

### Maven

- See https://maven.apache.org/guides/getting-started/index.html
- Default repository https://repo.maven.apache.org/maven2/

# Documentation

Generate documentation using maven's documentation system

```bash
mvn archetype:generate \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-site \
  -DgroupId=com.mycompany.app \
  -DartifactId=my-app-site
```

## Maven site

### site

Generate documentation for each project

```bash
mvn site
```

Generate documentation for the multimodule project

```bash
mvn site:stage
```

### Displaying Available Updates

```bash
mvn versions:display-dependency-updates
```

## Building Releases

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
- git tag -a \<version\> -m "\<feature\>" main
- git push --tags

## API

- get a page of bestuurlijkgebieden: http://localhost:8080/api/bestuurlijkegebieden?pageNumber=5
- get a single bestuurlijkgebied: http://localhost:8080/api/bestuurlijkegebieden/GM0221

## Scheduler

See https://www.baeldung.com/spring-scheduled-tasks

## Postgres

Backup/Restore https://www.postgresql.org/docs/current/backup.html

Options for backup/restore

- SQL Dump https://www.postgresql.org/docs/current/backup-dump.html
- File system level backup https://www.postgresql.org/docs/current/backup-file.html
- Continuous archiving https://www.postgresql.org/docs/current/continuous-archiving.html
- On restore:
    - database must already exist
    - Before restoring an SQL dump, all the users who own objects or were granted permissions on objects in the dumped
      database must already exist. If they do not, the restore will fail to recreate the objects with the original
      ownership and/or permissions. (Sometimes this is what you want, but usually it is not.)

### SQL Dump

An important advantage of pg_dump over the other backup methods described later is that pg_dump's output can generally
be re-loaded into newer versions of
PostgreSQL, whereas file-level backups and continuous archiving are both extremely server-version-specific. pg_dump is
also the only method that will work
when transferring a database to a different machine architecture, such as going from a 32-bit to a 64-bit server.

Dumps created by pg_dump are internally consistent, meaning, the dump represents a snapshot of the database at the time
pg_dump began running. pg_dump does
not block other operations on the database while it is working. (Exceptions are those operations that need to operate
with an exclusive lock, such as most
forms of ALTER TABLE.)

#### Backup

```bash
pg_dump ambtsgebied > 2024-01-24_ambtsgebied.pg_dump
```

#### Restore

```bash
pg_dump ambtsgebied < 2024-01-24_ambtsgebied.pg_dump
```

### File system level backup

Restrictions:

- The database server must be shut down in order to get a usable backup. Half-way measures such as disallowing all
  connections will not work (in part because tar and similar tools do not take an atomic snapshot of the state of the
  file system, but also because of internal buffering within the server).
- If you have dug into the details of the file system layout of the database, you might be tempted to try to back up or
  restore only certain individual tables or databases from their respective files or directories. This will not work
  because the information contained in these files is not usable without the commit log files, pg_xact/*, which contain
  the commit status of all transactions. A table file is only usable with this information. Of course it is also
  impossible to restore only a table and the associated pg_xact data because that would render all other tables in the
  database cluster useless. So file system backups only work for complete backup and restoration of an entire database
  cluster.

```bash
tar -cf backup.tar /usr/local/pgsql/data
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

-- Show geometry for bestuurlijkgebied with id 2
select ST_AsText(ST_GeomFromWKB(geometrie)) from bestuurlijkgebied where id = 2;

select b.id,b.identificatie, domein, gebiedtype, md5hash, b.begingeldigheid, b.eindgeldigheid, b.beginregistratie, b.eindregistratie, o.code, o.oin, o.lichaamtype, o.naam, o.bestuurslaag, o.beginregistratie, o.eindregistratie 
from bestuurlijkgebied b 
    left join openbaarlichaam o on (b.identificatie = o.code) 
where b.identificatie = 'WS0155' and b.eindregistratie is null and o.eindregistratie is null 
order by b.begingeldigheid, b.beginregistratie;

select * from auditlog;

-- job history for a bestuurlijkgebied
select b.identificatie, b.beginregistratie, b.eindregistratie, a.id, a.jobid, a.jobname, a.jobstate, a.registratie, l.* 
from bestuurlijkgebied b 
    left join auditlog a on (b.beginregistratie = a.registratie) 
    left join auditlog l on (a.jobid = l.jobid) 
where b.identificatie = 'GM0014' and 
      a.jobstate = 'START' and 
      l.jobstate = 'FINISHED' and 
      a.jobname = 'bestuurlijkegebieden' 
order by b.identificatie, b.beginregistratie;

```

# Proces

Synchronize
See auditlog

```sql
select * from auditlog;
 id  |                jobid                 |       jobname        | jobstate |  validat   | result  |        registratie         | added | updated | unmodified | removed | skipped | processed 
-----+--------------------------------------+----------------------+----------+------------+---------+----------------------------+-------+---------+------------+---------+---------+-----------
   1 | 4011a8e7-a501-4ab9-bb8c-dee4db473e6e | openbarelichamen     | START    | 2020-01-01 |         | 2024-01-27 17:01:56.580713 |     0 |       0 |          0 |       0 |       0 |         0
   2 | 4011a8e7-a501-4ab9-bb8c-dee4db473e6e | openbarelichamen     | FINISHED | 2020-01-01 | SUCCESS | 2024-01-27 17:02:01.892638 |   389 |       0 |          0 |       0 |       0 |       389
   3 | bcd2af78-989b-4fcc-8510-022fded94929 | bestuurlijkegebieden | START    | 2020-01-01 |         | 2024-01-27 17:02:01.934638 |     0 |       0 |          0 |       0 |       0 |         0
   4 | bcd2af78-989b-4fcc-8510-022fded94929 | bestuurlijkegebieden | FINISHED | 2020-01-01 | SUCCESS | 2024-01-27 17:02:26.259193 |   390 |       0 |          0 |       0 |       0 |       390
   5 | a3dfc7b9-ed0c-4ddb-9a2c-9e1347e33509 | openbarelichamen     | START    | 2020-01-03 |         | 2024-01-27 17:02:26.276362 |     0 |       0 |          0 |       0 |       0 |         0
   6 | a3dfc7b9-ed0c-4ddb-9a2c-9e1347e33509 | openbarelichamen     | FINISHED | 2020-01-03 | SUCCESS | 2024-01-27 17:02:27.485997 |     0 |       0 |        389 |       0 |       0 |       389
   7 | 5bce7a69-199d-4ab5-b96e-eab43175b23b | bestuurlijkegebieden | START    | 2020-01-03 |         | 2024-01-27 17:02:27.500341 |     0 |       0 |          0 |       0 |       0 |         0
   8 | 5bce7a69-199d-4ab5-b96e-eab43175b23b | bestuurlijkegebieden | FINISHED | 2020-01-03 | SUCCESS | 2024-01-27 17:02:43.918354 |     0 |       0 |        390 |       0 |       0 |       390
   9 | 805b9fe0-e033-4bb5-9097-2d672e13fed6 | openbarelichamen     | START    | 2020-01-09 |         | 2024-01-27 17:02:43.935333 |     0 |       0 |          0 |       0 |       0 |         0
  10 | 805b9fe0-e033-4bb5-9097-2d672e13fed6 | openbarelichamen     | FINISHED | 2020-01-09 | SUCCESS | 2024-01-27 17:02:45.137362 |     0 |       0 |        389 |       0 |       0 |       389
  11 | c1464140-673b-4399-8f20-0cf8f2819d27 | bestuurlijkegebieden | START    | 2020-01-09 |         | 2024-01-27 17:02:45.172122 |     0 |       0 |          0 |       0 |       0 |         0
  12 | c1464140-673b-4399-8f20-0cf8f2819d27 | bestuurlijkegebieden | FINISHED | 2020-01-09 | SUCCESS | 2024-01-27 17:03:02.704962 |     0 |       0 |        390 |       0 |       0 |       390
  13 | 98593a25-dc22-4e2a-8ebf-8a8c78116d33 | openbarelichamen     | START    | 2020-03-01 |         | 2024-01-27 17:03:02.72481  |     0 |       0 |          0 |       0 |       0 |         0
  14 | 98593a25-dc22-4e2a-8ebf-8a8c78116d33 | openbarelichamen     | FINISHED | 2020-03-01 | SUCCESS | 2024-01-27 17:03:03.771959 |     0 |       0 |        389 |       0 |       0 |       389
  15 | cba5b1cb-fe48-4deb-8e61-172fee89114b | bestuurlijkegebieden | START    | 2020-03-01 |         | 2024-01-27 17:03:03.781815 |     0 |       0 |          0 |       0 |       0 |         0
  16 | cba5b1cb-fe48-4deb-8e61-172fee89114b | bestuurlijkegebieden | FINISHED | 2020-03-01 | SUCCESS | 2024-01-27 17:03:20.909785 |     0 |       0 |        390 |       0 |       0 |       390
  17 | e46fbacb-f8f1-4b81-a084-2286ef7fe534 | openbarelichamen     | START    | 2020-03-03 |         | 2024-01-27 17:03:20.950811 |     0 |       0 |          0 |       0 |       0 |         0
  18 | e46fbacb-f8f1-4b81-a084-2286ef7fe534 | openbarelichamen     | FINISHED | 2020-03-03 | SUCCESS | 2024-01-27 17:03:21.974227 |     0 |       0 |        389 |       0 |       0 |       389
  19 | dee603f5-7a2a-4364-9a00-5f0c6aacb600 | bestuurlijkegebieden | START    | 2020-03-03 |         | 2024-01-27 17:03:21.997646 |     0 |       0 |          0 |       0 |       0 |         0
  20 | dee603f5-7a2a-4364-9a00-5f0c6aacb600 | bestuurlijkegebieden | FINISHED | 2020-03-03 | SUCCESS | 2024-01-27 17:03:38.486114 |     0 |       0 |        390 |       0 |       0 |       390
  21 | 7a818912-0345-4b4a-a1ab-b224a26e921e | openbarelichamen     | START    | 2020-03-09 |         | 2024-01-27 17:03:38.50627  |     0 |       0 |          0 |       0 |       0 |         0
  22 | 7a818912-0345-4b4a-a1ab-b224a26e921e | openbarelichamen     | FINISHED | 2020-03-09 | SUCCESS | 2024-01-27 17:03:39.64591  |     0 |       0 |        389 |       0 |       0 |       389
  23 | 1a8ab3aa-3109-444e-863b-239b36a2154b | bestuurlijkegebieden | START    | 2020-03-09 |         | 2024-01-27 17:03:39.738603 |     0 |       0 |          0 |       0 |       0 |         0
  24 | 1a8ab3aa-3109-444e-863b-239b36a2154b | bestuurlijkegebieden | FINISHED | 2020-03-09 | SUCCESS | 2024-01-27 17:03:55.698455 |     0 |       0 |        390 |       0 |       0 |       390
  25 | 0fff5419-a850-49ef-b59d-084e1be411cb | openbarelichamen     | START    | 2020-06-01 |         | 2024-01-27 17:03:55.721917 |     0 |       0 |          0 |       0 |       0 |         0
  26 | 0fff5419-a850-49ef-b59d-084e1be411cb | openbarelichamen     | FINISHED | 2020-06-01 | SUCCESS | 2024-01-27 17:03:56.95455  |     0 |       0 |        389 |       0 |       0 |       389
  27 | 24a2d7ec-a87d-4339-bf32-98cbcab3c2c6 | bestuurlijkegebieden | START    | 2020-06-01 |         | 2024-01-27 17:03:57.000843 |     0 |       0 |          0 |       0 |       0 |         0
  28 | 24a2d7ec-a87d-4339-bf32-98cbcab3c2c6 | bestuurlijkegebieden | FINISHED | 2020-06-01 | SUCCESS | 2024-01-27 17:04:15.072102 |     0 |       0 |        390 |       0 |       0 |       390
  29 | 00771625-efde-490d-b9ee-d15d46f21916 | openbarelichamen     | START    | 2020-06-03 |         | 2024-01-27 17:04:15.095851 |     0 |       0 |          0 |       0 |       0 |         0
  30 | 00771625-efde-490d-b9ee-d15d46f21916 | openbarelichamen     | FINISHED | 2020-06-03 | SUCCESS | 2024-01-27 17:04:16.14341  |     0 |       0 |        389 |       0 |       0 |       389
  31 | 9f63071d-4ab1-4e3b-8a4a-abd2dbb089da | bestuurlijkegebieden | START    | 2020-06-03 |         | 2024-01-27 17:04:16.153025 |     0 |       0 |          0 |       0 |       0 |         0
  32 | 9f63071d-4ab1-4e3b-8a4a-abd2dbb089da | bestuurlijkegebieden | FINISHED | 2020-06-03 | SUCCESS | 2024-01-27 17:04:33.279833 |     0 |       0 |        390 |       0 |       0 |       390
  33 | 23fd1cdd-fcc4-4447-99b1-a937471e5d2c | openbarelichamen     | START    | 2020-06-09 |         | 2024-01-27 17:04:33.299444 |     0 |       0 |          0 |       0 |       0 |         0
  34 | 23fd1cdd-fcc4-4447-99b1-a937471e5d2c | openbarelichamen     | FINISHED | 2020-06-09 | SUCCESS | 2024-01-27 17:04:34.317109 |     0 |       0 |        389 |       0 |       0 |       389
  35 | fc98ba9b-5298-4820-9d8d-6a419470922b | bestuurlijkegebieden | START    | 2020-06-09 |         | 2024-01-27 17:04:34.335133 |     0 |       0 |          0 |       0 |       0 |         0
  36 | fc98ba9b-5298-4820-9d8d-6a419470922b | bestuurlijkegebieden | FINISHED | 2020-06-09 | SUCCESS | 2024-01-27 17:04:52.271716 |     0 |       0 |        390 |       0 |       0 |       390
  37 | 1382e077-d7a6-44c0-930e-819f239006d4 | openbarelichamen     | START    | 2020-09-01 |         | 2024-01-27 17:04:52.309943 |     0 |       0 |          0 |       0 |       0 |         0
  38 | 1382e077-d7a6-44c0-930e-819f239006d4 | openbarelichamen     | FINISHED | 2020-09-01 | SUCCESS | 2024-01-27 17:04:53.333664 |     0 |       0 |        389 |       0 |       0 |       389


```

## Resources

- mapstruct https://mapstruct.org/
- postgres https://computingforgeeks.com/install-and-configure-postgresql-on-ubuntu/
- postgis https://unixcop.com/how-to-install-the-postgis-extension-for-postgresql/
- functional interfaces https://www.geeksforgeeks.org/functional-interfaces-java/
- flyway https://www.baeldung.com/database-migrations-with-flyway
- postgis https://postgis.net/docs/reference.html#Measurement_Functions
- maven documentation generation https://books.sonatype.com/mvnref-book/reference/site-generation.html
- spring application events https://reflectoring.io/spring-boot-application-events-explained/
- spring boot documentation https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
