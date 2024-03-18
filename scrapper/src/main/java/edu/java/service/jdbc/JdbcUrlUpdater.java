package edu.java.service.jdbc;

import edu.java.bot.api.UpdatesApi;
import edu.java.configuration.ApplicationConfig;
import edu.java.linkClients.SupportableLinkService;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Log4j2
public class JdbcUrlUpdater extends AbstractUrlUpdater {

    public JdbcUrlUpdater(
        JdbcUrlService urlService,
        ApplicationConfig applicationConfig,
        List<SupportableLinkService> supportableLinkServices,
        UpdatesApi updatesApi
    ) {
        super(urlService, applicationConfig, supportableLinkServices, updatesApi);
    }
}
