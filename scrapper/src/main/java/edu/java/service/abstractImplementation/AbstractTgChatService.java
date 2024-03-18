package edu.java.service.abstractImplementation;

import edu.java.repository.TgChatRepository;
import edu.java.service.TgChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AbstractTgChatService implements TgChatService {
    private final TgChatRepository tgChatRepository;

    @Override
    public void register(long tgChatId) {
        tgChatRepository.add(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        tgChatRepository.remove(tgChatId);
    }
}
