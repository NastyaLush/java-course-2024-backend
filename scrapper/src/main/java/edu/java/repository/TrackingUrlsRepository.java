package edu.java.repository;

import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsEntity;
import edu.java.entity.TrackingUrlsInput;
import java.util.List;

public interface TrackingUrlsRepository {
    void add(TrackingUrlsInput trackingUrlsDTO);

    void remove(TrackingUrlsDelete trackingUrlsDTO);

    List<TrackingUrlsEntity> findAll();

    List<TrackingUrlsEntity> findByChatId(long chatId);

    List<TrackingUrlsEntity> findByUrlId(long urlId);
}
