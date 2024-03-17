package edu.java.repository.jooq;

import edu.java.repository.entity.TrackingUrlsDelete;
import edu.java.repository.entity.TrackingUrlsEntity;
import edu.java.repository.entity.TrackingUrlsInput;
import edu.java.repository.interf.TrackingUrlsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class JooqTrackingUrlsRepository implements TrackingUrlsRepository {
    @Override
    public long add(TrackingUrlsInput trackingUrlsDTO) {
        return 0;
    }

    @Override
    public long remove(TrackingUrlsDelete trackingUrlsDTO) {
        return 0;
    }

    @Override
    public List<TrackingUrlsEntity> findAll() {
        return null;
    }

    @Override
    public List<TrackingUrlsEntity> findByChatId(long chatId) {
        return null;
    }

    @Override
    public List<TrackingUrlsEntity> findByUrlId(long urlId) {
        return null;
    }
}
