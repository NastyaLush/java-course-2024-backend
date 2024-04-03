
--liquibase formatted sql

--changeset create_tables:1
create table if not exists url
(
    id          BIGSERIAL PRIMARY KEY,
    url         text unique,
    last_update timestamp with time zone not null,
    last_check  timestamp with time zone not null default now()

);


CREATE TABLE IF NOT EXISTS chat
(

    id         BIGSERIAL PRIMARY KEY,
    tg_chat_id bigint not null unique
);
create table if not exists tracking_urls
(
    url_id  bigint references url (id) on delete cascade,
    chat_id bigint references chat (id) on DELETE cascade

);
ALTER TABLE tracking_urls
    ADD UNIQUE (url_id, chat_id);

--liquibase formatted sql


