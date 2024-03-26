package edu.java.service.abstractImplementation;

import edu.java.entity.ChatEntity;
import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsInput;
import edu.java.entity.UrlEntity;
import edu.java.entity.UrlInput;
import edu.java.exception.NotExistException;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.repository.TgChatRepository;
import edu.java.repository.TrackingUrlsRepository;
import edu.java.repository.UrlRepository;
import edu.java.service.UrlService;
import edu.java.util.LinkManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class AbstractUrlService implements UrlService {
    private static final String THIS_CHAT_IS_NOT_EXISTS_ERROR = "this chat is not exists";
    private final UrlRepository urlRepository;
    private final TrackingUrlsRepository trackingUrlsRepository;
    private final TgChatRepository tgChatRepository;
    private final LinkManager linkManager;


    @Override
    @Transactional
    public LinkResponse add(long tgChatId, URI url) {
        linkManager.validateURI(url);
        long urlId = urlRepository.add(new UrlInput(url.toString(), OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException(THIS_CHAT_IS_NOT_EXISTS_ERROR);
        }
        trackingUrlsRepository.add(new TrackingUrlsInput(chatEntity.get()
                                                                   .getId(), urlId));
        return new LinkResponse().id(urlId)
                                 .url(url);
    }

    @Override
    @Transactional
    public LinkResponse remove(long tgChatId, URI url) {

        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException(THIS_CHAT_IS_NOT_EXISTS_ERROR);
        }
        Optional<UrlEntity> urlEntity = urlRepository.findByUrl(url.toString());
        if (urlEntity.isEmpty()) {
            throw new NotExistException("this url is not exists");
        }
        trackingUrlsRepository.remove(new TrackingUrlsDelete(chatEntity.get()
                                                                       .getId(), urlEntity.get()
                                                                                          .getId()));
        if (trackingUrlsRepository.findByUrlId(urlEntity.get()
                                                        .getId())
                                  .isEmpty()) {
            urlRepository.remove(url.toString());
        }
        return new LinkResponse().id(urlEntity.get()
                                              .getId())
                                 .url(url);
    }

    @Override
    @Transactional
    public LinkResponse remove(URI url) {
        long id = urlRepository.remove(url.toString());
        return new LinkResponse().id(id)
                                 .url(url);
    }

    @Override
    @Transactional
    public void update(Long id, OffsetDateTime lastCheck) {
        urlRepository.update(id, lastCheck);
    }

    @Override
    @Transactional
    public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {
        urlRepository.update(id, lastCheck, lastUpdate);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatEntity> getChats(Long urlId) {
        return trackingUrlsRepository.findByUrlId(urlId)
                                     .stream()
                                     .map(trackingUrlsDTO -> tgChatRepository.findByTgId(
                                                                                     trackingUrlsDTO.chatId())
                                                                             .get())
                                     .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListLinksResponse listAll(long tgChatId) {
        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException("this chat does not exists");
        }
        List<UrlEntity> urlEntities = trackingUrlsRepository.findByChatId(chatEntity.get()
                                                                                    .getId())
                                                            .stream()
                                                            .map(trackingUrlsDTO ->
                                                                    urlRepository.findById(trackingUrlsDTO.urlId())
                                                                                 .get())
                                                            .toList();
        return new ListLinksResponse().size(urlEntities.size())
                                      .links(urlEntities
                                              .stream()
                                              .map(urlDTO -> new LinkResponse()
                                                      .id(urlDTO.getId())
                                                      .url(URI.create(urlDTO.getUrl())))
                                              .toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return urlRepository.findNotCheckedForLongTime(maxLastCheck);
    }
}
