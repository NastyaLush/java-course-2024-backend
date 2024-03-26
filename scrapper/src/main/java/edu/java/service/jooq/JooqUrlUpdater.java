package edu.java.service.jooq;

import edu.java.bot.api.UpdatesApi;
import edu.java.configuration.ApplicationConfig;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import edu.java.util.LinkManager;


public class JooqUrlUpdater extends AbstractUrlUpdater {

    public JooqUrlUpdater(
            JooqUrlService urlService,
            ApplicationConfig applicationConfig,
            UpdatesApi updatesApi,
            LinkManager linkManager
    ) {
        super(urlService, applicationConfig, updatesApi, linkManager);
    }
}
