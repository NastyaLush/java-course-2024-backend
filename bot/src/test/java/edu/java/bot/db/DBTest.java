package edu.java.bot.db;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DBTest {

    private DB db;

    @BeforeEach
    void setUp() {
        db = new DB();
    }

    @Test
    void createUser_shouldSuccessfullyCreateUser() {
        ChatDB chatDB = new ChatDB(1L);
        db.createUser(chatDB);
        assertTrue(db.getUrlsByPerson(1L).isEmpty());
    }
    @Test
    void createUser_shouldThrowExceptionIfUserExists() {
        ChatDB chatDB = new ChatDB(1L);
        db.createUser(chatDB);
        assertThrows(IllegalArgumentException.class, ()-> db.createUser(chatDB));
    }

    @Test
    void addUrlToUser_shouldClearlyAddUrlToUser() throws MalformedURLException {
        ChatDB chatDB = new ChatDB(1L);
        db.createUser(chatDB);

        URL url = new URL("http://example.com");
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        db.addUrlToUser(1L, url, zonedDateTime);

        assertEquals(1, db.getUrlsByPerson(1L).size());
        assertEquals(url, db.getUrlsByPerson(1L).get(0));
        assertTrue(db.getUrls().containsKey(url));
        assertEquals(zonedDateTime, db.getUrls().get(url));
        assertEquals(1, db.getUsers(url).size());
        assertEquals(chatDB, db.getUsers(url).get(0));
    }
    @Test
    void addUrlToUser_shouldThrowExceptionIfUserNotExists() throws MalformedURLException {

        URL url = new URL("http://example.com");
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        assertThrows(IllegalArgumentException.class, ()->{db.addUrlToUser(1L, url, zonedDateTime);});
    }
    @Test
    void deleteUrl_shouldWorkCorrectly() throws MalformedURLException {
        ChatDB chatDB = new ChatDB(1L);
        db.createUser(chatDB);

        URL url = new URL("http://example.com");
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        db.addUrlToUser(1L, url, zonedDateTime);

        db.deleteUrl(1L, url);

        assertTrue(db.getUrlsByPerson(1L).isEmpty());
        System.out.println(db.getUsers(url));
        assertTrue(db.getUsers(url).isEmpty());
    }
    @Test
    void deleteUrl_shouldThrowExceptionIfUserNotExist() throws MalformedURLException {

        URL url = new URL("http://example.com");
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        assertThrows(IllegalArgumentException.class, ()->{db.deleteUrl(1l,url);});
    }

    @Test
    void getUrlsByPerson_shouldReturnEmptyListWhenUserNotRegistered() {
        assertEquals(List.of(), db.getUrlsByPerson(1L));
    }

    @Test
    void getUsers_shouldReturnEmptyListWhenNoURL() throws MalformedURLException {
        URL url = new URL("http://example.com");
        assertTrue(db.getUsers(url).isEmpty());
    }
}

