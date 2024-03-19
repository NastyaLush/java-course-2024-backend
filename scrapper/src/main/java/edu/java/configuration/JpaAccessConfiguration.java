package edu.java.configuration;

import edu.java.bot.api.UpdatesApi;
import edu.java.repository.jpa.JpaTgChatRepository;
import edu.java.repository.jpa.JpaUrlRepository;
import edu.java.service.TgChatService;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaUrlService;
import edu.java.service.jpa.JpaUrlUpdater;
import edu.java.util.LinkManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public TgChatService tgChatService(
            JpaTgChatRepository jooqTgChatRepository
    ) {
        return new JpaChatService(jooqTgChatRepository);
    }

    @Bean
    public UrlService urlService(
            JpaUrlRepository jooqUrlRepository,
            JpaTgChatRepository jooqTgChatRepository,
            LinkManager linkManager
    ) {
        return new JpaUrlService(jooqUrlRepository, jooqTgChatRepository, linkManager);
    }

    @Bean
    public UrlUpdater urlUpdater(
            ApplicationConfig applicationConfig,
            UpdatesApi updatesApi,
            LinkManager linkManager,
            JpaUrlRepository jooqUrlRepository,
            JpaTgChatRepository jooqTgChatRepository
    ) {
        return new JpaUrlUpdater((JpaUrlService) urlService(jooqUrlRepository, jooqTgChatRepository, linkManager),
                applicationConfig,
                updatesApi,
                linkManager);
    }
}
