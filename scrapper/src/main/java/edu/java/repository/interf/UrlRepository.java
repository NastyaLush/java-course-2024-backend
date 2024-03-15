package edu.java.repository.interf;

import edu.java.repository.entity.UrlEntity;
import edu.java.repository.entity.UrlInput;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface UrlRepository {
    long add(UrlInput urlDTO);

    long remove(String url);

    List<UrlEntity> findAll();

    void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate);

    void update(Long id, OffsetDateTime lastCheck);

    Optional<UrlEntity> findById(long id);

    Optional<UrlEntity> findByUrl(String url);

    List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck);
}
