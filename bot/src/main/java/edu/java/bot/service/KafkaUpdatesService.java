package edu.java.bot.service;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.model.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaUpdatesService {
    private final UpdatesService updatesService;
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(id = "id", topics = "${app.topic.name}", containerFactory = "linkUpdateKafkaListenerFactory")
    public void listen(LinkUpdate update) {
        log.info("получено сообщение {}", update);
        try {
            updatesService.updatesPost(update);
        } catch (Exception e) {
            log.warn("ошибочное сообщение {}", e.getMessage());
            kafkaTemplate.send(applicationConfig.deadTopic()
                                                .name(), e.getMessage());
        }
    }
}
