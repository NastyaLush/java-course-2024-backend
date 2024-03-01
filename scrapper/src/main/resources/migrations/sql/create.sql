--liquibase formatted sql

--changeset create_tables:1
create table if not exists url
(
    id     bigint primary key ,
    domain text not null,
    url    text
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