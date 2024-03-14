package edu.java.repository.jdbc;

import edu.java.repository.interf.UrlRepository;
import edu.java.repository.dto.UrlDTO;
import edu.java.repository.dto.UrlInputDTO;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUrlRepository implements UrlRepository {
    private final JdbcClient jdbcClient;

    @Autowired
    public JdbcUrlRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public long add(UrlInputDTO urlDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("INSERT INTO url (url, last_update, last_check) VALUES (?, ?, ?) ON CONFLICT (url) DO UPDATE SET last_update = EXCLUDED.last_update, last_check = EXCLUDED.last_check RETURNING id")
                .param(urlDTO.url())
                .param(Timestamp.from(urlDTO.lastUpdate().toInstant()))
                .param(Timestamp.from(urlDTO.lastCheck().toInstant()))
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add url");
        }
        return keyHolder.getKey().longValue();
    }


    @Override
    public long remove(String url) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcClient.sql("DELETE FROM url WHERE url = ? RETURNING id")
                .param(url)
                .update(keyHolder);
        if(update == 0) {
            throw new RuntimeException("Failed to add url");
        }
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<UrlDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM url")
                .query((rs, rowNum) -> new UrlDTO(
                        rs.getInt("id"),
                        rs.getString("url"),
                        ZonedDateTime.ofInstant(rs.getTimestamp("last_update").toInstant(), ZoneOffset.UTC),
                        ZonedDateTime.ofInstant(rs.getTimestamp("last_check").toInstant(), ZoneOffset.UTC)
                )).list();
    }

    @Override
    public UrlDTO findById(long id) {
        return jdbcClient.sql("SELECT * FROM url where id = ?")
            .param(id)
            .query((rs, rowNum) -> new UrlDTO(
                rs.getInt("id"),
                rs.getString("url"),
                ZonedDateTime.ofInstant(rs.getTimestamp("last_update").toInstant(), ZoneOffset.UTC),
                ZonedDateTime.ofInstant(rs.getTimestamp("last_check").toInstant(), ZoneOffset.UTC)
            )).single();
    }

    @Override
    public List<UrlDTO> findNotCheckedForLongTime(ZonedDateTime max_last_check) {
        return jdbcClient.sql("SELECT * FROM url where last_check <= ?")
            .param(max_last_check)
            .query((rs, rowNum) -> new UrlDTO(
                rs.getInt("id"),
                rs.getString("url"),
                ZonedDateTime.ofInstant(rs.getTimestamp("last_update").toInstant(), ZoneOffset.UTC),
                ZonedDateTime.ofInstant(rs.getTimestamp("last_check").toInstant(), ZoneOffset.UTC)
            )).list();
    }

}
