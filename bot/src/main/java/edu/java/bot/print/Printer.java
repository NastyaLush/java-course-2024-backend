package edu.java.bot.print;

import com.pengrad.telegrambot.request.SendMessage;

public interface Printer {
    SendMessage getMessage(Long chatId, String message);

    String makeBold(String message);

    String makeItalic(String message);

    String makeURL(String message);

    String makeURL(String text, String message);

    String makeInlineCode(String message);

    String makeFormatedInlineCode(String message);
}
