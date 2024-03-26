--liquibase formatted sql

--changeset create_tables:1
create table if not exists url
(
    id          serial primary key,
    url    text unique ,
    last_update timestamp with time zone not null,
    last_check  timestamp with time zone not null default now()
);

create table if not exists chat
(
    id      serial primary key,
    tg_chat_id int not null unique
);
create table if not exists tracking_urls
(
    id      serial primary key,
    url_id  int references url (id) on delete cascade,
    chat_id int references chat (id) on DELETE cascade

);
ALTER TABLE tracking_urls
    ADD UNIQUE (url_id, chat_id);

