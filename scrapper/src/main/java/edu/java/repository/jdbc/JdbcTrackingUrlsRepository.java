package edu.java.repository.jdbc;

import edu.java.repository.entity.TrackingUrlsDelete;
import edu.java.repository.entity.TrackingUrlsEntity;
import edu.java.repository.entity.TrackingUrlsInput;
import edu.java.repository.interf.TrackingUrlsRepository;
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
    public long add(TrackingUrlsInput trackingUrlsDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql(
                "INSERT INTO tracking_urls (url_id, chat_id) "
                    + "VALUES (?, ?)"
                    + "ON CONFLICT (url_id, chat_id) DO NOTHING "
                    + "RETURNING id;")
            .param(trackingUrlsDTO.urlId())
            .param(trackingUrlsDTO.chatId())
            .update(keyHolder);
        if (update == 0) {
            throw new IllegalArgumentException("this url is already tracking");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public long remove(TrackingUrlsDelete trackingUrlsDTO) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("DELETE FROM tracking_urls where url_id=? and  chat_id = ? Returning id")
            .param(trackingUrlsDTO.urlId())
            .param(trackingUrlsDTO.chatId())
            .update(keyHolder);
        if (update == 0) {
            throw new IllegalArgumentException("this url is not tracking");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<TrackingUrlsEntity> findAll() {
        return jdbcClient.sql("SELECT * FROM tracking_urls").query(TrackingUrlsEntity.class)
            .list();
    }

    @Override
    public List<TrackingUrlsEntity> findByChatId(long chatId) {
        return jdbcClient.sql("SELECT * FROM tracking_urls where chat_id = ?").param(chatId)
            .query(TrackingUrlsEntity.class)
            .list();
    }

    @Override
    public List<TrackingUrlsEntity> findByUrlId(long urlId) {
        return jdbcClient.sql("SELECT * FROM tracking_urls where url_id = ?").param(urlId)
            .query(TrackingUrlsEntity.class)
            .list();
    }
}
