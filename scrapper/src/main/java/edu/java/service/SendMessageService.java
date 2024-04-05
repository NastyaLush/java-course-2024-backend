package edu.java.service;

import edu.java.bot.api.UpdatesApi;
import edu.java.bot.model.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.service.queue.ScrapperQueueProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final ApplicationConfig applicationConfig;

    private final UpdatesApi updatesApi;

    private final ScrapperQueueProducer scrapperQueueProducer;

    public void send(LinkUpdate linkUpdate) {
        if (applicationConfig.useQueue()) {
            scrapperQueueProducer.send(linkUpdate);
        } else {
            updatesApi.updatesPost(linkUpdate);
        }
    }
}
