alter table bestuurlijkgebied
    add begingeldigheid date not null,
    add eindgeldigheid date,
    add registratietijdstip timestamp default now() not null;

create index bestuurlijkgebied_begingeldigheid_idx on bestuurlijkgebied (begingeldigheid);

create index bestuurlijkgebied_eindgeldigheid_idx on bestuurlijkgebied (eindgeldigheid);