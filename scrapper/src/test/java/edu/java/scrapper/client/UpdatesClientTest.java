package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.bot.model.LinkUpdate;
import edu.java.client.UpdatesClient;
import edu.java.exceptions.CustomWebClientException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
@SpringBootTest(classes = ScrapperApplication.class)
@ContextConfiguration(initializers = UpdatesClientTest.Initializer.class)
public class UpdatesClientTest {
    @Autowired
    UpdatesClient updatesClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("app.client-config.base-url=" + wireMockExtension.baseUrl() + "/mock")
                              .applyTo(configurableApplicationContext);
        }
    }

    @Test
    public void updatesPost_shouldCorrectlyUpdatePost() {
        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")));

        assertDoesNotThrow(() -> updatesClient.updatesPost(new LinkUpdate()));

    }

    @Test
    public void updatesPost_shouldThrowExceptionOnError() {
        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(400)
                                                              .withHeader("Content-Type", "application/json")));


        assertThrows(CustomWebClientException.class, () -> updatesClient.updatesPost(new LinkUpdate()));

    }

}
