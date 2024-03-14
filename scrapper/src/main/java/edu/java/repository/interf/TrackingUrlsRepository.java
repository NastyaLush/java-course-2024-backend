package edu.java.repository.interf;

import edu.java.repository.dto.TrackingUrlsDTO;
import edu.java.repository.dto.TrackingUrlsDeleteDTO;
import edu.java.repository.dto.TrackingUrlsInputDTO;
import java.util.List;

public interface TrackingUrlsRepository {
    long add(TrackingUrlsInputDTO trackingUrlsDTO);

    long remove(TrackingUrlsDeleteDTO trackingUrlsDTO);

    List<TrackingUrlsDTO> findAll();
    List<TrackingUrlsDTO> findByTgId(long tgId);
}
