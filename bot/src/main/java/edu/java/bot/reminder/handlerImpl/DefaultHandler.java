package edu.java.bot.reminder.handlerImpl;

import edu.java.bot.annotation.FilterChain;
import edu.java.bot.reminder.Handler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
@FilterChain
@lombok.extern.log4j.Log4j2
public class DefaultHandler implements Handler {

    private Handler children;

    @Override
    public ZonedDateTime handle(URL url) throws IOException {
        if (canHandle(url)) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            long dateTime = connection.getLastModified();
            connection.disconnect();
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateTime), ZoneId.of("GMT"));
        } else {
            if (children != null) {
                return children.handle(url);
            }
            log.info("incorrect url " + url);
            throw new IllegalArgumentException("there is no handler for this url");
        }
    }

    @Override
    public boolean canHandle(URL url) {
        return true;
    }

    @Override
    public Handler setNext(Handler handler) {
        children = handler;
        return handler;
    }
}
