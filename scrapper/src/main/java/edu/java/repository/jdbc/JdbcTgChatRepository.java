package edu.java.repository.jdbc;

import edu.java.repository.dto.UrlDTO;
import edu.java.repository.interf.TgChatRepository;
import edu.java.repository.dto.ChatDTO;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
    public long add(long tgChatId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("INSERT INTO tg_chat (chat_id) VALUES (?) RETURNING id")
                .param(tgChatId)
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add chat");
        }
        return keyHolder.getKey().longValue();
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

    @Override
    public ChatDTO findById(long tgId) {
        return jdbcClient.sql("SELECT *  FROM tg_chat WHERE chat_id = ?")
            .param(tgId)
            .query((rs, rowNum) -> new ChatDTO(
                rs.getInt("id"),
                rs.getInt("chat_id")
            )).single();
    }
}
