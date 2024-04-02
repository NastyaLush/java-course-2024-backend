package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.repository.jpa.JpaTgChatRepository;
import edu.java.service.TgChatService;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements TgChatService {
    private final JpaTgChatRepository jpaTgChatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (!jpaTgChatRepository.existsByTgChatId(tgChatId)) {
            jpaTgChatRepository.saveAndFlush(new ChatEntity().setTgChatId(tgChatId)
                                                             .setUrls(new HashSet<>()));
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
