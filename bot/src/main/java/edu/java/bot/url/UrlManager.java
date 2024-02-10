package edu.java.bot.url;

import java.net.URL;

public class UrlManager {
    private UrlManager() {
    }

    public static String getUrlDomainName(URL url) {
        return url.getHost();
    }
}
