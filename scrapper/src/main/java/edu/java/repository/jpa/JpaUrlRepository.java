package edu.java.repository.jpa;

import edu.java.entity.UrlEntity;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUrlRepository extends JpaRepository<UrlEntity, Integer> {

    Optional<UrlEntity> findByUrl(String url);

    @Query("FROM url u WHERE u.lastCheck <= :maxLastCheck")
    List<UrlEntity> findNotCheckedForLongTime(@Param("maxLastCheck") OffsetDateTime dateTime);
}
