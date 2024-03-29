package edu.java.service.jpa;

import edu.java.entity.ChatEntity;
import edu.java.entity.UrlEntity;
import edu.java.exception.NotExistException;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.repository.jpa.JpaTgChatRepository;
import edu.java.repository.jpa.JpaUrlRepository;
import edu.java.service.UrlService;
import edu.java.util.LinkManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaUrlService implements UrlService {
    private static final String CHAT_IS_NOT_EXISTS_ERROR = "this chat is not exists";
    private static final String URL_IS_NOT_EXISTS_ERROR = "this url is not exists";
    private final JpaUrlRepository jpaUrlRepository;
    private final JpaTgChatRepository jpaTgChatRepository;
    private final LinkManager linkManager;


    @Override
    @Transactional
    public LinkResponse add(long tgChatId, URI url) {
        linkManager.validateURI(url);

        UrlEntity urlEntity = jpaUrlRepository.findByUrl(url.toString())
                                              .orElse(new UrlEntity().setUrl(url.toString())
                                                                     .setLastCheck(OffsetDateTime.now())
                                                                     .setLastUpdate(OffsetDateTime.now())
                                                                     .setChats(new HashSet<>()));
        ChatEntity chatEntity = jpaTgChatRepository.findByTgChatId(tgChatId)
                                                   .orElseThrow(() -> new NotExistException(CHAT_IS_NOT_EXISTS_ERROR));
        chatEntity.addUrl(urlEntity);

        UrlEntity save = jpaUrlRepository.saveAndFlush(urlEntity);
        jpaTgChatRepository.saveAndFlush(chatEntity);

        return new LinkResponse().id(save.getId())
                                 .url(url);
    }

    @Override
    @Transactional
    public LinkResponse remove(long tgChatId, URI url) {
        UrlEntity urlEntity = jpaUrlRepository.findByUrl(url.toString())
                                              .orElseThrow(() -> new NotExistException(URL_IS_NOT_EXISTS_ERROR));
        ChatEntity chatEntity = jpaTgChatRepository.findByTgChatId(tgChatId)
                                                   .orElseThrow(() -> new NotExistException(CHAT_IS_NOT_EXISTS_ERROR));
        chatEntity.removeUrl(urlEntity);
        if (urlEntity.getChats()
                     .isEmpty()) {
            jpaUrlRepository.delete(urlEntity);
        }
        return new LinkResponse().url(url)
                                 .id(urlEntity.getId());
    }

    @Override
    @Transactional
    public LinkResponse remove(URI url) {
        UrlEntity urlEntity = jpaUrlRepository.findByUrl(url.toString())
                                              .orElseThrow(() -> new NotExistException(URL_IS_NOT_EXISTS_ERROR));
        for (ChatEntity chatEntity : getChats(urlEntity.getId())) {
            chatEntity.removeUrl(urlEntity);
        }
        jpaUrlRepository.delete(urlEntity);
        return new LinkResponse().id(urlEntity.getId())
                                 .url(url);
    }

    @Override
    @Transactional
    public void update(Long id, OffsetDateTime lastCheck) {
        UrlEntity urlEntity = jpaUrlRepository.findById(Math.toIntExact(id))
                                              .orElseThrow(() -> new NotExistException(URL_IS_NOT_EXISTS_ERROR));
        urlEntity.setLastCheck(lastCheck);
        jpaUrlRepository.save(urlEntity);
    }

    @Override
    @Transactional
    public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {
        UrlEntity urlEntity = jpaUrlRepository.findById(Math.toIntExact(id))
                                              .orElseThrow(() -> new NotExistException(URL_IS_NOT_EXISTS_ERROR));
        urlEntity.setLastCheck(lastCheck);
        urlEntity.setLastUpdate(lastUpdate);
        jpaUrlRepository.save(urlEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatEntity> getChats(Long urlId) {
        UrlEntity urlEntity = jpaUrlRepository.findById(Math.toIntExact(urlId))
                                              .orElseThrow(() -> new NotExistException(URL_IS_NOT_EXISTS_ERROR));
        return urlEntity.getChats()
                        .stream()
                        .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse listAll(long tgChatId) {
        ChatEntity chatEntity = jpaTgChatRepository.findByTgChatId(tgChatId)
                                                   .orElseThrow(() -> new NotExistException(CHAT_IS_NOT_EXISTS_ERROR));
        Set<UrlEntity> urlEntities = chatEntity.getUrls();
        return new ListLinksResponse().links(urlEntities.stream()
                                                        .map(urlEntity -> new LinkResponse()
                                                                .id(urlEntity.getId())
                                                                .url(URI.create(urlEntity.getUrl())))
                                                        .toList())
                                      .size(urlEntities.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return jpaUrlRepository.findNotCheckedForLongTime(maxLastCheck);
    }
}
