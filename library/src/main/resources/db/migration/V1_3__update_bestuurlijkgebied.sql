alter table bestuurlijkgebied
    add begingeldigheid date not null,
    add eindgeldigheid date,
    add beginRegistratie timestamp default now() not null,
    add eindRegistratie timestamp;

create index bestuurlijkgebied_begingeldigheid_idx on bestuurlijkgebied (begingeldigheid);

create index bestuurlijkgebied_eindgeldigheid_idx on bestuurlijkgebied (eindgeldigheid);