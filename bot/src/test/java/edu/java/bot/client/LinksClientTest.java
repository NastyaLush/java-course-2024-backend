package edu.java.bot.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
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
@ContextConfiguration(initializers = LinksClientTest.Initializer.class)
public class LinksClientTest {
    public static final String HEADER_NAME = "Tg-Chat-Id";
    private static final Integer COUNT_OF_ATTEMPTS = 4;
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();
    @Autowired
    LinksClient linksClient;

    @Test
    public void linksPost_successRetry() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                                                      .withDefaultPrettyPrinter();
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.post("/mock")
                                              .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                              .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(addLinkRequest)))
                                              .inScenario("test_linkPost_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        LinkResponse linkResponse = new LinkResponse();
        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                          .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(addLinkRequest)))
                                          .inScenario("test_linkPost_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(objectWriter.writeValueAsString(linkResponse))));

        ResponseEntity<LinkResponse> linkResponseResponseEntity = linksClient.linksPost(1L, addLinkRequest);
        assertEquals(200, linkResponseResponseEntity.getStatusCode()
                                                    .value());

    }

    @Test
    public void linksPost_failedRetry() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                                                      .withDefaultPrettyPrinter();
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.post("/mock")
                                              .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                              .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(addLinkRequest)))
                                              .inScenario("test_linkPost_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.post("/mock")
                                          .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                          .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(addLinkRequest)))
                                          .inScenario("test_linkPost_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)));

        assertThrows(CustomWebClientException.class, () -> linksClient.linksPost(1L, addLinkRequest));

    }

    @Test
    public void linksDelete_successRetry() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                                                      .withDefaultPrettyPrinter();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest();
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.delete("/mock")
                                              .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                              .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(removeLinkRequest)))
                                              .inScenario("test_linkDelete_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }
        LinkResponse linkResponse = new LinkResponse();
        wireMockExtension.stubFor(WireMock.delete("/mock")
                                          .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                          .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(removeLinkRequest)))
                                          .inScenario("test_linkDelete_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(objectWriter.writeValueAsString(linkResponse))));


        ResponseEntity<LinkResponse> linkResponseResponseEntity = linksClient.linksDelete(1L, removeLinkRequest);
        assertEquals(200, linkResponseResponseEntity.getStatusCode()
                                                    .value());

    }

    @Test
    public void linksDelete_failedRetry() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                                                      .withDefaultPrettyPrinter();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest();
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.delete("/mock")
                                              .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                              .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(removeLinkRequest)))
                                              .inScenario("test_linkDelete_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }
        wireMockExtension.stubFor(WireMock.delete("/mock")
                                          .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                          .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(removeLinkRequest)))
                                          .inScenario("test_linkDelete_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)));
        assertThrows(CustomWebClientException.class, () -> linksClient.linksDelete(1L, removeLinkRequest));
    }

    @Test
    public void linksGet_successRetry() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer()
                                                      .withDefaultPrettyPrinter();
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get("/mock")
                                              .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                              .inScenario("test_linkGet_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }
        ListLinksResponse listLinksResponse = new ListLinksResponse();
        wireMockExtension.stubFor(WireMock.get("/mock")
                                          .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                          .inScenario("test_linkGet_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(objectWriter.writeValueAsString(listLinksResponse))));
        assertEquals(200, linksClient.linksGet(1L)
                                     .getStatusCode()
                                     .value());

    }

    @Test
    public void linksGet_failedRetry() {
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get("/mock")
                                              .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                              .inScenario("test_linkGet_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(404))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }
        wireMockExtension.stubFor(WireMock.get("/mock")
                                          .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                                          .inScenario("test_linkGet_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)));
        assertThrows(CustomWebClientException.class, () -> linksClient.linksGet(1L));
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("app.link-client-url=" + wireMockExtension.baseUrl() + "/mock")
                              .and("app.retry-config.max-attempts=" + (COUNT_OF_ATTEMPTS + 1))
                              .and("app.retry-config.delay=1s")
                              .and("app.retry-config.back-off-type=exponential")
                              .and("app.retry-config.retry-ports=404")
                              .applyTo(configurableApplicationContext);
        }
    }
}
