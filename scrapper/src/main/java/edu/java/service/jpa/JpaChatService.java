package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.jpa.JpaTgChatRepository;
import edu.java.service.TgChatService;
import org.springframework.transaction.annotation.Transactional;


public class JpaChatService implements TgChatService {
    private final JpaTgChatRepository jpaTgChatRepository;


    public JpaChatService(JpaTgChatRepository jpaTgChatRepository) {
        this.jpaTgChatRepository = jpaTgChatRepository;
    }

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (!jpaTgChatRepository.existsByTgChatId(tgChatId)) {
            jpaTgChatRepository.save(new ChatEntity().setTgChatId(tgChatId));
        } else {
            throw new AlreadyExistException("Chat already exists");
        }

    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        if (jpaTgChatRepository.existsByTgChatId(tgChatId)) {
            jpaTgChatRepository.deleteByTgChatId(tgChatId);
        } else {
            throw new NotExistException("this chat is not exist");
        }
    }
}
