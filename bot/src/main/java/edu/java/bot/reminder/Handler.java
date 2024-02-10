package edu.java.bot.reminder;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;

public interface Handler {
    ZonedDateTime handle(URL url) throws IOException;

    boolean canHandle(URL url);

    Handler setNext(Handler handler);

}
