package edu.java.repository.jdbc;

import edu.java.repository.interf.TgChatRepository;
import edu.java.repository.dto.ChatDTO;
import java.util.List;
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
    public int add(long tgChatId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("INSERT INTO tg_chat (chat_id) VALUES (?) RETURNING id")
                .param(tgChatId)
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add chat");
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public void remove(long tgChatId) {
        jdbcClient.sql("DELETE FROM tg_chat WHERE chat_id = ?")
                .param(tgChatId)
                .update();
    }

    @Override
    public List<ChatDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM tg_chat").query(ChatDTO.class).list();
    }
}
