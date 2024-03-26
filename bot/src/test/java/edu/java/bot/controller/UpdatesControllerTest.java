package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.bot.BotListenerImpl;
import edu.java.bot.limit.RateLimitService;
import edu.java.bot.limit.RateLimitServiceImpl;
import edu.java.bot.model.LinkUpdate;
import edu.java.bot.print.Printer;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UpdatesController.class, properties = "app.client-config.limit=3")
@Import(RateLimitServiceImpl.class)
public class UpdatesControllerTest {
    private static final int LIMIT = 3;
    private static final String REMOTE_ADDR_1 = "192.168.0.2";
    private static final String REMOTE_ADDR_2 = "192.168.0.1";
    @Autowired
    public RateLimitService rateLimitService;
    @MockBean
    BotListenerImpl botListener;
    @MockBean
    Printer printer;
    @Autowired
    MockMvc mockMvc;

    @Test
    void post_shouldReturnOkForCorrectRequest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LinkUpdate
                linkUpdateRequest = new LinkUpdate().id(1L)
                                                    .url(URI.create("url"))
                                                    .description("smth")
                                                    .tgChatIds(List.of(1l));
        Mockito.doNothing()
               .when(botListener)
               .execute(Mockito.any());
        Mockito.when(printer.getMessage(Mockito.any(), Mockito.any()))
               .thenReturn(Mockito.mock(SendMessage.class));

        mockMvc.perform(post("/updates")
                       .with(request -> {
                           request.setRemoteAddr(REMOTE_ADDR_1);
                           return request;
                       })
                       .content(objectMapper.writeValueAsString(linkUpdateRequest))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    void post_shouldReturnErrorIfLimit() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LinkUpdate
                linkUpdateRequest = new LinkUpdate().id(1L)
                                                    .url(URI.create("url"))
                                                    .description("smth")
                                                    .tgChatIds(List.of(1l));
        Mockito.doNothing()
               .when(botListener)
               .execute(Mockito.any());
        Mockito.when(printer.getMessage(Mockito.any(), Mockito.any()))
               .thenReturn(Mockito.mock(SendMessage.class));

        for (int i = 0; i < LIMIT; i++) {
            mockMvc.perform(post("/updates")
                           .with(request -> {
                               request.setRemoteAddr(REMOTE_ADDR_2);
                               return request;
                           })
                           .content(objectMapper.writeValueAsString(linkUpdateRequest))
                           .contentType(MediaType.APPLICATION_JSON)
                           .accept(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk());
        }
        mockMvc.perform(post("/updates")
                       .with(request -> {
                           request.setRemoteAddr(REMOTE_ADDR_2);
                           return request;
                       })
                       .content(objectMapper.writeValueAsString(linkUpdateRequest))
                       .contentType(MediaType.APPLICATION_JSON)
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().is(429));
    }
}
