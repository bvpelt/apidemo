alter table openbaarlichaam
    add beginRegistratie timestamp default now() not null,
    add eindRegistratie timestamp;

