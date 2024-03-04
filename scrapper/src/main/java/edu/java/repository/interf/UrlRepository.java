package edu.java.repository.interf;

import edu.java.repository.dto.UrlDTO;
import edu.java.repository.dto.UrlInputDTO;
import java.util.List;

public interface UrlRepository {
    int add(UrlInputDTO urlDTO);

    void remove(String url);

    List<UrlDTO> findAll();
}
