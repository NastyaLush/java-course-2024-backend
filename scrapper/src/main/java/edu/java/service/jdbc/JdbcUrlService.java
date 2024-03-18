package edu.java.service.jdbc;

import edu.java.linkClients.SupportableLinkService;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.service.abstractImplementation.AbstractUrlService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JdbcUrlService extends AbstractUrlService {

    @Autowired
    public JdbcUrlService(
        JdbcUrlRepository urlRepository,
        JdbcTrackingUrlsRepository trackingUrlsRepository,
        JdbcTgChatRepository tgChatRepository,
        List<SupportableLinkService> supportableLinkServices
    ) {
        super(urlRepository, trackingUrlsRepository, tgChatRepository, supportableLinkServices);
    }
}
