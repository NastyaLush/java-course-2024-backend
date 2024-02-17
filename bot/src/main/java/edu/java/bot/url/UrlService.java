package edu.java.bot.url;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Repository;

@Repository
public class UrlService {
    public boolean isUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }

    }
}
