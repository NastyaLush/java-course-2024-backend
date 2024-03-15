package edu.java.scrapper.stackoverflow;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.linkClients.stackoverflow.StackoverflowServiceImplSupportable;
import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WireMockTest
public class StackoverflowImplTest {
    private static final List<Integer> questions = List.of(78027826, 8968641);
    private static final Path pathToCorrectExampleResponse = Path.of("src/test/java/edu/java/scrapper/stackoverflow/testData/correctResponseExample.txt");
    private static final Path pathToIncorrectExampleResponse = Path.of("src/test/java/edu/java/scrapper/stackoverflow/testData/incorrectResponseExample.txt");
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @Test
    public void getQuestions_shouldReturnListOfQuestions() throws IOException {
        String response = String.join("", Files.readAllLines(pathToCorrectExampleResponse));
        String questions = StackoverflowImplTest.questions.stream().map(Object::toString).collect(Collectors.joining("%3B"));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                .withPathParam("ids", WireMock.equalTo(questions)).withQueryParam("order", WireMock.equalTo("desc"))
                .withQueryParam("sort", WireMock.equalTo("activity"))
                .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(response)));

        StackoverflowServiceImplSupportable stackoverflowClient = new StackoverflowServiceImplSupportable(wireMockExtension.baseUrl());

        Mono<QuestionResponse> stackoverflowClientQuestions = stackoverflowClient.getQuestions(StackoverflowImplTest.questions);
        QuestionResponse repositoryResponse = new QuestionResponse(List.of(
                new QuestionResponse.ItemResponse(78027826L,
                        "Worksheet_Change event executing on workbook open? Can I disable this behaviour?",
                        "https://stackoverflow.com/questions/78027826/worksheet-change-event-executing-on-workbook-open-can-i-disable-this-behaviour",
                        OffsetDateTime.parse("2024-02-20T13:43:25Z"),
                        OffsetDateTime.parse("2024-02-20T13:43:25Z"), null),
                new QuestionResponse.ItemResponse(8968641L,
                        "How do I check a WebClient Request for a 404 error",
                        "https://stackoverflow.com/questions/8968641/how-do-i-check-a-webclient-request-for-a-404-error",
                        OffsetDateTime.parse("2021-08-13T00:24:02Z"), OffsetDateTime.parse("2012-01-23T08:06:40Z"),
                        OffsetDateTime.parse("2012-01-23T09:01:50Z"))));

        StepVerifier.create(stackoverflowClientQuestions).expectNext(repositoryResponse).verifyComplete();
    }
    @Test
    public void getQuestions_shouldReturnEmptyListIfNoAnswerWithThisId() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        String questions = StackoverflowImplTest.questions.stream().map(Object::toString).collect(Collectors.joining("%3B"));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/2.3/questions/{ids}"))
                .withPathParam("ids", WireMock.equalTo(questions)).withQueryParam("order", WireMock.equalTo("desc"))
                .withQueryParam("sort", WireMock.equalTo("activity"))
                .withQueryParam("site", WireMock.equalTo("stackoverflow"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(response)));

        StackoverflowServiceImplSupportable stackoverflowClient = new StackoverflowServiceImplSupportable(wireMockExtension.baseUrl());

        Mono<QuestionResponse> stackoverflowClientQuestions = stackoverflowClient.getQuestions(StackoverflowImplTest.questions);
        QuestionResponse repositoryResponse = new QuestionResponse(List.of());

        StepVerifier.create(stackoverflowClientQuestions).expectNext(repositoryResponse).verifyComplete();
    }
}
