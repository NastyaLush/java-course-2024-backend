package edu.java.bot.commands.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.printer.Printer;
import java.net.MalformedURLException;

public interface CallableCommand extends Command {
    SendMessage callableHandle(Update update, Printer printer) throws MalformedURLException;
}
