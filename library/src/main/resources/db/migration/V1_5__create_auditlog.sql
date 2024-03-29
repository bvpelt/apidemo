create table auditlog
(
    id          bigint      not null primary key,
    jobid       varchar(48) not null,
    jobname     varchar(48),
    jobstate    varchar(12) not null,
    validat     date,
    result      text,
    registratie timestamp,
    added       int,
    updated     int,
    unmodified  int,
    removed     int,
    skipped     int,
    processed   int
);

create index auditlog_jobid_idx on auditlog (jobid);

create index auditlog_jobname_idx on auditlog (jobname);


ALTER TABLE public.auditlog
    OWNER TO testuser;

ALTER TABLE public.auditlog
ALTER
COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        SEQUENCE NAME public.auditlog_id_seq
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );