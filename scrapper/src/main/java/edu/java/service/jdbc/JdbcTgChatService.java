package edu.java.service.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository jdbcTgChatRepository;


    @Override
    public void register(long tgChatId) {
        jdbcTgChatRepository.add(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        jdbcTgChatRepository.remove(tgChatId);
    }
}
