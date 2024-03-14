package edu.java.repository.jdbc;

import edu.java.repository.dto.TrackingUrlsDeleteDTO;
import edu.java.repository.interf.TrackingUrlsRepository;
import edu.java.repository.dto.TrackingUrlsDTO;
import edu.java.repository.dto.TrackingUrlsInputDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTrackingUrlsRepository implements TrackingUrlsRepository {

    private final JdbcClient jdbcClient;

    @Autowired
    public JdbcTrackingUrlsRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long add(TrackingUrlsInputDTO trackingUrlsDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("INSERT INTO tracking_urls (url_id, chat_id) VALUES (?, ?) Returning id")
                .param(trackingUrlsDTO.urlId())
                .param(trackingUrlsDTO.chatId())
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add tracking url");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public long remove(TrackingUrlsDeleteDTO trackingUrlsDTO) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("DELETE FROM tracking_urls join url on url.id = trackingUrlsDTO.url_id WHERE url.url = ? AND chat_id = ? Returning id")
                .param(trackingUrlsDTO.url())
                .param(trackingUrlsDTO.chatId())
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add tracking url");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<TrackingUrlsDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM tracking_urls").query(TrackingUrlsDTO.class)
                .list();
    }

    @Override
    public List<TrackingUrlsDTO> findByTgId(long tgId) {
        return jdbcClient.sql("SELECT * FROM tracking_urls where chat_d = ?").param(tgId).query(TrackingUrlsDTO.class)
            .list();
    }
}
