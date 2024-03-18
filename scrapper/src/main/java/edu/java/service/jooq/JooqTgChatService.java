package edu.java.service.jooq;

import edu.java.repository.jooq.JooqTgChatRepository;
import edu.java.service.abstractImplementation.AbstractTgChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqTgChatService extends AbstractTgChatService {
    @Autowired
    public JooqTgChatService(JooqTgChatRepository tgChatRepository) {
        super(tgChatRepository);
    }
}
