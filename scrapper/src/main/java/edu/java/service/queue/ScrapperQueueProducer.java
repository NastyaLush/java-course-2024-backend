package edu.java.service.queue;

import edu.java.bot.model.LinkUpdate;
import edu.java.configuration.ApplicationConfig;
import edu.java.exception.CustomWebClientException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScrapperQueueProducer {
    private static final int TIMEOUT = 10;
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdate> template;

    public void send(LinkUpdate update) {
        try {

            template.send(applicationConfig.topic()
                                           .name(), update)
                    .get(TIMEOUT, TimeUnit.SECONDS);
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
            log.warn("error when send kafka message");
            throw new CustomWebClientException(e.getMessage());
        }

    }
}
