package edu.java.scrapper.controller;

import edu.java.controller.LinksController;
import edu.java.exception.AlreadyExistException;
import edu.java.exception.NotExistException;
import edu.java.limit.RateLimitServiceImpl;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import edu.java.service.UrlService;
import java.net.URI;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = LinksController.class, properties = "app.client-config.limit=3")
@Import(RateLimitServiceImpl.class)
public class LinksControllerTest {
    private static final long TG_CHAT_ID = 1;
    private static final String URL = "url";
    private static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UrlService urlService;
    @Autowired
    RateLimitServiceImpl rateLimitService;
    private static final String REMOTE_ADDR = "192.168.0.2";
    private static final int LIMIT = 3;

    @AfterEach
    public void clearBucket() {
        rateLimitService.clearBucket();
    }


    @Test
    void delete_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.when(urlService.remove(TG_CHAT_ID, URI.create(URL)))
               .thenReturn(Mockito.mock(LinkResponse.class));

        getDeletePerform().andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturnNotFoundIfNotFountException() throws Exception {
        Mockito.when(urlService.remove(TG_CHAT_ID, URI.create(URL)))
               .thenThrow(new ResourceNotFoundException("haha"));

        getDeletePerform().andExpect(status().is(404));
    }

    @Test
    void delete_shouldReturnTooManyRequestsIfLimitExhausted() throws Exception {

        OngoingStubbing<LinkResponse> stubbing = Mockito.when(urlService.remove(TG_CHAT_ID, URI.create(URL)));

        for (int i = 0; i < LIMIT; i++) {
            stubbing = stubbing.thenReturn(Mockito.mock(LinkResponse.class));
            getDeletePerform().andExpect(status().isOk());
        }
        stubbing.thenReturn(Mockito.mock(LinkResponse.class));
        getDeletePerform().andExpect(status().is(429));
    }


    @Test
    void get_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.when(urlService.listAll(TG_CHAT_ID))
               .thenReturn(Mockito.mock(ListLinksResponse.class));

        getGetPerform().andExpect(status().isOk());
    }

    @Test
    void get_shouldReturnNotFoundIfNotFountException() throws Exception {
        Mockito.when(urlService.listAll(TG_CHAT_ID))
               .thenThrow(new AlreadyExistException("haha"));

        getGetPerform().andExpect(status().is(400));
    }

    @Test
    void get_shouldReturnTooManyRequestsIfLimitExhausted() throws Exception {

        OngoingStubbing<ListLinksResponse> stubbing = Mockito.when(urlService.listAll(TG_CHAT_ID));

        for (int i = 0; i < LIMIT; i++) {
            stubbing = stubbing.thenReturn(Mockito.mock(ListLinksResponse.class));
            getGetPerform().andExpect(status().isOk());
        }
        stubbing.thenReturn(Mockito.mock(ListLinksResponse.class));
        getGetPerform().andExpect(status().is(429));
    }

    @Test
    void post_shouldReturnOkForCorrectRequest() throws Exception {
        Mockito.when(urlService.add(TG_CHAT_ID, URI.create(URL)))
               .thenReturn(Mockito.mock(LinkResponse.class));

        postGetPerform().andExpect(status().isOk());
    }

    @Test
    void post_shouldReturnNotFoundIfNotFountException() throws Exception {
        Mockito.when(urlService.add(TG_CHAT_ID, URI.create(URL)))
               .thenThrow(new NotExistException("haha"));

        postGetPerform().andExpect(status().is(400));
    }

    @Test
    void post_shouldReturnTooManyRequestsIfLimitExhausted() throws Exception {

        OngoingStubbing<LinkResponse> stubbing = Mockito.when(urlService.add(TG_CHAT_ID, URI.create(URL)));

        for (int i = 0; i < LIMIT; i++) {
            stubbing = stubbing.thenReturn(Mockito.mock(LinkResponse.class));
            postGetPerform().andExpect(status().isOk());
        }
        stubbing.thenReturn(Mockito.mock(LinkResponse.class));
        postGetPerform().andExpect(status().is(429));
    }

    @NotNull
    private ResultActions postGetPerform() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AddLinkRequest addLinkRequest = new AddLinkRequest().link(URI.create(URL));
        return mockMvc.perform(post("/links").with(request -> {
                                                 request.setRemoteAddr(REMOTE_ADDR);
                                                 return request;
                                             })
                                             .header(TG_CHAT_ID_HEADER, TG_CHAT_ID)
                                             .content(objectMapper.writeValueAsString(addLinkRequest))
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getDeletePerform() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest().link(URI.create(URL));
        return mockMvc.perform(delete("/links").with(request -> {
                                                   request.setRemoteAddr(REMOTE_ADDR);
                                                   return request;
                                               })
                                               .header(TG_CHAT_ID_HEADER, TG_CHAT_ID)
                                               .content(objectMapper.writeValueAsString(removeLinkRequest))
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .accept(MediaType.APPLICATION_JSON));
    }

    @NotNull
    private ResultActions getGetPerform() throws Exception {
        return mockMvc.perform(get("/links").with(request -> {
                                                request.setRemoteAddr(REMOTE_ADDR);
                                                return request;
                                            })
                                            .header(TG_CHAT_ID_HEADER, TG_CHAT_ID)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON));
    }
}
