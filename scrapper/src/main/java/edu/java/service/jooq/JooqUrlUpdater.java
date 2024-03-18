package edu.java.service.jooq;

import edu.java.bot.api.UpdatesApi;
import edu.java.configuration.ApplicationConfig;
import edu.java.linkClients.SupportableLinkService;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JooqUrlUpdater extends AbstractUrlUpdater {
    @Autowired
    public JooqUrlUpdater(
        JooqUrlService urlService,
        ApplicationConfig applicationConfig,
        List<SupportableLinkService> supportableLinkServices,
        UpdatesApi updatesApi
    ) {
        super(urlService, applicationConfig, supportableLinkServices, updatesApi);
    }
}
