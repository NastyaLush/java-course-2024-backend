package edu.java.service.abstractImplementation;

import edu.java.repository.TgChatRepository;
import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AbstractTgChatService implements TgChatService {
    private final TgChatRepository tgChatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        tgChatRepository.add(tgChatId);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        tgChatRepository.remove(tgChatId);
    }
}
