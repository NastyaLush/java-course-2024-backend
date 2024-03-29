package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.model.LinkUpdate;
import edu.java.client.UpdatesClient;
import edu.java.exceptions.CustomWebClientException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
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
                                                              .withHeader("Content-Type", "application/json")));

        UpdatesClient updatesClient = new UpdatesClient(wireMockExtension.baseUrl() + "/mock");

        assertDoesNotThrow(() -> updatesClient.updatesPost(new LinkUpdate()));

    }

    @Test
    public void updatesPost_shouldThrowExceptionOnError() {
        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(400)
                                                              .withHeader("Content-Type", "application/json")));

        UpdatesClient updatesClient = new UpdatesClient(wireMockExtension.baseUrl() + "/mock");

        assertThrows(CustomWebClientException.class, () -> updatesClient.updatesPost(new LinkUpdate()));

    }

}
