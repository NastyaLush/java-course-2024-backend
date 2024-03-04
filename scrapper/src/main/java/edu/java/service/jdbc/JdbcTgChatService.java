package edu.java.service.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.service.TgChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcTgChatService implements TgChatService {
    private final JdbcTgChatRepository jdbcTgChatRepository;

    @Autowired
    public JdbcTgChatService(JdbcTgChatRepository jdbcTgChatRepository) {
        this.jdbcTgChatRepository = jdbcTgChatRepository;
    }

    @Override
    public void register(long tgChatId) {
        jdbcTgChatRepository.add(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        jdbcTgChatRepository.remove(tgChatId);
    }
}
