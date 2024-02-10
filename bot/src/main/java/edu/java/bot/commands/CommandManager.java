package edu.java.bot.commands;

import edu.java.bot.commands.command.CallableCommand;
import edu.java.bot.commands.command.Command;
import edu.java.bot.printer.Printer;
import edu.java.bot.reflection.Util;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Getter
@lombok.extern.log4j.Log4j2
public class CommandManager {

    private final List<Command> commandList = new ArrayList<>();
    private final List<CallableCommand> callableCommandList = new ArrayList<>();
    private Printer printer;

    @Autowired
    public CommandManager(ApplicationContext applicationContext) {

        Util.findClassAnnotations("edu/java/bot/commands/commandImpl", (annotation, clazz) -> {
            if (annotation.annotationType() == edu.java.bot.annotation.Command.class) {
                commandList.add((Command) applicationContext.getBean(clazz));
            }
            if (annotation.annotationType() == edu.java.bot.annotation.CommandCallable.class) {
                callableCommandList.add((CallableCommand) applicationContext.getBean(clazz));
            }
        });
        Util.findClassAnnotations("edu/java/bot/printer", (annotation, clazz) -> {
            if (annotation.annotationType() == edu.java.bot.annotation.CurrentPrinter.class) {
                if (printer != null) {
                    log.warn(clazz + " and " + printer.getClass()
                             + " have current printer annotation, allowed just for one class");
                    throw new IllegalArgumentException("only one class can have CurrenPrinter annotation");
                }
                printer = (Printer) applicationContext.getBean(clazz);
            }
        });
        if (printer == null) {
            log.warn("there is no class with current print annotation");
            throw new IllegalArgumentException("one class must have CurrenPrinter annotation");
        }


    }

}
