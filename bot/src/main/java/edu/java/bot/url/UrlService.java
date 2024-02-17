package edu.java.bot.url;

import org.springframework.stereotype.Repository;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Repository
public class UrlService {
    public boolean isUrl(String url){
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
