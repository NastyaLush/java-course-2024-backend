package edu.java.service.jooq;

import edu.java.configuration.ApplicationConfig;
import edu.java.service.SendMessageService;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import edu.java.util.LinkManager;


public class JooqUrlUpdater extends AbstractUrlUpdater {

    public JooqUrlUpdater(
            JooqUrlService urlService,
            ApplicationConfig applicationConfig,
            SendMessageService service,
            LinkManager linkManager
    ) {
        super(urlService, applicationConfig, service, linkManager);
    }
}
