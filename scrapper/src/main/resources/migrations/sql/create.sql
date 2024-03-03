--liquibase formatted sql

--changeset create_tables:1
create table if not exists link
(
    id     bigint primary key ,
    domain text not null,
    url    text,
    last_update timestamp not null,
    last_check timestamp not null default now()
);
create table if not exists chat
(
    id     bigint primary key ,
    chatID int not null
);
create table if not exists trackingUrls
(
    id     bigint primary key ,
    urlID  int references url(id) on delete cascade,
    chatID int references chat(id) on delete cascade
);