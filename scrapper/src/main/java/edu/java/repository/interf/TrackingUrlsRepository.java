package edu.java.repository.interf;

import edu.java.repository.dto.TrackingUrlsDTO;
import edu.java.repository.dto.TrackingUrlsInputDTO;
import java.util.List;

public interface TrackingUrlsRepository {
    int add(TrackingUrlsInputDTO trackingUrlsDTO);

    void remove(TrackingUrlsInputDTO trackingUrlsDTO);

    List<TrackingUrlsDTO> findAll();
}
