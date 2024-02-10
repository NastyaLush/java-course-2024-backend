package edu.java.bot.db;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@lombok.extern.log4j.Log4j2
@Component
public class DB {
    private final Map<Long, ChatDB> persons = new HashMap<>();
    private final Map<URL, List<ChatDB>> urlPerson = new HashMap<>();
    private final Map<Long, List<URL>> personUrl = new HashMap<>();
    private final Map<URL, ZonedDateTime> urlModify = new HashMap<>();

    public void createUser(ChatDB chatDB) {
        if (persons.containsKey(chatDB.chatId())) {
            log.info("user " + chatDB.chatId() + " already registered");
            throw new IllegalArgumentException("you are already registered");
        }
        personUrl.put(chatDB.chatId(), new ArrayList<>());
        persons.put(chatDB.chatId(), chatDB);
    }

    public void addUrlToUser(Long chatId, URL url, ZonedDateTime zonedDateTime) {
        log.info("add " + url + " to " + chatId);
        if (!persons.containsKey(chatId)) {
            throw new IllegalArgumentException("you are not registered, please type /start");
        }
        if (!urlPerson.containsKey(url)) {
            urlModify.put(url, zonedDateTime);
        }
        if (!urlPerson.containsKey(url)) {
            List<ChatDB> chatDBList = new ArrayList<>();
            chatDBList.add(persons.get(chatId));
            urlPerson.put(url, chatDBList);
        } else {
            urlPerson.get(url)
                     .add(persons.get(chatId));
        }
        if (!personUrl.containsKey(chatId)) {
            List<URL> urlList = new ArrayList<>();
            urlList.add(url);
            personUrl.put(chatId, urlList);
        } else {
            personUrl.get(chatId)
                     .add(url);
        }
    }

    public void deleteUrl(Long chatId, URL url) {
        log.info("delete " + url + " from " + chatId);
        if (!urlPerson.containsKey(url)) {
            throw new IllegalArgumentException("there is no this url");
        }
        urlPerson.get(url)
                 .remove(persons.get(chatId));
        if (urlPerson.get(url)
                     .isEmpty()) {
            urlModify.remove(url);
        }
        personUrl.get(chatId)
                 .remove(url);

    }

    public List<URL> getUrlsByPerson(Long tgID) {
        if (personUrl.get(tgID) != null) {
            return personUrl.get(tgID);
        } else {
            return new ArrayList<>();
        }
    }

    public Map<URL, ZonedDateTime> getUrls() {
        return Map.copyOf(urlModify);
    }

    public List<ChatDB> getUsers(URL url) {
        List<ChatDB> chatDBS = urlPerson.get(url);
        if (chatDBS == null) {
            return List.of();
        }
        return List.copyOf(chatDBS);
    }

}
