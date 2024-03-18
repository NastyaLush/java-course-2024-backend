package edu.java.repository;

import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsEntity;
import edu.java.entity.TrackingUrlsInput;
import java.util.List;

public interface TrackingUrlsRepository {
    long add(TrackingUrlsInput trackingUrlsDTO);

    long remove(TrackingUrlsDelete trackingUrlsDTO);

    List<TrackingUrlsEntity> findAll();

    List<TrackingUrlsEntity> findByChatId(long chatId);

    List<TrackingUrlsEntity> findByUrlId(long urlId);
}
