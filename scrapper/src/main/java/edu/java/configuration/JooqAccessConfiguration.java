package edu.java.configuration;

import edu.java.bot.api.UpdatesApi;
import edu.java.repository.jooq.JooqTgChatRepository;
import edu.java.repository.jooq.JooqTrackingUrlsRepository;
import edu.java.repository.jooq.JooqUrlRepository;
import edu.java.service.TgChatService;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import edu.java.service.jooq.JooqTgChatService;
import edu.java.service.jooq.JooqUrlService;
import edu.java.service.jooq.JooqUrlUpdater;
import edu.java.util.LinkManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public TgChatService tgChatService(
            JooqTgChatRepository jooqTgChatRepository
    ) {
        return new JooqTgChatService(jooqTgChatRepository);
    }

    @Bean
    public UrlService urlService(
            JooqUrlRepository jooqUrlRepository,
            JooqTrackingUrlsRepository jooqTrackingUrlsRepository,
            JooqTgChatRepository jooqTgChatRepository,
            LinkManager linkManager
    ) {
        return new JooqUrlService(jooqUrlRepository, jooqTrackingUrlsRepository, jooqTgChatRepository, linkManager);
    }

    @Bean
    public UrlUpdater urlUpdater(
            ApplicationConfig applicationConfig,
            UpdatesApi updatesApi,
            LinkManager linkManager,
            JooqUrlRepository jooqUrlRepository,
            JooqTrackingUrlsRepository jooqTrackingUrlsRepository,
            JooqTgChatRepository jooqTgChatRepository
    ) {
        return new JooqUrlUpdater(
                (JooqUrlService) urlService(
                        jooqUrlRepository,
                        jooqTrackingUrlsRepository,
                        jooqTgChatRepository,
                        linkManager
                ),
                applicationConfig,
                updatesApi,
                linkManager);
    }
}
