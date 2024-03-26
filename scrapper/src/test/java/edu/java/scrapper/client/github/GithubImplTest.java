package edu.java.scrapper.client.github;

import edu.java.exception.CustomWebClientException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.client.linkClients.github.GithubServiceSupportable;
import edu.java.client.linkClients.github.dto.IssueResponse;
import edu.java.client.linkClients.github.dto.PullRequestResponse;
import edu.java.client.linkClients.github.dto.RepositoryResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
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
@ContextConfiguration(initializers = GithubImplTest.Initializer.class)
public class GithubImplTest {
    @Autowired
    GithubServiceSupportable githubClient;
    private static final int COUNT_OF_ATTEMPTS = 4;
    private static final String GITHUB_OWNER = "somebody";
    private static final String GITHUB_REPOSITORY = "something";
    private static final Path pathToCorrectRepositoryExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/client/github/testData/correctRepositoryResponseExample.txt");
    private static final Path pathToCorrectCommentsExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/client/github/testData/correctCommentsResponseExample.txt");
    private static final Path pathToCorrectPullRequestsExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/client/github/testData/correctPullRequestResponseExample.txt");
    private static final Path pathToIncorrectExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/client/github/testData/incorrectResponseExample.txt");

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("app.client-config.github.api-url=" + wireMockExtension.baseUrl())
                              .applyTo(configurableApplicationContext);
            TestPropertyValues.of("app.retry-config.max-attempts=" + (COUNT_OF_ATTEMPTS + 1))
                              .applyTo(configurableApplicationContext);
        }
    }

    @Test
    public void getGithubRepository_successRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {

            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}"))
                                              .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                              .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                              .inScenario("test_getGithubRepository_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(403)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }

        response = String.join("", Files.readAllLines(pathToCorrectRepositoryExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repository}"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repository", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .inScenario("test_getGithubRepository_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));

        RepositoryResponse githubRepository = githubClient.getGithubRepository(GITHUB_OWNER, GITHUB_REPOSITORY);
        RepositoryResponse repositoryResponse = new RepositoryResponse(
                755652574,
                "java-course-2024-backend",
                "NastyaLush/java-course-2024-backend",
                OffsetDateTime.parse("2024-02-20T11:11:08Z"),
                OffsetDateTime.parse("2024-02-10T19:32:19Z"),
                OffsetDateTime.parse("2024-02-10T20:01:13Z")
        );

        assertEquals(githubRepository, repositoryResponse);

    }

    @Test
    public void getGithubRepository_failedRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {

            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}"))
                                              .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                              .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                              .inScenario("test_getGithubRepository_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(403)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }


        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .inScenario("test")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(403)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));


        assertThrows(
                CustomWebClientException.class,
                () -> githubClient.getGithubRepository(GITHUB_OWNER, GITHUB_REPOSITORY)
        );

    }



    @Test
    public void getListComments_successRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/issues/comments"))
                                              .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                              .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                              .inScenario("test_getListComments_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(403)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }

        response = String.join("", Files.readAllLines(pathToCorrectCommentsExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/issues/comments"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .inScenario("test_getListComments_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));

        List<IssueResponse> listComments = githubClient.getListComments(GITHUB_OWNER, GITHUB_REPOSITORY);

        List<IssueResponse> expectedListComments = List.of(
                new IssueResponse(
                        1937105610,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/1#issuecomment-1937105610",
                        OffsetDateTime.parse("2024-02-10T19:47:23Z")
                ),
                new IssueResponse(
                        1950265862,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/2#issuecomment-1950265862",
                        OffsetDateTime.parse("2024-02-17T17:39:48Z")
                ),
                new IssueResponse(
                        1950265903,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/1#issuecomment-1950265903",
                        OffsetDateTime.parse("2024-02-17T17:40:01Z")
                ),
                new IssueResponse(
                        1956074328,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/3#issuecomment-1956074328",
                        OffsetDateTime.parse("2024-02-21T07:54:14Z")
                ),
                new IssueResponse(
                        1966553840,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/4#issuecomment-1966553840",
                        OffsetDateTime.parse("2024-02-27T13:28:18Z")
                ),
                new IssueResponse(
                        1972977827,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/5#issuecomment-1972977827",
                        OffsetDateTime.parse("2024-03-01T10:59:46Z")
                ),
                new IssueResponse(
                        1997155416,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/6#issuecomment-1997155416",
                        OffsetDateTime.parse("2024-03-14T10:47:38Z")
                ),
                new IssueResponse(
                        1999866501,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/7#issuecomment-1999866501",
                        OffsetDateTime.parse("2024-03-15T15:06:54Z")
                )

        );
        assertEquals(listComments, expectedListComments);

    }

    @Test
    public void getListComments_failedRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {

            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/issues/comments}"))
                                              .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                              .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                              .inScenario("test_getListComments_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(403)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }

        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/issues/comments}"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .inScenario("test_getListComments_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(403)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));


        assertThrows(
                CustomWebClientException.class,
                () -> githubClient.getGithubRepository(GITHUB_OWNER, GITHUB_REPOSITORY)
        );
    }


    @Test
    public void getPullRequests_successRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {
            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/pulls"))
                                              .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                              .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                              .inScenario("test_getPullRequests_success")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(400)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response)).willSetStateTo(String.valueOf(i + 1)));

        }

        response = String.join("", Files.readAllLines(pathToCorrectPullRequestsExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/pulls"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .inScenario("test_getPullRequests_success")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));

        List<PullRequestResponse> listPullRequests = githubClient.getListPullRequests(GITHUB_OWNER, GITHUB_REPOSITORY);
        List<PullRequestResponse> expectedList = List.of(
                new PullRequestResponse(
                        1774374423,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/7",
                        "Hw5",
                        null,
                        OffsetDateTime.parse("2024-03-15T15:06:13Z"),
                        new PullRequestResponse.UserResponse("NastyaLush", "https://github.com/NastyaLush")
                ),
                new PullRequestResponse(
                        1771890499,
                        "https://github.com/NastyaLush/java-course-2024-backend/pull/6",
                        "hw3 relocate clients",
                        null,
                        OffsetDateTime.parse("2024-03-14T10:46:55Z"),
                        new PullRequestResponse.UserResponse("NastyaLush", "https://github.com/NastyaLush")
                )

        );
        assertEquals(listPullRequests, expectedList);

    }

    @Test
    public void getPullRequests_failedRetry() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        for (int i = 0; i < COUNT_OF_ATTEMPTS; i++) {

            wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/pulls"))
                                              .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                              .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                              .inScenario("test_getPullRequests_failed")
                                              .whenScenarioStateIs(i == 0 ? STARTED : String.valueOf(i))
                                              .willReturn(WireMock.aResponse()
                                                                  .withStatus(403)
                                                                  .withHeader("Content-Type", "application/json")
                                                                  .withBody(response))
                                              .willSetStateTo(String.valueOf(i + 1)));

        }


        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/pulls"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .inScenario("test_getPullRequests_failed")
                                          .whenScenarioStateIs(String.valueOf(COUNT_OF_ATTEMPTS))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(403)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));

        assertThrows(
                CustomWebClientException.class,
                () -> githubClient.getListPullRequests(GITHUB_OWNER, GITHUB_REPOSITORY)
        );

    }


}
