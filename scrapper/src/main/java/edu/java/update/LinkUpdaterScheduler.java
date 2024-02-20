package edu.java.update;

import edu.java.configuration.ApplicationConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Log4j2
@EnableScheduling
@Component
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@Sheduler.forceCheckDelay}")
    public void update(){
        log.info("update method");
    }
}
