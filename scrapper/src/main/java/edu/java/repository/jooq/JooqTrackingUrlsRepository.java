package edu.java.repository.jooq;

import edu.java.domain.jooq.Tables;
import edu.java.domain.jooq.tables.records.TrackingUrlsRecord;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.entity.TrackingUrlsDelete;
import edu.java.repository.entity.TrackingUrlsEntity;
import edu.java.repository.entity.TrackingUrlsInput;
import edu.java.repository.interf.TrackingUrlsRepository;
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
    public long add(TrackingUrlsInput trackingUrlsDTO) {

        TrackingUrlsRecord trackingUrlsRecord =
            dsl.insertInto(Tables.TRACKING_URLS).columns(Tables.TRACKING_URLS.URL_ID, Tables.TRACKING_URLS.CHAT_ID)
               .values(
                   Math.toIntExact(trackingUrlsDTO.urlId()), Math.toIntExact(trackingUrlsDTO.chatId())
               ).onConflictDoNothing().returning(Tables.TRACKING_URLS.ID).fetchOne();
        if (trackingUrlsRecord == null) {
            throw new AlreadyExistException("this url is already tracking");
        }
        return trackingUrlsRecord.getId();
    }

    @Override
    public long remove(TrackingUrlsDelete trackingUrlsDTO) {
        TrackingUrlsRecord trackingUrlsRecord = dsl.deleteFrom(Tables.TRACKING_URLS)
                                                   .where(Tables.TRACKING_URLS.URL_ID.equal(Math.toIntExact(
                                                       trackingUrlsDTO.urlId())))
                                                   .and(Tables.TRACKING_URLS.CHAT_ID.equal(Math.toIntExact(
                                                       trackingUrlsDTO.chatId())))
                                                   .returning(Tables.TRACKING_URLS.ID).fetchOne();
        if (trackingUrlsRecord == null) {
            throw new NotExistException("this url is not tracking");
        }
        return trackingUrlsRecord.getId();
    }

    @Override
    public List<TrackingUrlsEntity> findAll() {
        return dsl.selectFrom(Tables.TRACKING_URLS).fetchInto(TrackingUrlsEntity.class);
    }

    @Override
    public List<TrackingUrlsEntity> findByChatId(long chatId) {
        return dsl.selectFrom(Tables.TRACKING_URLS).where(Tables.TRACKING_URLS.CHAT_ID.equal(Math.toIntExact(
            chatId))).fetchInto(TrackingUrlsEntity.class);
    }

    @Override
    public List<TrackingUrlsEntity> findByUrlId(long urlId) {
        return dsl.selectFrom(Tables.TRACKING_URLS).where(Tables.TRACKING_URLS.URL_ID.equal(Math.toIntExact(
            urlId))).fetchInto(TrackingUrlsEntity.class);
    }
}
