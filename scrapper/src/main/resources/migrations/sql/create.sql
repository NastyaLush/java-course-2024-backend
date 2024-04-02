CREATE TABLE IF NOT EXISTS url
(
    id          serial PRIMARY KEY,
    url         text UNIQUE,
    last_update timestamp WITH TIME ZONE NOT NULL,
    last_check  timestamp WITH TIME ZONE NOT NULL DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS chat
(
    id         serial PRIMARY KEY,
    tg_chat_id int NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS tracking_urls
(
    id      serial PRIMARY KEY,
    url_id  int REFERENCES url (id) ON DELETE CASCADE,
    chat_id int REFERENCES chat (id) ON DELETE CASCADE
);


ALTER TABLE tracking_urls ADD UNIQUE (url_id, chat_id);
