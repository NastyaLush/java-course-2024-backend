package edu.java.bot.printer;


import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.print.MarkDownPrinter;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MarkDownPrinterTest {
    @Test
    @DisplayName("getMessage should return object SendMessage with Markdown parse mode")
    public void getMessage_shouldReturnSendMessageWithMarkdownMode() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        Map<String, Object> expected = new SendMessage(1L, "2").parseMode(ParseMode.Markdown)
                                                               .getParameters();
        Map<String, Object> actual = markDownPrinter.getMessage(1L, "2")
                                                    .getParameters();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("makeBold should return string type of *text*")
    public void makeBold_shouldReturnBoldMdString() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        String expected = "*text*";
        String actual = markDownPrinter.makeBold("text");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("makeItalic should return string type of _text_")
    public void makeItalic_shouldReturnItalicMdString() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        String expected = "_text_";
        String actual = markDownPrinter.makeItalic("text");

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("makeUrl should return string type of text")
    public void makeUrl_shouldReturnUrlMdString() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        String expected = "text";
        String actual = markDownPrinter.makeURL("text");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("makeMakeUrl should return string type of *text*")
    public void makeUrl_shouldReturnUrlMdStringInlineInText() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        String expected = "[text](url)";
        String actual = markDownPrinter.makeURL("text", "url");

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("makeInlineCode should return string type of `text`")
    public void makeInlineCode_shouldReturnInlineCodeMdString() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        String expected = "`text`";
        String actual = markDownPrinter.makeInlineCode("text");

        Assertions.assertEquals(expected, actual);
    }


    @Test
    @DisplayName("makeFormatedInlineCode should return string type of ```text```")
    public void makeFormatedInlineCode_shouldReturnFormatedInlineCodeMdString() {
        MarkDownPrinter markDownPrinter = new MarkDownPrinter();

        String expected = "```text```";
        String actual = markDownPrinter.makeFormatedInlineCode("text");

        Assertions.assertEquals(expected, actual);
    }

}
