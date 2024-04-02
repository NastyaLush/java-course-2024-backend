package edu.java.repository.jdbc;

import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsEntity;
import edu.java.entity.TrackingUrlsInput;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.TrackingUrlsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTrackingUrlsRepository implements TrackingUrlsRepository {

    private final JdbcClient jdbcClient;



    @Override
    public void add(TrackingUrlsInput trackingUrlsDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql(
                                       "INSERT INTO tracking_urls (url_id, chat_id) "
                                               + "VALUES (?, ?)"
                                               + "ON CONFLICT (url_id, chat_id) DO NOTHING ")
                               .param(trackingUrlsDTO.urlId())
                               .param(trackingUrlsDTO.chatId())
                               .update(keyHolder);
        if (update == 0) {
            throw new AlreadyExistException("this url is already tracking");
        }

    }

    @Override
    public void remove(TrackingUrlsDelete trackingUrlsDTO) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("DELETE FROM tracking_urls where url_id=? and  chat_id = ?")
                               .param(trackingUrlsDTO.urlId())
                               .param(trackingUrlsDTO.chatId())
                               .update(keyHolder);
        if (update == 0) {
            throw new NotExistException("this url is not tracking");
        }

    }

    @Override
    public List<TrackingUrlsEntity> findAll() {
        return jdbcClient.sql("SELECT * FROM tracking_urls")
                         .query(TrackingUrlsEntity.class)
                         .list();
    }

    @Override
    public List<TrackingUrlsEntity> findByChatId(long chatId) {
        return jdbcClient.sql("SELECT * FROM tracking_urls where chat_id = ?")
                         .param(chatId)
                         .query(TrackingUrlsEntity.class)
                         .list();
    }

    @Override
    public List<TrackingUrlsEntity> findByUrlId(long urlId) {
        return jdbcClient.sql("SELECT * FROM tracking_urls where url_id = ?")
                         .param(urlId)
                         .query(TrackingUrlsEntity.class)
                         .list();
    }
}
