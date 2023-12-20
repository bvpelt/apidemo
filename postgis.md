# Postgis

See https://unixcop.com/how-to-install-the-postgis-extension-for-postgresql/

```sql
SELECT * FROM pg_available_extensions;
              name              | default_version | installed_version |                                                       comment                                                       
--------------------------------+-----------------+-------------------+---------------------------------------------------------------------------------------------------------------------
isn                            | 1.2             |                   | data types for international product numbering standards
pg_surgery                     | 1.0             |                   | extension to perform surgery on a damaged relation
autoinc                        | 1.0             |                   | functions for autoincrementing fields
address_standardizer-3         | 3.4.1           |                   | Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.
pg_buffercache                 | 1.4             |                   | examine the shared buffer cache
postgis                        | 3.4.1           |                   | PostGIS geometry and geography spatial types and functions
address_standardizer_data_us-3 | 3.4.1           |                   | Address Standardizer US dataset example
dict_int                       | 1.0             |                   | text search dictionary template for integers
file_fdw                       | 1.0             |                   | foreign-data wrapper for flat file access
pgrowlocks                     | 1.2             |                   | show row-level locking information
refint                         | 1.0             |                   | functions for implementing referential integrity (obsolete)
amcheck                        | 1.3             |                   | functions for verifying relation integrity
tablefunc                      | 1.0             |                   | functions that manipulate whole tables, including crosstab
postgis_sfcgal                 | 3.4.1           |                   | PostGIS SFCGAL functions
sslinfo                        | 1.2             |                   | information about SSL certificates
pg_walinspect                  | 1.1             |                   | functions to inspect contents of PostgreSQL Write-Ahead Log
postgis_raster-3               | 3.4.1           |                   | PostGIS raster types and functions
pgcrypto                       | 1.3             |                   | cryptographic functions
postgis_tiger_geocoder         | 3.4.1           |                   | PostGIS tiger geocoder and reverse geocoder
intagg                         | 1.1             |                   | integer aggregator and enumerator (obsolete)
btree_gist                     | 1.7             |                   | support for indexing common datatypes in GiST
plpgsql                        | 1.0             | 1.0               | PL/pgSQL procedural language
postgres_fdw                   | 1.1             |                   | foreign-data wrapper for remote PostgreSQL servers
adminpack                      | 2.1             |                   | administrative functions for PostgreSQL
old_snapshot                   | 1.0             |                   | utilities in support of old_snapshot_threshold
postgis_sfcgal-3               | 3.4.1           |                   | PostGIS SFCGAL functions
citext                         | 1.6             |                   | data type for case-insensitive character strings
unaccent                       | 1.1             |                   | text search dictionary that removes accents
pgstattuple                    | 1.5             |                   | show tuple-level statistics
lo                             | 1.1             |                   | Large Object maintenance
postgis-3                      | 3.4.1           |                   | PostGIS geometry and geography spatial types and functions
earthdistance                  | 1.1             |                   | calculate great-circle distances on the surface of the Earth
pg_stat_statements             | 1.10            |                   | track planning and execution statistics of all SQL statements executed
intarray                       | 1.5             |                   | functions, operators, and index support for 1-D arrays of integers
pg_visibility                  | 1.2             |                   | examine the visibility map (VM) and page-level visibility info
tcn                            | 1.0             |                   | Triggered change notifications
tsm_system_time                | 1.0             |                   | TABLESAMPLE method which accepts time in milliseconds as a limit
hstore                         | 1.8             |                   | data type for storing sets of (key, value) pairs
address_standardizer_data_us   | 3.4.1           |                   | Address Standardizer US dataset example
bloom                          | 1.0             |                   | bloom access method - signature file based index
address_standardizer           | 3.4.1           |                   | Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.
pg_trgm                        | 1.6             |                   | text similarity measurement and index searching based on trigrams
postgis_topology-3             | 3.4.1           |                   | PostGIS topology spatial types and functions
postgis_tiger_geocoder-3       | 3.4.1           |                   | PostGIS tiger geocoder and reverse geocoder
insert_username                | 1.0             |                   | functions for tracking who changed a table
dblink                         | 1.2             |                   | connect to other PostgreSQL databases from within a database
tsm_system_rows                | 1.0             |                   | TABLESAMPLE method which accepts number of rows as a limit
moddatetime                    | 1.0             |                   | functions for tracking last modification time
cube                           | 1.5             |                   | data type for multidimensional cubes
dict_xsyn                      | 1.0             |                   | text search dictionary template for extended synonym processing
xml2                           | 1.1             |                   | XPath querying and XSLT
pageinspect                    | 1.12            |                   | inspect the contents of database pages at a low level
btree_gin                      | 1.3             |                   | support for indexing common datatypes in GIN
pg_freespacemap                | 1.2             |                   | examine the free space map (FSM)
seg                            | 1.4             |                   | data type for representing line segments or floating-point intervals
postgis_topology               | 3.4.1           |                   | PostGIS topology spatial types and functions
postgis_raster                 | 3.4.1           |                   | PostGIS raster types and functions
pg_prewarm                     | 1.2             |                   | prewarm relation data
ltree                          | 1.2             |                   | data type for hierarchical tree-like structures
fuzzystrmatch                  | 1.2             |                   | determine similarities and distance between strings
uuid-ossp                      | 1.1             |                   | generate universally unique identifiers (UUIDs)
(61 rows)

CREATE EXTENSION postgis ;
CREATE EXTENSION
SELECT * FROM pg_available_extensions;
name              | default_version | installed_version |                                                       comment                                                       
--------------------------------+-----------------+-------------------+---------------------------------------------------------------------------------------------------------------------
isn                            | 1.2             |                   | data types for international product numbering standards
pg_surgery                     | 1.0             |                   | extension to perform surgery on a damaged relation
autoinc                        | 1.0             |                   | functions for autoincrementing fields
address_standardizer-3         | 3.4.1           |                   | Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.
pg_buffercache                 | 1.4             |                   | examine the shared buffer cache
postgis                        | 3.4.1           | 3.4.1             | PostGIS geometry and geography spatial types and functions
address_standardizer_data_us-3 | 3.4.1           |                   | Address Standardizer US dataset example
dict_int                       | 1.0             |                   | text search dictionary template for integers
file_fdw                       | 1.0             |                   | foreign-data wrapper for flat file access
pgrowlocks                     | 1.2             |                   | show row-level locking information
refint                         | 1.0             |                   | functions for implementing referential integrity (obsolete)
amcheck                        | 1.3             |                   | functions for verifying relation integrity
tablefunc                      | 1.0             |                   | functions that manipulate whole tables, including crosstab
postgis_sfcgal                 | 3.4.1           |                   | PostGIS SFCGAL functions
sslinfo                        | 1.2             |                   | information about SSL certificates
pg_walinspect                  | 1.1             |                   | functions to inspect contents of PostgreSQL Write-Ahead Log
postgis_raster-3               | 3.4.1           |                   | PostGIS raster types and functions
pgcrypto                       | 1.3             |                   | cryptographic functions
postgis_tiger_geocoder         | 3.4.1           |                   | PostGIS tiger geocoder and reverse geocoder
intagg                         | 1.1             |                   | integer aggregator and enumerator (obsolete)
btree_gist                     | 1.7             |                   | support for indexing common datatypes in GiST
plpgsql                        | 1.0             | 1.0               | PL/pgSQL procedural language
postgres_fdw                   | 1.1             |                   | foreign-data wrapper for remote PostgreSQL servers
adminpack                      | 2.1             |                   | administrative functions for PostgreSQL
old_snapshot                   | 1.0             |                   | utilities in support of old_snapshot_threshold
postgis_sfcgal-3               | 3.4.1           |                   | PostGIS SFCGAL functions
citext                         | 1.6             |                   | data type for case-insensitive character strings
unaccent                       | 1.1             |                   | text search dictionary that removes accents
pgstattuple                    | 1.5             |                   | show tuple-level statistics
lo                             | 1.1             |                   | Large Object maintenance
postgis-3                      | 3.4.1           |                   | PostGIS geometry and geography spatial types and functions
earthdistance                  | 1.1             |                   | calculate great-circle distances on the surface of the Earth
pg_stat_statements             | 1.10            |                   | track planning and execution statistics of all SQL statements executed
intarray                       | 1.5             |                   | functions, operators, and index support for 1-D arrays of integers
pg_visibility                  | 1.2             |                   | examine the visibility map (VM) and page-level visibility info
tcn                            | 1.0             |                   | Triggered change notifications
tsm_system_time                | 1.0             |                   | TABLESAMPLE method which accepts time in milliseconds as a limit
hstore                         | 1.8             |                   | data type for storing sets of (key, value) pairs
address_standardizer_data_us   | 3.4.1           |                   | Address Standardizer US dataset example
bloom                          | 1.0             |                   | bloom access method - signature file based index
address_standardizer           | 3.4.1           |                   | Used to parse an address into constituent elements. Generally used to support geocoding address normalization step.
pg_trgm                        | 1.6             |                   | text similarity measurement and index searching based on trigrams
postgis_topology-3             | 3.4.1           |                   | PostGIS topology spatial types and functions
postgis_tiger_geocoder-3       | 3.4.1           |                   | PostGIS tiger geocoder and reverse geocoder
insert_username                | 1.0             |                   | functions for tracking who changed a table
dblink                         | 1.2             |                   | connect to other PostgreSQL databases from within a database
tsm_system_rows                | 1.0             |                   | TABLESAMPLE method which accepts number of rows as a limit
moddatetime                    | 1.0             |                   | functions for tracking last modification time
cube                           | 1.5             |                   | data type for multidimensional cubes
dict_xsyn                      | 1.0             |                   | text search dictionary template for extended synonym processing
xml2                           | 1.1             |                   | XPath querying and XSLT
pageinspect                    | 1.12            |                   | inspect the contents of database pages at a low level
btree_gin                      | 1.3             |                   | support for indexing common datatypes in GIN
pg_freespacemap                | 1.2             |                   | examine the free space map (FSM)
seg                            | 1.4             |                   | data type for representing line segments or floating-point intervals
postgis_topology               | 3.4.1           |                   | PostGIS topology spatial types and functions
postgis_raster                 | 3.4.1           |                   | PostGIS raster types and functions
pg_prewarm                     | 1.2             |                   | prewarm relation data
ltree                          | 1.2             |                   | data type for hierarchical tree-like structures
fuzzystrmatch                  | 1.2             |                   | determine similarities and distance between strings
uuid-ossp                      | 1.1             |                   | generate universally unique identifiers (UUIDs)
(61 rows)

select postgis_version();
postgis_version
---------------------------------------
3.4 USE_GEOS=1 USE_PROJ=1 USE_STATS=1
(1 row)
```
