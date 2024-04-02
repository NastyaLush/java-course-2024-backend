package edu.java.service.jpa;

import edu.java.bot.api.UpdatesApi;
import edu.java.configuration.ApplicationConfig;
import edu.java.service.UrlService;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import edu.java.util.LinkManager;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class JpaUrlUpdater extends AbstractUrlUpdater {

    public JpaUrlUpdater(UrlService urlService, ApplicationConfig applicationConfig, UpdatesApi updatesApi,
                         LinkManager linkManager) {
        super(urlService, applicationConfig, updatesApi, linkManager);
    }
}
