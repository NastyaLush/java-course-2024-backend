package edu.java.scrapper.controller;

import edu.java.controller.TgChatController;
import edu.java.exception.NotExistException;
import edu.java.limit.RateLimitServiceImpl;
import edu.java.service.TgChatService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TgChatController.class, properties = "app.client-config.limit=3")
@Import(RateLimitServiceImpl.class)
public class TgChatControllerTest {
    private static final long TG_CHAT_ID = 1;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    TgChatService tgChatService;
    @Autowired
    RateLimitServiceImpl rateLimitService;
    private static final String REMOTE_ADDR = "192.168.0.2";
    private static final int LIMIT = 3;
    @AfterEach
    public void clearBucket() {
        rateLimitService.clearBucket();
    }

    @Test
    void tgChatDelete_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.doNothing()
               .when(tgChatService)
               .unregister(TG_CHAT_ID);

        getDeletePerform().andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturnNotFoundIfNotFountException() throws Exception {
        Mockito.doThrow(new IllegalArgumentException())
               .when(tgChatService)
               .unregister(TG_CHAT_ID);

        getDeletePerform().andExpect(status().is(400));
    }

    @Test
    void delete_shouldReturnTooManyRequestsIfLimitExhausted() throws Exception {
        Mockito.doNothing()
               .when(tgChatService)
               .unregister(TG_CHAT_ID);
        ;
        for (int i = 0; i < LIMIT; i++) {
            getDeletePerform().andExpect(status().isOk());
        }
        getDeletePerform().andExpect(status().is(429));
    }

    @NotNull
    private ResultActions getDeletePerform() throws Exception {
        return mockMvc.perform(delete("/tg-chat/{id}", TG_CHAT_ID)
                .with(request -> {
                    request.setRemoteAddr(REMOTE_ADDR);
                    return request;
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
    @Test
    void post_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.doNothing()
               .when(tgChatService)
               .register(TG_CHAT_ID);

        getPostPerform().andExpect(status().isOk());
    }

    @Test
    void post_shouldReturnNotFoundIfNotFountException() throws Exception {
        Mockito.doThrow(new NotExistException("haha"))
               .when(tgChatService)
               .register(TG_CHAT_ID);

        getPostPerform().andExpect(status().is(400));
    }

    @Test
    void post_shouldReturnTooManyRequestsIfLimitExhausted() throws Exception {
        Mockito.doNothing()
               .when(tgChatService)
               .register(TG_CHAT_ID);
        ;
        for (int i = 0; i < LIMIT; i++) {
            getPostPerform().andExpect(status().isOk());
        }
        getPostPerform().andExpect(status().is(429));
    }

    @NotNull
    private ResultActions getPostPerform() throws Exception {
        return mockMvc.perform(post("/tg-chat/{id}", TG_CHAT_ID)
                .with(request -> {
                    request.setRemoteAddr(REMOTE_ADDR);
                    return request;
                })
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }
}
