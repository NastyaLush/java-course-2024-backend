package edu.java.scrapper.client.stackoverflow;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.client.linkClients.stackoverflow.StackoverflowServiceSupportable;
import edu.java.client.linkClients.stackoverflow.dto.AnswerResponse;
import edu.java.client.linkClients.stackoverflow.dto.QuestionResponse;
import edu.java.exceptions.CustomWebClientException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ScrapperApplication.class, properties = {"app.scheduler.enable=false"})
@ContextConfiguration(initializers = StackoverflowImplTest.Initializer.class)
public class StackoverflowImplTest {
    @Autowired
    StackoverflowServiceSupportable stackoverflowClient;
    private static final int COUNT_OF_ATTEMPTS = 4;
    private static final List<Integer> questions = List.of(78027826, 8968641);
    private static final Path pathToCorrectQuestionExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/stackoverflow/testData/correctResponseQuestionExample.txt");
    private static final Path pathToCorrectAnswerExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/stackoverflow/testData/correctResponseQuestionExample.txt");
    private static final Path pathToIncorrectExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/stackoverflow/testData/incorrectResponseExample.txt");
    private static final Path pathToFailedExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/stackoverflow/testData/failedResponseExample.txt");
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("app.client-config.stackoverflow.api-url=" + wireMockExtension.baseUrl())
                              .applyTo(configurableApplicationContext);
            TestPropertyValues.of("app.retry-config.max-attempts=" + (COUNT_OF_ATTEMPTS + 1))
                              .applyTo(configurableApplicationContext);
        }
    }

    @Test
    public void getQuestions_shouldReturnEmptyListIfNoAnswerWithThisId() throws IOException, CustomWebClientException {
        //todo
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        String questions =
                StackoverflowImplTest.questions.stream()
                                               .map(Object::toString)
                                               .collect(Collectors.joining("%3B"));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo(questions))
                                          .withQueryParam("order", WireMock.equalTo("desc"))
                                          .withQueryParam("sort", WireMock.equalTo("activity"))
                                          .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));


        QuestionResponse stackoverflowClientQuestions =
                stackoverflowClient.getQuestions(StackoverflowImplTest.questions);
        QuestionResponse repositoryResponse = new QuestionResponse(List.of());

        assertEquals(stackoverflowClientQuestions, repositoryResponse);
    }

    @Test
    public void getQuestions_successRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToFailedExampleResponse));
        String questions =
                StackoverflowImplTest.questions.stream()
                                               .map(Object::toString)
                                               .collect(Collectors.joining("%3B"));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                                              .withPathParam("ids", WireMock.equalTo(questions))
                                              .withQueryParam("order", WireMock.equalTo("desc"))
                                              .withQueryParam("sort", WireMock.equalTo("activity"))
                                              .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                              .inScenario("test_getQuestions_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(502)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }
        response = String.join("", Files.readAllLines(pathToCorrectQuestionExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo(questions))
                                          .withQueryParam("order", WireMock.equalTo("desc"))
                                          .withQueryParam("sort", WireMock.equalTo("activity"))
                                          .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                          .inScenario("test_getQuestions_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));


        QuestionResponse stackoverflowClientQuestions =
                stackoverflowClient.getQuestions(StackoverflowImplTest.questions);
        QuestionResponse repositoryResponse = new QuestionResponse(List.of(
                new QuestionResponse.ItemResponse(
                        78027826L,
                        "Worksheet_Change event executing on workbook open? Can I disable this behaviour?",
                        "https://stackoverflow.com/questions/78027826/worksheet-change-event-executing-on-workbook-open-can-i-disable-this-behaviour",
                        OffsetDateTime.parse("2024-02-20T13:43:25Z"),
                        OffsetDateTime.parse("2024-02-20T13:43:25Z"),
                        null
                ),
                new QuestionResponse.ItemResponse(8968641L,
                        "How do I check a WebClient Request for a 404 error",
                        "https://stackoverflow.com/questions/8968641/how-do-i-check-a-webclient-request-for-a-404-error",
                        OffsetDateTime.parse("2021-08-13T00:24:02Z"), OffsetDateTime.parse("2012-01-23T08:06:40Z"),
                        OffsetDateTime.parse("2012-01-23T09:01:50Z")
                )
        ));

        assertEquals(repositoryResponse, stackoverflowClientQuestions);

    }

    @Test
    public void getQuestions_failedRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToFailedExampleResponse));
        String questions =
                StackoverflowImplTest.questions.stream()
                                               .map(Object::toString)
                                               .collect(Collectors.joining("%3B"));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                                              .withPathParam("ids", WireMock.equalTo(questions))
                                              .withQueryParam("order", WireMock.equalTo("desc"))
                                              .withQueryParam("sort", WireMock.equalTo("activity"))
                                              .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                              .inScenario("test_getQuestions_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(502)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                                          .withPathParam("ids", WireMock.equalTo(questions))
                                          .withQueryParam("order", WireMock.equalTo("desc"))
                                          .withQueryParam("sort", WireMock.equalTo("activity"))
                                          .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                          .inScenario("test_getQuestions_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(502)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));


        assertThrows(CustomWebClientException.class, () -> stackoverflowClient.getQuestions(StackoverflowImplTest.questions));
    }

    @Test
    public void getAnswer_shouldReturnEmptyListIfNoAnswerWithThisId() throws IOException, CustomWebClientException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}/answers"))
                                          .withPathParam(
                                                  "ids",
                                                  WireMock.equalTo(String.valueOf(StackoverflowImplTest.questions.get(0)))
                                          )
                                          .withQueryParam("order", WireMock.equalTo("desc"))
                                          .withQueryParam("sort", WireMock.equalTo("activity"))
                                          .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));

        List<AnswerResponse.ItemResponse> items =
                stackoverflowClient.getAnswers(Long.valueOf(StackoverflowImplTest.questions.get(0)))
                                   .items();

        List<AnswerResponse.ItemResponse> expectedItems = List.of();
        assertEquals(items, expectedItems);
    }

    @Test
    public void getAnswer_successRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToFailedExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}/answers"))
                                              .withPathParam(
                                                      "ids",
                                                      WireMock.equalTo(String.valueOf(StackoverflowImplTest.questions.get(0)))
                                              )
                                              .withQueryParam("order", WireMock.equalTo("desc"))
                                              .withQueryParam("sort", WireMock.equalTo("activity"))
                                              .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                              .inScenario("test_getAnswer_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(502)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }
        response = String.join("", Files.readAllLines(pathToCorrectQuestionExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}/answers"))
                                          .withPathParam(
                                                  "ids",
                                                  WireMock.equalTo(String.valueOf(StackoverflowImplTest.questions.get(0)))
                                          )
                                          .withQueryParam("order", WireMock.equalTo("desc"))
                                          .withQueryParam("sort", WireMock.equalTo("activity"))
                                          .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                          .inScenario("test_getAnswer_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));


        AnswerResponse answers = stackoverflowClient.getAnswers(Long.valueOf(StackoverflowImplTest.questions.get(0)));
        AnswerResponse expectedAnswerResponse = new AnswerResponse(List.of(
                new AnswerResponse.ItemResponse(new AnswerResponse.ItemResponse.Owner(
                        "https://stackoverflow.com/users/23448800/am1234"), OffsetDateTime.parse("2024-02-20T13:43:25Z")),
                new AnswerResponse.ItemResponse(new AnswerResponse.ItemResponse.Owner(
                        "https://stackoverflow.com/users/1161796/alex-gatti"), OffsetDateTime.parse("2012-01-23T08:06:40Z"))
        ));

        assertEquals(answers, expectedAnswerResponse);

    }

    @Test
    public void getAnswer_failedRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToFailedExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}/answers"))
                                              .withPathParam(
                                                      "ids",
                                                      WireMock.equalTo(String.valueOf(StackoverflowImplTest.questions.get(0)))
                                              )
                                              .withQueryParam("order", WireMock.equalTo("desc"))
                                              .withQueryParam("sort", WireMock.equalTo("activity"))
                                              .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                              .inScenario("test_getAnswer_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(502)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));
        }
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}/answers"))
                                          .withPathParam(
                                                  "ids",
                                                  WireMock.equalTo(String.valueOf(StackoverflowImplTest.questions.get(0)))
                                          )
                                          .withQueryParam("order", WireMock.equalTo("desc"))
                                          .withQueryParam("sort", WireMock.equalTo("activity"))
                                          .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                                          .inScenario("test_getAnswer_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(502)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));

        assertThrows(CustomWebClientException.class, () -> stackoverflowClient.getAnswers(Long.valueOf(StackoverflowImplTest.questions.get(0))));
    }
}
