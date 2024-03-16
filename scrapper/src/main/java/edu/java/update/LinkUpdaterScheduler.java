package edu.java.update;

import edu.java.client.UpdatesClient;
import edu.java.service.UrlUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@EnableScheduling
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final UrlUpdater urlUpdater;

    @Scheduled(fixedDelayString = "#{@scheduler.forceCheckDelay}")
    public void update() {
        urlUpdater.update();
        log.info("update method");
    }
}
