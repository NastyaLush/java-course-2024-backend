package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.TgChatClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
public class TgChatClientTest {
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @Test
    public void tgChatIdDelete_shouldWorkCorrectlyIfSuccess() {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathTemplate("/mock/{ids}"))
                .withPathParam("ids", WireMock.equalTo("1"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(" ")));
        TgChatClient tgChatClient = new TgChatClient(wireMockExtension.baseUrl()+"/mock");
        assertDoesNotThrow(() -> tgChatClient.tgChatIdDelete(1L));
    }
    @Test
    public void tgChatIdDelete_shouldThrowErrorIf404() {
        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathTemplate("/mock/{ids}"))
                .withPathParam("ids", WireMock.equalTo("1"))
                .willReturn(WireMock.aResponse().withStatus(404)));
        TgChatClient tgChatClient = new TgChatClient(wireMockExtension.baseUrl()+"/mock");
        assertThrows(WebClientResponseException.class,() -> tgChatClient.tgChatIdDelete(1L));
    }

    @Test
    public void tgChatIdPost_shouldWorkCorrectlyIfSuccess() {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathTemplate("/mock/{ids}"))
                .withPathParam("ids", WireMock.equalTo("1"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(" ")));
        TgChatClient tgChatClient = new TgChatClient(wireMockExtension.baseUrl()+"/mock");
        assertDoesNotThrow(() -> tgChatClient.tgChatIdPost(1L));
    }
    @Test
    public void tgChatIdPost_shouldThrowErrorIf404() {
        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathTemplate("/mock/{ids}"))
                .withPathParam("ids", WireMock.equalTo("1"))
                .willReturn(WireMock.aResponse().withStatus(404)));
        TgChatClient tgChatClient = new TgChatClient(wireMockExtension.baseUrl()+"/mock");
        assertThrows(WebClientResponseException.class, () -> tgChatClient.tgChatIdPost(1L));
    }
}
