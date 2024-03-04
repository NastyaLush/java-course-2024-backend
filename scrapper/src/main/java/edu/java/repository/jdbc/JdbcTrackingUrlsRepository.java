package edu.java.repository.jdbc;

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
    public int add(TrackingUrlsInputDTO trackingUrlsDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("INSERT INTO tracking_urls (url_id, chat_id) VALUES (?, ?) Returning id")
                .param(trackingUrlsDTO.urlId())
                .param(trackingUrlsDTO.chatId())
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add tracking url");
        }
        return keyHolder.getKey().intValue();
    }

    @Override
    public void remove(TrackingUrlsInputDTO trackingUrlsDTO) {
        jdbcClient.sql("DELETE FROM tracking_urls WHERE url_id = ? AND chat_id = ?")
                .param(trackingUrlsDTO.urlId())
                .param(trackingUrlsDTO.chatId())
                .update();
    }

    @Override
    public List<TrackingUrlsDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM tracking_urls").query(TrackingUrlsDTO.class)
                .list();
    }
}
