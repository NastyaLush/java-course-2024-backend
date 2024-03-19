package edu.java.service.jdbc;

import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.service.abstractImplementation.AbstractUrlService;
import edu.java.util.LinkManager;

public class JdbcUrlService extends AbstractUrlService {

    public JdbcUrlService(JdbcUrlRepository urlRepository, JdbcTrackingUrlsRepository trackingUrlsRepository, JdbcTgChatRepository tgChatRepository, LinkManager linkManager) {
        super(urlRepository, trackingUrlsRepository, tgChatRepository, linkManager);
    }
}
