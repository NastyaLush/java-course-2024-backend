package edu.java.service.jooq;

import edu.java.repository.jooq.JooqTgChatRepository;
import edu.java.repository.jooq.JooqTrackingUrlsRepository;
import edu.java.repository.jooq.JooqUrlRepository;
import edu.java.service.abstractImplementation.AbstractUrlService;
import edu.java.util.LinkManager;


public class JooqUrlService extends AbstractUrlService {

    public JooqUrlService(
            JooqUrlRepository urlRepository,
            JooqTrackingUrlsRepository trackingUrlsRepository,
            JooqTgChatRepository tgChatRepository,
            LinkManager linkManager
    ) {
        super(urlRepository, trackingUrlsRepository, tgChatRepository, linkManager);
    }
}
