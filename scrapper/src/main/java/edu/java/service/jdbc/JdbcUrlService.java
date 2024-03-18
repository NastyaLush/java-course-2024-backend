package edu.java.service.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.entity.TrackingUrlsDelete;
import edu.java.entity.TrackingUrlsInput;
import edu.java.entity.UrlEntity;
import edu.java.entity.UrlInput;
import edu.java.exception.NotExistException;
import edu.java.linkClients.SupportableLinkService;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.service.UrlService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class JdbcUrlService implements UrlService {
    public static final String THIS_CHAT_IS_NOT_EXISTS_ERROR = "this chat is not exists";
    private final JdbcUrlRepository jdbcUrlRepository;
    private final JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository;
    private final JdbcTgChatRepository jdbcTgChatRepository;
    private final List<SupportableLinkService> supportableLinkServices;

    @Autowired public JdbcUrlService(
        JdbcUrlRepository jdbcUrlRepository,
        JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository,
        JdbcTgChatRepository jdbcTgChatRepository,
        List<SupportableLinkService> supportableLinkServices
    ) {
        this.jdbcUrlRepository = jdbcUrlRepository;
        this.jdbcTrackingUrlsRepository = jdbcTrackingUrlsRepository;
        this.jdbcTgChatRepository = jdbcTgChatRepository;
        this.supportableLinkServices = supportableLinkServices;
    }

    @Override @Transactional public LinkResponse add(long tgChatId, URI url) {
        Optional<SupportableLinkService> linkService =
            supportableLinkServices.stream().filter(linkClient -> linkClient.getDomain().equals(url.getAuthority()))
                                   .findFirst();
        if (linkService.isEmpty()) {
            throw new IllegalArgumentException("this url is not supported");
        }
        long urlId = jdbcUrlRepository.add(new UrlInput(url.toString(), OffsetDateTime.now(), OffsetDateTime.now()));
        Optional<ChatEntity> chatEntity = jdbcTgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException(THIS_CHAT_IS_NOT_EXISTS_ERROR);
        }
        jdbcTrackingUrlsRepository.add(new TrackingUrlsInput(chatEntity.get().id(), urlId));
        return new LinkResponse().id(urlId).url(url);
    }

    @Override @Transactional public LinkResponse remove(long tgChatId, URI url) {

        Optional<ChatEntity> chatEntity = jdbcTgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException(THIS_CHAT_IS_NOT_EXISTS_ERROR);
        }
        Optional<UrlEntity> urlEntity = jdbcUrlRepository.findByUrl(url.toString());
        if (urlEntity.isEmpty()) {
            throw new NotExistException("this url is not exists");
        }
        jdbcTrackingUrlsRepository.remove(new TrackingUrlsDelete(chatEntity.get().id(), urlEntity.get().id()));
        if (jdbcTrackingUrlsRepository.findByUrlId(urlEntity.get().id()).isEmpty()) {
            jdbcUrlRepository.remove(url.toString());
        }
        return new LinkResponse().id(urlEntity.get().id()).url(url);
    }

    @Override public LinkResponse remove(URI url) {
        long id = jdbcUrlRepository.remove(url.toString());
        return new LinkResponse().id(id).url(url);
    }

    @Override public void update(Long id, OffsetDateTime lastCheck) {
        jdbcUrlRepository.update(id, lastCheck);
    }

    @Override public void update(Long id, OffsetDateTime lastCheck, OffsetDateTime lastUpdate) {
        jdbcUrlRepository.update(id, lastCheck, lastUpdate);

    }

    @Override public List<ChatEntity> getChats(Long urlId) {
        return jdbcTrackingUrlsRepository.findByUrlId(urlId)
                                         .stream()
                                         .map(trackingUrlsDTO -> {
                                             Long chatTgId = trackingUrlsDTO.chatId();
                                             return jdbcTgChatRepository.findByTgId(chatTgId).get();
                                         })
                                         .toList();
    }

    @Override public ListLinksResponse listAll(long tgChatId) {
        Optional<ChatEntity> chatEntity = jdbcTgChatRepository.findByTgId(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new NotExistException("this chat does not exists");
        }
        List<UrlEntity> urlEntities = jdbcTrackingUrlsRepository.findByChatId(chatEntity.get().id()).stream()
                                                                .map(trackingUrlsDTO -> jdbcUrlRepository.findById(
                                                                    trackingUrlsDTO.urlId()).get()).toList();
        return new ListLinksResponse()
            .size(urlEntities.size())
            .links(urlEntities.stream()
                              .map(urlDTO -> new LinkResponse().id(urlDTO.id()).url(URI.create(urlDTO.url())))
                              .toList());
    }

    @Override public List<UrlEntity> findNotCheckedForLongTime(OffsetDateTime maxLastCheck) {
        return jdbcUrlRepository.findNotCheckedForLongTime(maxLastCheck);
    }
}
