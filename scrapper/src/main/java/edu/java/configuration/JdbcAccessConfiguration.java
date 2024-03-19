package edu.java.configuration;

import edu.java.bot.api.UpdatesApi;
import edu.java.repository.jdbc.JdbcTgChatRepository;
import edu.java.repository.jdbc.JdbcTrackingUrlsRepository;
import edu.java.repository.jdbc.JdbcUrlRepository;
import edu.java.service.TgChatService;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import edu.java.service.jdbc.JdbcTgChatService;
import edu.java.service.jdbc.JdbcUrlService;
import edu.java.service.jdbc.JdbcUrlUpdater;
import edu.java.util.LinkManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public TgChatService tgChatService(
            JdbcTgChatRepository jdbcTgChatRepository
    ) {
        return new JdbcTgChatService(
                jdbcTgChatRepository
        );
    }

    @Bean
    public UrlService urlService(
            JdbcUrlRepository jdbcUrlRepository,
            JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository,
            JdbcTgChatRepository jdbcTgChatRepository,
            LinkManager linkManager
    ) {
        return new JdbcUrlService(jdbcUrlRepository, jdbcTrackingUrlsRepository, jdbcTgChatRepository, linkManager);
    }

    @Bean
    public UrlUpdater urlUpdater(
            ApplicationConfig applicationConfig,
            UpdatesApi updatesApi,
            LinkManager linkManager,
            JdbcUrlRepository jdbcUrlRepository,
            JdbcTrackingUrlsRepository jdbcTrackingUrlsRepository,
            JdbcTgChatRepository jdbcTgChatRepository
    ) {
        return new JdbcUrlUpdater((JdbcUrlService) urlService(jdbcUrlRepository, jdbcTrackingUrlsRepository, jdbcTgChatRepository, linkManager), applicationConfig, updatesApi, linkManager);
    }
}
