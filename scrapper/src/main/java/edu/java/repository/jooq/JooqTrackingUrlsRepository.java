package edu.java.repository.jooq;

import edu.java.domain.jooq.Tables;
import edu.java.domain.jooq.tables.records.TrackingUrlsRecord;
import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsEntity;
import edu.java.entity.TrackingUrlsInput;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.TrackingUrlsRepository;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JooqTrackingUrlsRepository implements TrackingUrlsRepository {

    private final DSLContext dsl;

    @Autowired
    public JooqTrackingUrlsRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void add(TrackingUrlsInput trackingUrlsDTO) {

        TrackingUrlsRecord trackingUrlsRecord =
                dsl.insertInto(Tables.TRACKING_URLS)
                   .columns(Tables.TRACKING_URLS.URL_ID, Tables.TRACKING_URLS.CHAT_ID)
                   .values(
                           trackingUrlsDTO.urlId(), trackingUrlsDTO.chatId()
                   )
                   .onConflictDoNothing()
                   .returning()
                   .fetchOne();
        if (trackingUrlsRecord == null) {
            throw new AlreadyExistException("this url is already tracking");
        }
    }

    @Override
    public void remove(TrackingUrlsDelete trackingUrlsDTO) {
        TrackingUrlsRecord trackingUrlsRecord = dsl.deleteFrom(Tables.TRACKING_URLS)
                                                   .where(Tables.TRACKING_URLS.URL_ID.equal(
                                                           trackingUrlsDTO.urlId()))
                                                   .and(Tables.TRACKING_URLS.CHAT_ID.equal(
                                                           trackingUrlsDTO.chatId()))
                                                   .returning()
                                                   .fetchOne();
        if (trackingUrlsRecord == null) {
            throw new NotExistException("this url is not tracking");
        }
    }

    @Override
    public List<TrackingUrlsEntity> findAll() {
        return dsl.selectFrom(Tables.TRACKING_URLS)
                  .fetchInto(TrackingUrlsEntity.class);
    }

    @Override
    public List<TrackingUrlsEntity> findByChatId(long chatId) {
        return dsl.selectFrom(Tables.TRACKING_URLS)
                  .where(Tables.TRACKING_URLS.CHAT_ID.equal(
                          chatId))
                  .fetchInto(TrackingUrlsEntity.class);
    }

    @Override
    public List<TrackingUrlsEntity> findByUrlId(long urlId) {
        return dsl.selectFrom(Tables.TRACKING_URLS)
                  .where(Tables.TRACKING_URLS.URL_ID.equal(
                          urlId))
                  .fetchInto(TrackingUrlsEntity.class);
    }
}
