package edu.java.service.jdbc;

import edu.java.configuration.ApplicationConfig;
import edu.java.service.SendMessageService;
import edu.java.service.abstractImplementation.AbstractUrlUpdater;
import edu.java.util.LinkManager;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class JdbcUrlUpdater extends AbstractUrlUpdater {

    public JdbcUrlUpdater(
            JdbcUrlService urlService,
            ApplicationConfig applicationConfig,
            SendMessageService service,
            LinkManager linkManager
    ) {
        super(urlService, applicationConfig, service, linkManager);
    }
}
