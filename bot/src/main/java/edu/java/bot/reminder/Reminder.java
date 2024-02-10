package edu.java.bot.reminder;

import edu.java.bot.db.ChatDB;
import edu.java.bot.db.DB;
import edu.java.bot.tgBot.BotOperations;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@lombok.extern.log4j.Log4j2
public class Reminder {
    private final ApplicationContext applicationContext;
    private final DB db;
    private final BotOperations botOperations;
    private Handler handler;

    @Autowired
    public Reminder(ApplicationContext applicationContext, DB db, BotOperations botOperations) {
        this.applicationContext = applicationContext;
        this.db = db;
        this.botOperations = botOperations;
    }

    @PostConstruct
    public void checkUpdates() {
        edu.java.bot.reflection.Util.findClassAnnotations("edu/java/bot/reminder/handlerImpl", (annotation, clazz) -> {
            if (annotation.annotationType() == edu.java.bot.annotation.FilterChain.class) {
                if (handler == null) {
                    handler = (Handler) applicationContext.getBean(clazz);
                } else {
                    handler.setNext((Handler) applicationContext.getBean(clazz));
                }
            }
        });
        Thread thread = new Thread(() -> {
            while (true) {
                db.getUrls()
                  .forEach((key, value) -> {
                      try {
                          if (checkUpdates(key) != value) {
                              botOperations.remind(key, db.getUsers(key));
                          }
                      } catch (IOException e) {
                          throw new RuntimeException(e);
                      }
                  });
            }
        });
        thread.start();
    }

    public void addUrl(Long chatId, URL url) {
        try {
            db.addUrlToUser(chatId, url, handler.handle(url));
        } catch (IOException e) {
            log.info("url error: " + e.getMessage());
            throw new IllegalArgumentException("something went wrong, please check url and network connection");
        }
    }

    public void deleteUrl(Long chatId, URL url) {
        db.deleteUrl(chatId, url);
    }

    public void createUser(ChatDB chatDB) {
        db.createUser(chatDB);
    }

    public List<URL> getUrls(Long chatId) {
        return db.getUrlsByPerson(chatId);
    }

    public ZonedDateTime checkUpdates(URL url) throws IOException {
        return handler.handle(url);
    }


}
