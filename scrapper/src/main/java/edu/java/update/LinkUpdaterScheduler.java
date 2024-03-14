package edu.java.update;

import edu.java.configuration.ApplicationConfig;
import edu.java.service.UrlService;
import edu.java.service.UrlUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;

@Log4j2
@EnableScheduling
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final ApplicationConfig.Scheduler scheduler;
    private final UrlService urlService;

    @Scheduled(fixedDelayString = "#{@scheduler.forceCheckDelay}")
    public void update() {
        urlService.findNotCheckedForLongTime(ZonedDateTime.now().minus(scheduler.interval())).forEach(
            urlDTO ->

        );
        log.info("update method");
    }
}
