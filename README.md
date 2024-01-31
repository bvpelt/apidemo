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

find removed bestuurlijkegebieden
```sql
select * from auditlog where jobstate = 'START' and jobname = 'bestuurlijkegebieden';
id  |                jobid                 |       jobname        | jobstate |  validat   | result |        registratie         | added | updated | unmodified | removed | skipped | processed 
-----+--------------------------------------+----------------------+----------+------------+--------+----------------------------+-------+---------+------------+---------+---------+-----------
   3 | bcd2af78-989b-4fcc-8510-022fded94929 | bestuurlijkegebieden | START    | 2020-01-01 |        | 2024-01-27 17:02:01.934638 |     0 |       0 |          0 |       0 |       0 |         0
   7 | 5bce7a69-199d-4ab5-b96e-eab43175b23b | bestuurlijkegebieden | START    | 2020-01-03 |        | 2024-01-27 17:02:27.500341 |     0 |       0 |          0 |       0 |       0 |         0
  11 | c1464140-673b-4399-8f20-0cf8f2819d27 | bestuurlijkegebieden | START    | 2020-01-09 |        | 2024-01-27 17:02:45.172122 |     0 |       0 |          0 |       0 |       0 |         0
  15 | cba5b1cb-fe48-4deb-8e61-172fee89114b | bestuurlijkegebieden | START    | 2020-03-01 |        | 2024-01-27 17:03:03.781815 |     0 |       0 |          0 |       0 |       0 |         0
  19 | dee603f5-7a2a-4364-9a00-5f0c6aacb600 | bestuurlijkegebieden | START    | 2020-03-03 |        | 2024-01-27 17:03:21.997646 |     0 |       0 |          0 |       0 |       0 |         0
  23 | 1a8ab3aa-3109-444e-863b-239b36a2154b | bestuurlijkegebieden | START    | 2020-03-09 |        | 2024-01-27 17:03:39.738603 |     0 |       0 |          0 |       0 |       0 |         0
  27 | 24a2d7ec-a87d-4339-bf32-98cbcab3c2c6 | bestuurlijkegebieden | START    | 2020-06-01 |        | 2024-01-27 17:03:57.000843 |     0 |       0 |          0 |       0 |       0 |         0
  31 | 9f63071d-4ab1-4e3b-8a4a-abd2dbb089da | bestuurlijkegebieden | START    | 2020-06-03 |        | 2024-01-27 17:04:16.153025 |     0 |       0 |          0 |       0 |       0 |         0
  35 | fc98ba9b-5298-4820-9d8d-6a419470922b | bestuurlijkegebieden | START    | 2020-06-09 |        | 2024-01-27 17:04:34.335133 |     0 |       0 |          0 |       0 |       0 |         0
  39 | 86ec4f79-478b-48de-b7d0-5f852276a443 | bestuurlijkegebieden | START    | 2020-09-01 |        | 2024-01-27 17:04:53.351518 |     0 |       0 |          0 |       0 |       0 |         0
  43 | 195c405a-d4dd-4e25-a990-6b040496349c | bestuurlijkegebieden | START    | 2020-09-03 |        | 2024-01-27 17:05:12.043066 |     0 |       0 |          0 |       0 |       0 |         0
  47 | 65457d88-b484-4899-a89b-5200a5e555c2 | bestuurlijkegebieden | START    | 2020-09-09 |        | 2024-01-27 17:05:30.599424 |     0 |       0 |          0 |       0 |       0 |         0
  51 | bc624447-75dd-4064-8218-580947a6a8d9 | bestuurlijkegebieden | START    | 2021-01-01 |        | 2024-01-27 17:05:49.314158 |     0 |       0 |          0 |       0 |       0 |         0
  55 | 6eb0e714-8ab9-49df-882b-07fc003ee90b | bestuurlijkegebieden | START    | 2021-01-03 |        | 2024-01-27 17:06:28.400911 |     0 |       0 |          0 |       0 |       0 |         0
  59 | ec09d202-9307-43c4-a662-312b2fd47efa | bestuurlijkegebieden | START    | 2021-01-09 |        | 2024-01-27 17:06:49.821973 |     0 |       0 |          0 |       0 |       0 |         0
  63 | 47fdddb6-c329-4f04-9b23-0d8ad315ed3c | bestuurlijkegebieden | START    | 2021-03-01 |        | 2024-01-27 17:07:10.554698 |     0 |       0 |          0 |       0 |       0 |         0
  67 | 5f8dae5a-132f-4622-997a-2526b17adb86 | bestuurlijkegebieden | START    | 2021-03-03 |        | 2024-01-27 17:07:31.720104 |     0 |       0 |          0 |       0 |       0 |         0
  71 | 84f8cdf9-1bb8-48b3-bd17-67925c32db28 | bestuurlijkegebieden | START    | 2021-03-09 |        | 2024-01-27 17:07:52.468907 |     0 |       0 |          0 |       0 |       0 |         0
  75 | db7e0e9b-d30e-4051-8fa5-81704adc5dbe | bestuurlijkegebieden | START    | 2021-06-01 |        | 2024-01-27 17:08:12.608877 |     0 |       0 |          0 |       0 |       0 |         0
  79 | e08c2955-33e9-4caa-a2ea-7461ec166676 | bestuurlijkegebieden | START    | 2021-06-03 |        | 2024-01-27 17:08:33.383566 |     0 |       0 |          0 |       0 |       0 |         0
  83 | 708337a2-1791-436a-8c7f-10b014e83361 | bestuurlijkegebieden | START    | 2021-06-09 |        | 2024-01-27 17:08:53.365759 |     0 |       0 |          0 |       0 |       0 |         0
  87 | 3bc91f84-6588-48ab-aad0-f691f2c6fbe7 | bestuurlijkegebieden | START    | 2021-09-01 |        | 2024-01-27 17:09:14.016799 |     0 |       0 |          0 |       0 |       0 |         0
  91 | c01acce0-5fca-43d6-aec8-9f7aa3732a4c | bestuurlijkegebieden | START    | 2021-09-03 |        | 2024-01-27 17:09:33.846448 |     0 |       0 |          0 |       0 |       0 |         0
  95 | 7cd56d60-b7b9-477e-ae20-14e2362f1fa9 | bestuurlijkegebieden | START    | 2021-09-09 |        | 2024-01-27 17:09:53.662885 |     0 |       0 |          0 |       0 |       0 |         0
  99 | 3c0c8ba2-415a-4f96-bc8a-1812f16a41b2 | bestuurlijkegebieden | START    | 2022-01-01 |        | 2024-01-27 17:10:14.3453   |     0 |       0 |          0 |       0 |       0 |         0
 103 | 38110f32-6618-401a-b320-c0d49d6ce862 | bestuurlijkegebieden | START    | 2022-01-03 |        | 2024-01-27 17:10:58.646277 |     0 |       0 |          0 |       0 |       0 |         0
 107 | 1bd55543-79dd-4ee4-9bdb-393225f13b88 | bestuurlijkegebieden | START    | 2022-01-09 |        | 2024-01-27 17:11:22.403362 |     0 |       0 |          0 |       0 |       0 |         0
 111 | e3f2fab4-881e-43aa-80d9-aebe7669fe50 | bestuurlijkegebieden | START    | 2022-03-01 |        | 2024-01-27 17:11:46.243448 |     0 |       0 |          0 |       0 |       0 |         0
 115 | 4b9c1cdb-2e2f-43a2-bc3f-c58bb231c1c8 | bestuurlijkegebieden | START    | 2022-03-03 |        | 2024-01-27 17:12:10.25931  |     0 |       0 |          0 |       0 |       0 |         0
 119 | f9a6ad37-3fab-4aee-8edf-e48a3c5e32ff | bestuurlijkegebieden | START    | 2022-03-09 |        | 2024-01-27 17:12:33.741383 |     0 |       0 |          0 |       0 |       0 |         0
 123 | 7ddf4184-e474-45e7-a94a-a0d562c9b6ce | bestuurlijkegebieden | START    | 2022-06-01 |        | 2024-01-27 17:12:57.174214 |     0 |       0 |          0 |       0 |       0 |         0
 127 | 0ada66c3-99b3-4f4d-bba5-4fd358f41525 | bestuurlijkegebieden | START    | 2022-06-03 |        | 2024-01-27 17:13:45.781488 |     0 |       0 |          0 |       0 |       0 |         0
 131 | 0502dbf0-da07-4d4c-8442-7b4e472ad6e2 | bestuurlijkegebieden | START    | 2022-06-09 |        | 2024-01-27 17:14:12.2681   |     0 |       0 |          0 |       0 |       0 |         0
 135 | 240a4b64-cb9a-4856-a96e-526ba6413cb0 | bestuurlijkegebieden | START    | 2022-09-01 |        | 2024-01-27 17:14:38.327337 |     0 |       0 |          0 |       0 |       0 |         0
 139 | 5cb6ea74-7383-4301-a98e-61ab6044b910 | bestuurlijkegebieden | START    | 2022-09-03 |        | 2024-01-27 17:15:04.148047 |     0 |       0 |          0 |       0 |       0 |         0
 143 | 27d2b7d5-117e-4a37-b590-8617e015a17f | bestuurlijkegebieden | START    | 2022-09-09 |        | 2024-01-27 17:15:29.505619 |     0 |       0 |          0 |       0 |       0 |         0
 147 | 25b67efb-26fb-4db4-aa55-a90322123de1 | bestuurlijkegebieden | START    | 2023-01-01 |        | 2024-01-27 17:15:55.302728 |     0 |       0 |          0 |       0 |       0 |         0
 151 | 09b8aef4-b5ae-43fb-ab45-d8ca4dd282db | bestuurlijkegebieden | START    | 2023-01-03 |        | 2024-01-27 17:16:44.215873 |     0 |       0 |          0 |       0 |       0 |         0
 155 | b30d125c-03b3-4544-9791-e315dd2d1622 | bestuurlijkegebieden | START    | 2023-01-09 |        | 2024-01-27 17:17:13.799918 |     0 |       0 |          0 |       0 |       0 |         0
 159 | b761c30d-5fca-4ba3-a3bd-001c34310dac | bestuurlijkegebieden | START    | 2023-03-01 |        | 2024-01-27 17:17:43.289036 |     0 |       0 |          0 |       0 |       0 |         0
 163 | 18ce3233-6054-480f-90b5-6e22924adcc3 | bestuurlijkegebieden | START    | 2023-03-03 |        | 2024-01-27 17:18:11.963479 |     0 |       0 |          0 |       0 |       0 |         0
 167 | 0be3c44d-0b14-45d4-a9ad-c9ec3a89a493 | bestuurlijkegebieden | START    | 2023-03-09 |        | 2024-01-27 17:18:41.119174 |     0 |       0 |          0 |       0 |       0 |         0
 171 | e02db4ef-ff09-4101-a4a7-8a309d5aa774 | bestuurlijkegebieden | START    | 2023-06-01 |        | 2024-01-27 17:19:09.951921 |     0 |       0 |          0 |       0 |       0 |         0
 175 | 48fedc48-0172-414c-b261-9a426eb34230 | bestuurlijkegebieden | START    | 2023-06-03 |        | 2024-01-27 17:19:39.482637 |     0 |       0 |          0 |       0 |       0 |         0
 179 | 454e1797-d10e-41ae-a41c-dff42a94d13d | bestuurlijkegebieden | START    | 2023-06-09 |        | 2024-01-27 17:20:09.08937  |     0 |       0 |          0 |       0 |       0 |         0
 183 | a5a82d9d-c11e-48c6-8cf7-daff3d276812 | bestuurlijkegebieden | START    | 2023-09-01 |        | 2024-01-27 17:20:38.068747 |     0 |       0 |          0 |       0 |       0 |         0
 187 | 5321703c-e337-4466-9b64-7ebf1ab54489 | bestuurlijkegebieden | START    | 2023-09-03 |        | 2024-01-27 17:21:07.428569 |     0 |       0 |          0 |       0 |       0 |         0
 191 | 227be63e-551c-4625-8f27-39dc76b78915 | bestuurlijkegebieden | START    | 2023-09-09 |        | 2024-01-27 17:21:37.092904 |     0 |       0 |          0 |       0 |       0 |         0
 195 | 3d765ae7-f92a-4a20-a388-a6f6ac779826 | bestuurlijkegebieden | START    | 2024-01-01 |        | 2024-01-27 17:22:06.724233 |     0 |       0 |          0 |       0 |       0 |         0
 199 | b8b1dfd4-0855-48e4-b7d4-ba7982896e47 | bestuurlijkegebieden | START    | 2024-01-03 |        | 2024-01-27 17:22:36.633931 |     0 |       0 |          0 |       0 |       0 |         0
 203 | 89489673-a8c5-4841-9ae8-96681b6157ce | bestuurlijkegebieden | START    | 2024-01-09 |        | 2024-01-27 17:23:04.230589 |     0 |       0 |          0 |       0 |       0 |         0
 207 | dc3e746a-c296-44e4-976c-87f0170e5df6 | bestuurlijkegebieden | START    | 2024-03-01 |        | 2024-01-27 17:23:31.32113  |     0 |       0 |          0 |       0 |       0 |         0
 211 | f58b61ac-c445-40f2-8d26-474ee7b5381e | bestuurlijkegebieden | START    | 2024-03-03 |        | 2024-01-27 17:24:00.144872 |     0 |       0 |          0 |       0 |       0 |         0
 215 | de0bb49f-7f9e-428b-9df7-c65e9f476121 | bestuurlijkegebieden | START    | 2024-03-09 |        | 2024-01-27 17:24:28.283739 |     0 |       0 |          0 |       0 |       0 |         0
 219 | cb93063d-ed22-48cd-b704-23c685cf9899 | bestuurlijkegebieden | START    | 2024-06-01 |        | 2024-01-27 17:24:55.760652 |     0 |       0 |          0 |       0 |       0 |         0
 223 | 8e128f73-2f59-44de-9ef5-ba19ad19d8ff | bestuurlijkegebieden | START    | 2024-06-03 |        | 2024-01-27 17:25:24.230606 |     0 |       0 |          0 |       0 |       0 |         0
 227 | c089c310-008d-4a3d-ac96-4be8ffbf3465 | bestuurlijkegebieden | START    | 2024-06-09 |        | 2024-01-27 17:25:52.238093 |     0 |       0 |          0 |       0 |       0 |         0
(57 rows)

-- enddates
select * from auditlog where jobstate = 'FINISHED' and jobname = 'bestuurlijkegebieden';
id  |                jobid                 |       jobname        | jobstate |  validat   | result  |        registratie         | added | updated | unmodified | removed | skipped | processed 
-----+--------------------------------------+----------------------+----------+------------+---------+----------------------------+-------+---------+------------+---------+---------+-----------
   4 | bcd2af78-989b-4fcc-8510-022fded94929 | bestuurlijkegebieden | FINISHED | 2020-01-01 | SUCCESS | 2024-01-27 17:02:26.259193 |   390 |       0 |          0 |       0 |       0 |       390
   8 | 5bce7a69-199d-4ab5-b96e-eab43175b23b | bestuurlijkegebieden | FINISHED | 2020-01-03 | SUCCESS | 2024-01-27 17:02:43.918354 |     0 |       0 |        390 |       0 |       0 |       390
  12 | c1464140-673b-4399-8f20-0cf8f2819d27 | bestuurlijkegebieden | FINISHED | 2020-01-09 | SUCCESS | 2024-01-27 17:03:02.704962 |     0 |       0 |        390 |       0 |       0 |       390
  16 | cba5b1cb-fe48-4deb-8e61-172fee89114b | bestuurlijkegebieden | FINISHED | 2020-03-01 | SUCCESS | 2024-01-27 17:03:20.909785 |     0 |       0 |        390 |       0 |       0 |       390
  20 | dee603f5-7a2a-4364-9a00-5f0c6aacb600 | bestuurlijkegebieden | FINISHED | 2020-03-03 | SUCCESS | 2024-01-27 17:03:38.486114 |     0 |       0 |        390 |       0 |       0 |       390
  24 | 1a8ab3aa-3109-444e-863b-239b36a2154b | bestuurlijkegebieden | FINISHED | 2020-03-09 | SUCCESS | 2024-01-27 17:03:55.698455 |     0 |       0 |        390 |       0 |       0 |       390
  28 | 24a2d7ec-a87d-4339-bf32-98cbcab3c2c6 | bestuurlijkegebieden | FINISHED | 2020-06-01 | SUCCESS | 2024-01-27 17:04:15.072102 |     0 |       0 |        390 |       0 |       0 |       390
  32 | 9f63071d-4ab1-4e3b-8a4a-abd2dbb089da | bestuurlijkegebieden | FINISHED | 2020-06-03 | SUCCESS | 2024-01-27 17:04:33.279833 |     0 |       0 |        390 |       0 |       0 |       390
  36 | fc98ba9b-5298-4820-9d8d-6a419470922b | bestuurlijkegebieden | FINISHED | 2020-06-09 | SUCCESS | 2024-01-27 17:04:52.271716 |     0 |       0 |        390 |       0 |       0 |       390
  40 | 86ec4f79-478b-48de-b7d0-5f852276a443 | bestuurlijkegebieden | FINISHED | 2020-09-01 | SUCCESS | 2024-01-27 17:05:10.966996 |     0 |       0 |        390 |       0 |       0 |       390
  44 | 195c405a-d4dd-4e25-a990-6b040496349c | bestuurlijkegebieden | FINISHED | 2020-09-03 | SUCCESS | 2024-01-27 17:05:29.544951 |     0 |       0 |        390 |       0 |       0 |       390
  48 | 65457d88-b484-4899-a89b-5200a5e555c2 | bestuurlijkegebieden | FINISHED | 2020-09-09 | SUCCESS | 2024-01-27 17:05:47.996807 |     0 |       0 |        390 |       0 |       0 |       390
  52 | bc624447-75dd-4064-8218-580947a6a8d9 | bestuurlijkegebieden | FINISHED | 2021-01-01 | SUCCESS | 2024-01-27 17:06:27.235032 |     1 |     363 |         23 |       0 |       0 |       387
  56 | 6eb0e714-8ab9-49df-882b-07fc003ee90b | bestuurlijkegebieden | FINISHED | 2021-01-03 | SUCCESS | 2024-01-27 17:06:48.774976 |     0 |       0 |        387 |       0 |       0 |       387
  60 | ec09d202-9307-43c4-a662-312b2fd47efa | bestuurlijkegebieden | FINISHED | 2021-01-09 | SUCCESS | 2024-01-27 17:07:09.287071 |     0 |       0 |        387 |       0 |       0 |       387
  64 | 47fdddb6-c329-4f04-9b23-0d8ad315ed3c | bestuurlijkegebieden | FINISHED | 2021-03-01 | SUCCESS | 2024-01-27 17:07:30.600101 |     0 |       0 |        387 |       0 |       0 |       387
  68 | 5f8dae5a-132f-4622-997a-2526b17adb86 | bestuurlijkegebieden | FINISHED | 2021-03-03 | SUCCESS | 2024-01-27 17:07:51.450768 |     0 |       0 |        387 |       0 |       0 |       387
  72 | 84f8cdf9-1bb8-48b3-bd17-67925c32db28 | bestuurlijkegebieden | FINISHED | 2021-03-09 | SUCCESS | 2024-01-27 17:08:11.580174 |     0 |       0 |        387 |       0 |       0 |       387
  76 | db7e0e9b-d30e-4051-8fa5-81704adc5dbe | bestuurlijkegebieden | FINISHED | 2021-06-01 | SUCCESS | 2024-01-27 17:08:32.348496 |     0 |       0 |        387 |       0 |       0 |       387
  80 | e08c2955-33e9-4caa-a2ea-7461ec166676 | bestuurlijkegebieden | FINISHED | 2021-06-03 | SUCCESS | 2024-01-27 17:08:52.363052 |     0 |       0 |        387 |       0 |       0 |       387
  84 | 708337a2-1791-436a-8c7f-10b014e83361 | bestuurlijkegebieden | FINISHED | 2021-06-09 | SUCCESS | 2024-01-27 17:09:12.899101 |     0 |       0 |        387 |       0 |       0 |       387
  88 | 3bc91f84-6588-48ab-aad0-f691f2c6fbe7 | bestuurlijkegebieden | FINISHED | 2021-09-01 | SUCCESS | 2024-01-27 17:09:32.848969 |     0 |       0 |        387 |       0 |       0 |       387
  92 | c01acce0-5fca-43d6-aec8-9f7aa3732a4c | bestuurlijkegebieden | FINISHED | 2021-09-03 | SUCCESS | 2024-01-27 17:09:52.627476 |     0 |       0 |        387 |       0 |       0 |       387
  96 | 7cd56d60-b7b9-477e-ae20-14e2362f1fa9 | bestuurlijkegebieden | FINISHED | 2021-09-09 | SUCCESS | 2024-01-27 17:10:13.117575 |     0 |       0 |        387 |       0 |       0 |       387
 100 | 3c0c8ba2-415a-4f96-bc8a-1812f16a41b2 | bestuurlijkegebieden | FINISHED | 2022-01-01 | SUCCESS | 2024-01-27 17:10:57.466637 |     3 |     354 |         23 |       0 |       0 |       380
 104 | 38110f32-6618-401a-b320-c0d49d6ce862 | bestuurlijkegebieden | FINISHED | 2022-01-03 | SUCCESS | 2024-01-27 17:11:21.306114 |     0 |       0 |        380 |       0 |       0 |       380
 108 | 1bd55543-79dd-4ee4-9bdb-393225f13b88 | bestuurlijkegebieden | FINISHED | 2022-01-09 | SUCCESS | 2024-01-27 17:11:45.137656 |     0 |       0 |        380 |       0 |       0 |       380
 112 | e3f2fab4-881e-43aa-80d9-aebe7669fe50 | bestuurlijkegebieden | FINISHED | 2022-03-01 | SUCCESS | 2024-01-27 17:12:09.120779 |     0 |       0 |        380 |       0 |       0 |       380
 116 | 4b9c1cdb-2e2f-43a2-bc3f-c58bb231c1c8 | bestuurlijkegebieden | FINISHED | 2022-03-03 | SUCCESS | 2024-01-27 17:12:32.541589 |     0 |       0 |        380 |       0 |       0 |       380
 120 | f9a6ad37-3fab-4aee-8edf-e48a3c5e32ff | bestuurlijkegebieden | FINISHED | 2022-03-09 | SUCCESS | 2024-01-27 17:12:56.056898 |     0 |       0 |        380 |       0 |       0 |       380
 124 | 7ddf4184-e474-45e7-a94a-a0d562c9b6ce | bestuurlijkegebieden | FINISHED | 2022-06-01 | SUCCESS | 2024-01-27 17:13:44.459133 |     0 |     356 |         23 |       0 |       0 |       379
 128 | 0ada66c3-99b3-4f4d-bba5-4fd358f41525 | bestuurlijkegebieden | FINISHED | 2022-06-03 | SUCCESS | 2024-01-27 17:14:11.13544  |     0 |       0 |        379 |       0 |       0 |       379
 132 | 0502dbf0-da07-4d4c-8442-7b4e472ad6e2 | bestuurlijkegebieden | FINISHED | 2022-06-09 | SUCCESS | 2024-01-27 17:14:37.29786  |     0 |       0 |        379 |       0 |       0 |       379
 136 | 240a4b64-cb9a-4856-a96e-526ba6413cb0 | bestuurlijkegebieden | FINISHED | 2022-09-01 | SUCCESS | 2024-01-27 17:15:02.993177 |     0 |       0 |        379 |       0 |       0 |       379
 140 | 5cb6ea74-7383-4301-a98e-61ab6044b910 | bestuurlijkegebieden | FINISHED | 2022-09-03 | SUCCESS | 2024-01-27 17:15:28.437377 |     0 |       0 |        379 |       0 |       0 |       379
 144 | 27d2b7d5-117e-4a37-b590-8617e015a17f | bestuurlijkegebieden | FINISHED | 2022-09-09 | SUCCESS | 2024-01-27 17:15:54.194677 |     0 |       0 |        379 |       0 |       0 |       379
 148 | 25b67efb-26fb-4db4-aa55-a90322123de1 | bestuurlijkegebieden | FINISHED | 2023-01-01 | SUCCESS | 2024-01-27 17:16:43.002116 |     1 |     353 |         23 |       0 |       0 |       377
 152 | 09b8aef4-b5ae-43fb-ab45-d8ca4dd282db | bestuurlijkegebieden | FINISHED | 2023-01-03 | SUCCESS | 2024-01-27 17:17:12.522552 |     0 |       0 |        377 |       0 |       0 |       377
 156 | b30d125c-03b3-4544-9791-e315dd2d1622 | bestuurlijkegebieden | FINISHED | 2023-01-09 | SUCCESS | 2024-01-27 17:17:42.157234 |     0 |       0 |        377 |       0 |       0 |       377
 160 | b761c30d-5fca-4ba3-a3bd-001c34310dac | bestuurlijkegebieden | FINISHED | 2023-03-01 | SUCCESS | 2024-01-27 17:18:10.86379  |     0 |       0 |        377 |       0 |       0 |       377
 164 | 18ce3233-6054-480f-90b5-6e22924adcc3 | bestuurlijkegebieden | FINISHED | 2023-03-03 | SUCCESS | 2024-01-27 17:18:40.027134 |     0 |       0 |        377 |       0 |       0 |       377
 168 | 0be3c44d-0b14-45d4-a9ad-c9ec3a89a493 | bestuurlijkegebieden | FINISHED | 2023-03-09 | SUCCESS | 2024-01-27 17:19:08.880403 |     0 |       0 |        377 |       0 |       0 |       377
 172 | e02db4ef-ff09-4101-a4a7-8a309d5aa774 | bestuurlijkegebieden | FINISHED | 2023-06-01 | SUCCESS | 2024-01-27 17:19:38.470225 |     0 |       0 |        377 |       0 |       0 |       377
 176 | 48fedc48-0172-414c-b261-9a426eb34230 | bestuurlijkegebieden | FINISHED | 2023-06-03 | SUCCESS | 2024-01-27 17:20:07.974606 |     0 |       0 |        377 |       0 |       0 |       377
 180 | 454e1797-d10e-41ae-a41c-dff42a94d13d | bestuurlijkegebieden | FINISHED | 2023-06-09 | SUCCESS | 2024-01-27 17:20:36.978486 |     0 |       0 |        377 |       0 |       0 |       377
 184 | a5a82d9d-c11e-48c6-8cf7-daff3d276812 | bestuurlijkegebieden | FINISHED | 2023-09-01 | SUCCESS | 2024-01-27 17:21:06.277928 |     0 |       0 |        377 |       0 |       0 |       377
 188 | 5321703c-e337-4466-9b64-7ebf1ab54489 | bestuurlijkegebieden | FINISHED | 2023-09-03 | SUCCESS | 2024-01-27 17:21:35.85078  |     0 |       0 |        377 |       0 |       0 |       377
 192 | 227be63e-551c-4625-8f27-39dc76b78915 | bestuurlijkegebieden | FINISHED | 2023-09-09 | SUCCESS | 2024-01-27 17:22:05.553541 |     0 |       0 |        377 |       0 |       0 |       377
 196 | 3d765ae7-f92a-4a20-a388-a6f6ac779826 | bestuurlijkegebieden | FINISHED | 2024-01-01 | SUCCESS | 2024-01-27 17:22:35.55052  |     0 |      21 |        356 |       0 |       0 |       377
 200 | b8b1dfd4-0855-48e4-b7d4-ba7982896e47 | bestuurlijkegebieden | FINISHED | 2024-01-03 | SUCCESS | 2024-01-27 17:23:03.066198 |     0 |       0 |        377 |       0 |       0 |       377
 204 | 89489673-a8c5-4841-9ae8-96681b6157ce | bestuurlijkegebieden | FINISHED | 2024-01-09 | SUCCESS | 2024-01-27 17:23:30.166025 |     0 |       0 |        377 |       0 |       0 |       377
 208 | dc3e746a-c296-44e4-976c-87f0170e5df6 | bestuurlijkegebieden | FINISHED | 2024-03-01 | SUCCESS | 2024-01-27 17:23:58.903137 |     0 |       0 |        377 |       0 |       0 |       377
 212 | f58b61ac-c445-40f2-8d26-474ee7b5381e | bestuurlijkegebieden | FINISHED | 2024-03-03 | SUCCESS | 2024-01-27 17:24:27.214423 |     0 |       0 |        377 |       0 |       0 |       377
 216 | de0bb49f-7f9e-428b-9df7-c65e9f476121 | bestuurlijkegebieden | FINISHED | 2024-03-09 | SUCCESS | 2024-01-27 17:24:54.431632 |     0 |       0 |        377 |       0 |       0 |       377
 220 | cb93063d-ed22-48cd-b704-23c685cf9899 | bestuurlijkegebieden | FINISHED | 2024-06-01 | SUCCESS | 2024-01-27 17:25:23.0338   |     0 |       0 |        377 |       0 |       0 |       377
 224 | 8e128f73-2f59-44de-9ef5-ba19ad19d8ff | bestuurlijkegebieden | FINISHED | 2024-06-03 | SUCCESS | 2024-01-27 17:25:51.088431 |     0 |       0 |        377 |       0 |       0 |       377
 228 | c089c310-008d-4a3d-ac96-4be8ffbf3465 | bestuurlijkegebieden | FINISHED | 2024-06-09 | SUCCESS | 2024-01-27 17:26:17.43249  |     0 |       0 |        377 |       0 |       0 |       377
(57 rows)

    
-- job 3d765ae7-f92a-4a20-a388-a6f6ac779826 1e keer tijdstip registratie 2024-01-27 17:22:06.724233   aantal 0 updated 21 toegevoegd    
select id, identificatie, begingeldigheid, eindgeldigheid, beginregistratie, eindregistratie from bestuurlijkgebied where beginregistratie = '2024-01-27 17:02:01.934638';

-- job b8b1dfd4-0855-48e4-b7d4-ba7982896e47  1e keer bijgewerkt tijdstip registratie 2024-01-27 17:22:36.633931 toegevoegd 0 updated 0 gelijk 377
select id, identificatie, begingeldigheid, eindgeldigheid, beginregistratie, eindregistratie from bestuurlijkgebied where beginregistratie = '2024-01-27 17:05:49.314158';


select a.id, a.identificatie, a.begingeldigheid, a.eindgeldigheid, a.beginregistratie, a.eindregistratie, 
       b.id, b.identificatie, b.begingeldigheid, b.eindgeldigheid, b.beginregistratie, b.eindregistratie
from bestuurlijkgebied a, bestuurlijkgebied b 
where a.identificatie = b.identificatie and 
      a.eindgeldigheid is not null and
      a.eindregistratie is null and
      b.eindgeldigheid is null and
      b.eindregistratie is null
order by a.identificatie, a.begingeldigheid, a.beginregistratie
;


select a.id, a.identificatie, a.begingeldigheid, a.eindgeldigheid, a.beginregistratie, a.eindregistratie,
       b.id, b.identificatie, b.begingeldigheid, b.eindgeldigheid, b.beginregistratie, b.eindregistratie
from bestuurlijkgebied a, bestuurlijkgebied b
where a.identificatie = b.identificatie 
      and a.id <> b.id 
  and a.beginregistratie <> b.eindregistratie 
  and a.beginregistratie = '2024-01-27 17:02:01.934638' 
  and a.identificatie = 'GM0014'
order by a.identificatie, a.begingeldigheid, a.beginregistratie
;

select a.id, a.identificatie, a.begingeldigheid, a.eindgeldigheid, a.beginregistratie, a.eindregistratie,
       b.id, b.identificatie, b.begingeldigheid, b.eindgeldigheid, b.beginregistratie, b.eindregistratie
from bestuurlijkgebied a
  left join bestuurlijkgebied b on (a.identificatie = b.identificatie)
  where
   a.id <> b.id
  and a.beginregistratie <> b.eindregistratie
  and a.beginregistratie = '2024-01-27 17:22:06.724233 '
    and b.beginregistratie = '2024-01-27 17:22:36.633931'
  and a.identificatie = 'GM0014'
order by a.identificatie, a.begingeldigheid, a.beginregistratie
;
  

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
