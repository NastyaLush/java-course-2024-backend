package edu.java.bot.client;

import com.example.exceptions.CustomWebClientException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TgChatClientTest.Initializer.class)
public class TgChatClientTest {
    private static final long ID = 1L;
    private static final Integer COUNT_OF_ATTEMPTS = 4;
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();
    @Autowired
    TgChatClient tgChatClient;

    @Test
    public void tgChatIdDelete_successRetry() {

        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathTemplate("/mock/{ids}"))
                                              .withPathParam("ids", WireMock.equalTo("1"))
                                              .inScenario("test_tgChatIdDelete_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathTemplate("/mock/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo("1"))
                                          .withPathParam("ids", WireMock.equalTo("1"))
                                          .inScenario("test_tgChatIdDelete_success")
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(" ")));

        ResponseEntity<Void> voidResponseEntity = tgChatClient.tgChatIdDelete(ID);
        assertEquals(200, voidResponseEntity.getStatusCode()
                                            .value());

    }

    @Test
    public void tgChatIdDelete_failedRetry() {
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathTemplate("/mock/{ids}"))
                                              .withPathParam("ids", WireMock.equalTo("1"))
                                              .inScenario("test_tgChatIdDelete_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.delete(WireMock.urlPathTemplate("/mock/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo("1"))
                                          .inScenario("test_tgChatIdDelete_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)));


        assertThrows(CustomWebClientException.class, () -> tgChatClient.tgChatIdDelete(ID));

    }

    @Test
    public void tgChatIdPost_successRetry() {

        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.post(WireMock.urlPathTemplate("/mock/{ids}"))
                                              .withPathParam("ids", WireMock.equalTo("1"))
                                              .inScenario("test_tgChatIdPost_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathTemplate("/mock/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo("1"))
                                          .inScenario("test_tgChatIdPost_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(" ")));

        ResponseEntity<Void> voidResponseEntity = tgChatClient.tgChatIdPost(ID);
        assertEquals(200, voidResponseEntity.getStatusCode()
                                            .value());

    }

    @Test
    public void tgChatIdPost_failedRetry() {
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.post(WireMock.urlPathTemplate("/mock/{ids}"))
                                              .withPathParam("ids", WireMock.equalTo("1"))
                                              .inScenario("test_tgChatIdPost_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.post(WireMock.urlPathTemplate("/mock/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo("1"))
                                          .withPathParam("ids", WireMock.equalTo("1"))
                                          .inScenario("test_tgChatIdPost_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)));


        assertThrows(CustomWebClientException.class, () -> tgChatClient.tgChatIdPost(ID));

    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("app.tg-chat-client-url=" + wireMockExtension.baseUrl() + "/mock")
                              .and("app.retry-config.max-attempts=" + (COUNT_OF_ATTEMPTS + 1))
                              .and("app.retry-config.delay=1s")
                              .and("app.retry-config.back-off-type=linear")
                              .and("app.retry-config.retry-ports=404")
                              .applyTo(configurableApplicationContext);
        }
    }
}
