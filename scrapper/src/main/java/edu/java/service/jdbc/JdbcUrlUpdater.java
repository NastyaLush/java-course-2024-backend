package edu.java.service.jdbc;

import edu.java.bot.api.UpdatesApi;
import edu.java.configuration.ApplicationConfig;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import edu.java.util.LinkManager;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class JdbcUrlUpdater extends AbstractUrlUpdater {

    public JdbcUrlUpdater(
            JdbcUrlService urlService,
            ApplicationConfig applicationConfig,
            UpdatesApi updatesApi,
            LinkManager linkManager
    ) {
        super(urlService, applicationConfig, updatesApi, linkManager);
    }
}
