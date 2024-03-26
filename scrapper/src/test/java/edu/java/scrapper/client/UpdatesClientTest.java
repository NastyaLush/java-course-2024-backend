package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.model.LinkUpdate;
import edu.java.client.UpdatesClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
public class UpdatesClientTest {
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();

    @Test
    public void updatesPost_shouldCorrectlyUpdatePost() {
        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(" ")));

        UpdatesClient updatesClient = new UpdatesClient(wireMockExtension.baseUrl() + "/mock");

        assertDoesNotThrow(() -> updatesClient.updatesPost(new LinkUpdate()));

    }

    @Test
    public void updatesPost_shouldThrowExceptionOnError() {
        wireMockExtension.stubFor(WireMock.post("")
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(400)));

        UpdatesClient updatesClient = new UpdatesClient(wireMockExtension.baseUrl());

        assertThrows(WebClientResponseException.class, () -> updatesClient.updatesPost(new LinkUpdate()));

    }


}
