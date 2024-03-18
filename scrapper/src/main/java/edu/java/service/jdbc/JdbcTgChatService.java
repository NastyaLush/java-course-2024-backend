package edu.java.service.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.service.abstractImplementation.AbstractTgChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JdbcTgChatService extends AbstractTgChatService {

    @Autowired
    public JdbcTgChatService(JdbcTgChatRepository tgChatRepository) {
        super(tgChatRepository);
    }
}
