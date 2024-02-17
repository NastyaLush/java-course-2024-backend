package edu.java.bot.print;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class MarkDownPrinter implements Printer {
    @Override
    public SendMessage getMessage(Long chatId, String message) {
        return new SendMessage(chatId, message).parseMode(ParseMode.Markdown);
    }

    @Override
    public String makeBold(String message) {
        return "*%s*".formatted(message);
    }

    @Override
    public String makeItalic(String message) {
        return "_%s_".formatted(message);
    }

    @Override
    public String makeURL(String url) {
        return url;
    }

    @Override
    public String makeURL(String text, String url) {
        return "[%s](%s)".formatted(text, url);
    }

    @Override
    public String makeInlineCode(String message) {
        return "`%s`".formatted(message);
    }

    @Override
    public String makeFormatedInlineCode(String message) {
        return "```%s```".formatted(message);
    }
}
