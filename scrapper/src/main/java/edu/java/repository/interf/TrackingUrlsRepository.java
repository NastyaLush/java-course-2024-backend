package edu.java.repository.interf;

import edu.java.repository.entity.TrackingUrlsDelete;
import edu.java.repository.entity.TrackingUrlsEntity;
import edu.java.repository.entity.TrackingUrlsInput;
import java.util.List;

public interface TrackingUrlsRepository {
    long add(TrackingUrlsInput trackingUrlsDTO);

    long remove(TrackingUrlsDelete trackingUrlsDTO);

    List<TrackingUrlsEntity> findAll();

    List<TrackingUrlsEntity> findByTgId(long tgId);

    List<TrackingUrlsEntity> findByUrlId(long urlId);
}
