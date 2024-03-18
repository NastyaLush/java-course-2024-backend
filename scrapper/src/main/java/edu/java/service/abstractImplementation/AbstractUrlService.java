package edu.java.service.abstractImplementation;

import edu.java.entity.ChatEntity;
import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsInput;
import edu.java.entity.UrlEntity;
import edu.java.entity.UrlInput;
import edu.java.exception.NotExistException;
import edu.java.linkClients.SupportableLinkService;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.repository.TgChatRepository;
import edu.java.repository.TrackingUrlsRepository;
import edu.java.repository.UrlRepository;
import edu.java.service.UrlService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class AbstractUrlService implements UrlService {
    public static final String THIS_CHAT_IS_NOT_EXISTS_ERROR = "this chat is not exists";
    private final UrlRepository urlRepository;
    private final TrackingUrlsRepository trackingUrlsRepository;
    private final TgChatRepository tgChatRepository;
    private final List<SupportableLinkService> supportableLinkServices;


    @Override
    @Transactional
    public LinkResponse add(long tgChatId, URI url) {
        Optional<SupportableLinkService> linkService =
            supportableLinkServices.stream().filter(linkClient -> linkClient.getDomain().equals(url.getAuthority()))
                                   .findFirst();
        if (linkService.isEmpty()) {
            throw new IllegalArgumentException("this url is not supported");
        }
        long urlId = urlRepository.add(new UrlInput(url.toString(), OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException(THIS_CHAT_IS_NOT_EXISTS_ERROR);
        }
        trackingUrlsRepository.add(new TrackingUrlsInput(chatEntity.get().id(), urlId));
        return new LinkResponse().id(urlId).url(url);
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
        trackingUrlsRepository.remove(new TrackingUrlsDelete(chatEntity.get().id(), urlEntity.get().id()));
        if (trackingUrlsRepository.findByUrlId(urlEntity.get().id()).isEmpty()) {
            urlRepository.remove(url.toString());
        }
        return new LinkResponse().id(urlEntity.get().id()).url(url);
    }

    @Override
    public LinkResponse remove(URI url) {
        long id = urlRepository.remove(url.toString());
        return new LinkResponse().id(id).url(url);
    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck) {
        urlRepository.update(id, lastCheck);
    }

    @Override
    public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {
        urlRepository.update(id, lastCheck, lastUpdate);

    }

    @Override
    public List<ChatEntity> getChats(Long urlId) {
        return trackingUrlsRepository.findByUrlId(urlId).stream()
                                     .map(trackingUrlsDTO -> tgChatRepository.findByTgId(
                                             trackingUrlsDTO.chatId()).get()).toList();
    }

    @Override
    public ListLinksResponse listAll(long tgChatId) {
        Optional<ChatEntity> chatEntity = tgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException("this chat does not exists");
        }
        List<UrlEntity> urlEntities = trackingUrlsRepository.findByChatId(chatEntity.get().id()).stream()
                                                            .map(trackingUrlsDTO -> urlRepository.findById(
                                                                    trackingUrlsDTO.urlId()).get()).toList();
        return new ListLinksResponse().size(urlEntities.size())
                                      .links(urlEntities
                                          .stream()
                                          .map(urlDTO -> new LinkResponse()
                                              .id(urlDTO.id())
                                              .url(URI.create(urlDTO.url()))).toList());
    }

    @Override
    public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return urlRepository.findNotCheckedForLongTime(maxLastCheck);
    }
}
