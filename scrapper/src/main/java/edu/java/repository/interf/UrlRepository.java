package edu.java.repository.interf;

import edu.java.repository.dto.UrlDTO;
import edu.java.repository.dto.UrlInputDTO;
import java.time.ZonedDateTime;
import java.util.List;

public interface UrlRepository {
    long add(UrlInputDTO urlDTO);

    long remove(String url);

    List<UrlDTO> findAll();
    UrlDTO findById(long id);
    List<UrlDTO> findNotCheckedForLongTime(ZonedDateTime max_last_check);
}
