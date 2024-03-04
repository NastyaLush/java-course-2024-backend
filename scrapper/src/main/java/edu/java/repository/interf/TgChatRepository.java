package edu.java.repository.interf;

import edu.java.repository.dto.ChatDTO;
import java.util.List;

public interface TgChatRepository {
    int add(long tgChatId);

    void remove(long tgChatId);

    List<ChatDTO> findAll();
}