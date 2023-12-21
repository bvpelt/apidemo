alter table openbaarlichaam
    add beginRegistratie timestamp default now() not null,
    add eindRegistratie timestamp;

create index openbaarlichaam_beginregistratie_idx on openbaarlichaam (beginregistratie);
create index openbaarlichaam_eindregistratie_idx on openbaarlichaam (eindregistratie);