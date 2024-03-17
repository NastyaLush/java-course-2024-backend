package edu.java.repository.jooq;

import edu.java.domain.jooq.Tables;
import edu.java.domain.jooq.tables.records.UrlRecord;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.entity.UrlEntity;
import edu.java.repository.entity.UrlInput;
import edu.java.repository.interf.UrlRepository;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.UpdateResultStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqUrlRepository implements UrlRepository {
    private final DSLContext dsl;

    @Autowired
    public JooqUrlRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public long add(UrlInput urlDTO) {
        UrlRecord urlRecord =
            dsl.insertInto(Tables.URL).columns(Tables.URL.URL_, Tables.URL.LAST_UPDATE, Tables.URL.LAST_CHECK)
               .values(urlDTO.url(), urlDTO.lastUpdate(), urlDTO.lastCheck())
               .onConflict(Tables.URL.URL_).doUpdate()
               .set(Tables.URL.LAST_CHECK, urlDTO.lastCheck())
               .set(Tables.URL.LAST_UPDATE, urlDTO.lastUpdate())
               .returning(Tables.URL.ID)
               .fetchOne();

        if (urlRecord == null) {
            throw new AlreadyExistException("url already exists");
        }

        return urlRecord.getId();
    }

    @Override
    public long remove(String url) {

        UrlRecord urlRecord =
            dsl.deleteFrom(Tables.URL).where(Tables.URL.URL_.equal(url)).returning(Tables.URL.ID).fetchOne();
        if (urlRecord == null) {
            throw new NotExistException("this url is not exist");
        }
        return urlRecord.getId();
    }

    @Override
    public List<UrlEntity> findAll() {
        return dsl.selectFrom(Tables.URL).fetchInto(UrlEntity.class);
    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {

        UpdateResultStep<UrlRecord> returning = dsl.update(Tables.URL)
                                                   .set(Tables.URL.LAST_CHECK, lastCheck)
                                                   .set(Tables.URL.LAST_UPDATE, lastUpdate)
                                                   .where(Tables.URL.ID.eq(Math.toIntExact(id)))
                                                   .returning();

        if (returning.fetchOptional().isEmpty()) {
            throw new NotExistException("URL with this id does not exist");
        }

    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck) {
        UpdateResultStep<UrlRecord> returning = dsl.update(Tables.URL)
                                                   .set(Tables.URL.LAST_CHECK, lastCheck)
                                                   .where(Tables.URL.ID.eq(Math.toIntExact(id)))
                                                   .returning();

        if (returning.fetchOptional().isEmpty()) {
            throw new NotExistException("there is no url with this id");
        }
    }

    @Override
    public Optional<UrlEntity> findById(long id) {
        return dsl.selectFrom(Tables.URL)
                  .where(Tables.URL.ID.eq(Math.toIntExact(id)))
                  .fetchOptional()
                  .map(urlRecord -> new UrlEntity(
                      Long.valueOf(urlRecord.get(Tables.URL.ID)),
                      urlRecord.get(Tables.URL.URL_),
                      OffsetDateTime.ofInstant(urlRecord.get(Tables.URL.LAST_UPDATE).toInstant(), ZoneOffset.UTC),
                      OffsetDateTime.ofInstant(urlRecord.get(Tables.URL.LAST_CHECK).toInstant(), ZoneOffset.UTC)
                  ));

    }

    @Override
    public Optional<UrlEntity> findByUrl(String url) {
        return dsl.selectFrom(Tables.URL)
                  .where(Tables.URL.URL_.equal(url))
                  .fetchOptional()
                  .map(urlRecord -> new UrlEntity(
                      Long.valueOf(urlRecord.get(Tables.URL.ID)),
                      urlRecord.get(Tables.URL.URL_),
                      OffsetDateTime.ofInstant(urlRecord.get(Tables.URL.LAST_UPDATE).toInstant(), ZoneOffset.UTC),
                      OffsetDateTime.ofInstant(urlRecord.get(Tables.URL.LAST_CHECK).toInstant(), ZoneOffset.UTC)
                  ));
    }

    @Override
    public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return dsl.selectFrom(Tables.URL)
                  .where(Tables.URL.LAST_CHECK.le(maxLastCheck))
                  .fetch()
                  .map(urlRecord -> new UrlEntity(
                      Long.valueOf(urlRecord.get(Tables.URL.ID)),
                      urlRecord.get(Tables.URL.URL_),
                      OffsetDateTime.ofInstant(urlRecord.get(Tables.URL.LAST_UPDATE).toInstant(), ZoneOffset.UTC),
                      OffsetDateTime.ofInstant(urlRecord.get(Tables.URL.LAST_CHECK).toInstant(), ZoneOffset.UTC)
                  ));
    }
}
