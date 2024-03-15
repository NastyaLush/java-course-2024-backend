package edu.java.repository.jdbc;

import edu.java.exception.NotExistException;
import edu.java.repository.entity.ChatEntity;
import edu.java.repository.interf.TgChatRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTgChatRepository implements TgChatRepository {

    private final JdbcClient jdbcClient;

    @Autowired
    public JdbcTgChatRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long add(long tgChatId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("INSERT INTO chat (tg_chat_id) VALUES (?) on conflict do nothing RETURNING id")
            .param(tgChatId)
            .update(keyHolder);
        if (update == 0) {
            throw new IllegalArgumentException("Chat already exists");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public long remove(long tgChatId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("DELETE FROM chat WHERE tg_chat_id = ? returning id")
            .param(tgChatId)
            .update(keyHolder);
        if (update == 0) {
            throw new NotExistException("this chat is not exist");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<ChatEntity> findAll() {
        return jdbcClient.sql("SELECT * FROM chat").query(ChatEntity.class).list();
    }

    @Override
    public Optional<ChatEntity> findByTgId(long chatTgId) {
        return jdbcClient.sql("SELECT *  FROM chat WHERE tg_chat_id = ?")
            .param(chatTgId)
            .query((rs, rowNum) -> new ChatEntity(
                rs.getLong("id"),
                rs.getLong("tg_chat_id")
            )).optional();
    }
}
