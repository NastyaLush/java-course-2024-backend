package edu.java.repository.jdbc;

import edu.java.entity.UrlEntity;
import edu.java.entity.UrlInput;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.UrlRepository;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUrlRepository implements UrlRepository {
    public static final String ID_COLUMN = "id";
    public static final String URL_COLUMN = "url";
    public static final String LAST_UPDATE_COLUMN = "last_update";
    public static final String LAST_CHECK_COLUMN = "last_check";
    private final JdbcClient jdbcClient;

    @Autowired
    public JdbcUrlRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long add(UrlInput urlDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql(
                                       "INSERT INTO url (url, last_update, last_check) VALUES (?, ?, ?) "
                                               + "ON CONFLICT (url) "
                                               + "DO UPDATE SET last_update = EXCLUDED.last_update, "
                                               + "last_check = EXCLUDED.last_check RETURNING id")
                               .param(urlDTO.url())
                               .param(Timestamp.from(urlDTO.lastUpdate()
                                                           .toInstant()))
                               .param(Timestamp.from(urlDTO.lastCheck()
                                                           .toInstant()))
                               .update(keyHolder);
        if (update == 0) {
            throw new AlreadyExistException("url already exist");
        }
        return keyHolder.getKey()
                        .longValue();
    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck) {
        int update = jdbcClient.sql("UPDATE url set last_check = ? where id = ?")
                               .param(lastCheck)
                               .param(id)
                               .update();
        if (update == 0) {
            throw new NotExistException("url if this id is not exist");
        }
    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {
        int update = jdbcClient.sql("UPDATE url set last_check = ?, last_update=? where id = ?")
                               .param(lastCheck)
                               .param(lastUpdate)
                               .param(id)
                               .update();
        if (update == 0) {
            throw new NotExistException("there is no url with this id");
        }
    }

    @Override
    public long remove(String url) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("DELETE FROM url WHERE url=? RETURNING id")
                               .param(url)
                               .update(keyHolder);
        if (update == 0) {
            throw new NotExistException("this url is not exist");
        }
        return keyHolder.getKey()
                        .longValue();
    }

    @Override
    public List<UrlEntity> findAll() {
        return jdbcClient.sql("SELECT * FROM url")
                         .query((rs, rowNum) -> new UrlEntity(
                                 rs.getLong(ID_COLUMN),
                                 rs.getString(URL_COLUMN),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_UPDATE_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_CHECK_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC)
                         ))
                         .list();
    }

    @Override
    public Optional<UrlEntity> findById(long id) {
        return jdbcClient.sql("SELECT * FROM url where id = ?")
                         .param(id)
                         .query((rs, rowNum) -> new UrlEntity(
                                 rs.getLong(ID_COLUMN),
                                 rs.getString(URL_COLUMN),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_UPDATE_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_CHECK_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC)
                         ))
                         .optional();
    }

    @Override
    public Optional<UrlEntity> findByUrl(String url) {
        return jdbcClient.sql("SELECT * FROM url where url=?")
                         .param(url)
                         .query((rs, rowNum) -> new UrlEntity(
                                 rs.getLong(ID_COLUMN),
                                 rs.getString(URL_COLUMN),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_UPDATE_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_CHECK_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC)
                         ))
                         .optional();
    }

    @Override
    public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return jdbcClient.sql("SELECT * FROM url WHERE last_check <= :maxLastCheck")
                         .param("maxLastCheck", maxLastCheck)
                         .query((rs, rowNum) -> new UrlEntity(
                                 rs.getLong(ID_COLUMN),
                                 rs.getString(URL_COLUMN),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_UPDATE_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC),
                                 OffsetDateTime.ofInstant(rs.getTimestamp(LAST_CHECK_COLUMN)
                                                            .toInstant(), ZoneOffset.UTC)
                         ))
                         .list();
    }

}
