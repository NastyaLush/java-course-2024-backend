package edu.java.repository.jooq;

import edu.java.repository.entity.UrlEntity;
import edu.java.repository.entity.UrlInput;
import edu.java.repository.interf.UrlRepository;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public class JooqUrlRepository implements UrlRepository {
    @Override
    public long add(UrlInput urlDTO) {
        return 0;
    }

    @Override
    public long remove(String url) {
        return 0;
    }

    @Override
    public List<UrlEntity> findAll() {
        return null;
    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {

    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck) {

    }

    @Override
    public Optional<UrlEntity> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UrlEntity> findByUrl(String url) {
        return Optional.empty();
    }

    @Override
    public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return null;
    }
}
