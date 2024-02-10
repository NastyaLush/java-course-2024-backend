package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.printer.Printer;

public class DefaultPrinter implements Printer {
    @Override
    public SendMessage getMessage(Long chatId, String message) {
        return new SendMessage(chatId, message);
    }

    @Override
    public String makeBold(String message) {
        return message;
    }

    @Override
    public String makeItalic(String message) {
        return message;
    }

    @Override
    public String makeURL(String message) {
        return null;
    }

    @Override
    public String makeURL(String text, String message) {
        return message;
    }

    @Override
    public String makeInlineCode(String message) {
        return message;
    }

    @Override
    public String makeFormatedInlineCode(String message) {
        return message;
    }
}
