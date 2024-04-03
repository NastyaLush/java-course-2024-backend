package edu.java.scrapper.client;

import edu.java.exception.CustomWebClientException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.bot.model.LinkUpdate;
import edu.java.client.UpdatesClient;

import edu.java.scrapper.IntegrationTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ScrapperApplication.class, properties = {"app.scheduler.enable=false"})
@ContextConfiguration(initializers = UpdatesClientTest.Initializer.class)
public class UpdatesClientTest extends IntegrationTest {
    private static final int COUNT_OF_ATTEMPTS = 4;
    @Autowired
    UpdatesClient updatesClient;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("app.client-config.update-url=" + wireMockExtension.baseUrl() + "/mock")
                              .applyTo(configurableApplicationContext);
            TestPropertyValues.of("app.retry-config.max-attempts=" + (COUNT_OF_ATTEMPTS + 1))
                              .applyTo(configurableApplicationContext);
        }
    }


    @Test
    public void updatesPost_successRetry() {
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.post("/mock")
                                              .inScenario("test")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(400)
                                                                  .withBody("{"
                                                                          + "\"message\":\"message\"}")
                                                                  .withHeader("Content-Type", "application/json"))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .inScenario("test")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")));


        ResponseEntity<Void> voidResponseEntity = updatesClient.updatesPost(new LinkUpdate());
        assertEquals(200, voidResponseEntity.getStatusCode()
                                            .value());

    }

    @Test
    public void updatesPost_failedRetry() {
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.post("/mock")
                                              .inScenario("test")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(400)
                                                                  .withBody("{"
                                                                          + "\"message\":\"message\"}")
                                                                  .withHeader("Content-Type", "application/json"))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .inScenario("test")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(400)
                                                              .withBody("{"
                                                                      + "\"message\":\"message\"}")
                                                              .withHeader("Content-Type", "application/json")));


        assertThrows(CustomWebClientException.class, () -> updatesClient.updatesPost(new LinkUpdate()));

    }

}
