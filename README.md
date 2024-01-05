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
This goal looks at the dependencies after final resolution and looks for mismatches in your dependencyManagement section.

#### dependency:analyze-report
This goal is used to include a report of the dependencies in the output of the maven-site-plugin.

#### dependency:build-classpath
This goal will output a classpath string of dependencies from the local repository to a file or log and optionally attach and deploy the file.

#### dependency:list-repositories
This goal lists all the repositories that this build depends upon. It shows repositories defined in your settings, poms, and declared in transitive dependency poms.

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
This goal is used to view the dependency hierarchy of the project currently being built. It will output the resolved tree of dependencies that the Maven build process actually uses.

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
- maven documentation generation https://books.sonatype.com/mvnref-book/reference/site-generation.html
- spring application events https://reflectoring.io/spring-boot-application-events-explained/
