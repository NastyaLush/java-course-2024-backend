package edu.java.service.jooq;

import edu.java.linkClients.SupportableLinkService;
import edu.java.repository.jooq.JooqTgChatRepository;
import edu.java.repository.jooq.JooqTrackingUrlsRepository;
import edu.java.repository.jooq.JooqUrlRepository;
import edu.java.service.abstractImplementation.AbstractUrlService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqUrlService extends AbstractUrlService {
    @Autowired
    public JooqUrlService(
        JooqUrlRepository urlRepository,
        JooqTrackingUrlsRepository trackingUrlsRepository,
        JooqTgChatRepository tgChatRepository,
        List<SupportableLinkService> supportableLinkServices
    ) {
        super(urlRepository, trackingUrlsRepository, tgChatRepository, supportableLinkServices);
    }
}
