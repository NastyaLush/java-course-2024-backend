package edu.java.scrapper.github;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.linkClients.github.GithubServiceImplSupportable;
import edu.java.linkClients.github.dto.IssueResponse;
import edu.java.linkClients.github.dto.PullRequestResponse;
import edu.java.linkClients.github.dto.RepositoryResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ScrapperApplication.class})
public class GithubImplTest {
    private static final String GITHUB_OWNER = "somebody";
    private static final String GITHUB_REPOSITORY = "something";
    private static final Path pathToCorrectRepositoryExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/github/testData/correctRepositoryResponseExample.txt");
    private static final Path pathToCorrectCommentsExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/github/testData/correctCommentsResponseExample.txt");
    private static final Path pathToCorrectPullRequestsExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/github/testData/correctPullRequestResponseExample.txt");
    private static final Path pathToIncorrectExampleResponse =
            Path.of("src/test/java/edu/java/scrapper/github/testData/incorrectResponseExample.txt");

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
                                                                  .options(WireMockConfiguration.wireMockConfig()
                                                                                                .dynamicPort())
                                                                  .build();

    @Test
    public void getGithubRepository_shouldReturnRepositoryOfUser() throws IOException {
        //todo
        String response = String.join("", Files.readAllLines(pathToCorrectRepositoryExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repository}"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repository", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));
        GithubServiceImplSupportable githubClient = new GithubServiceImplSupportable(wireMockExtension.baseUrl());

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
//        StepVerifier.create(githubRepository).expectNext(repositoryResponse).verifyComplete();
    }

    @Test
    public void getGithubRepository_shouldThrowErrorIFRequestIncorrect() throws IOException {
        //todo
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));
        GithubServiceImplSupportable githubClient = new GithubServiceImplSupportable(wireMockExtension.baseUrl());

        assertThrows(
                WebClientResponseException.class,
                () -> githubClient.getGithubRepository(GITHUB_OWNER, GITHUB_REPOSITORY)
        );
    }

    @Test
    public void getListComments_shouldReturnListCommentsOfUser() throws IOException {
        String response = String.join("", Files.readAllLines(pathToCorrectCommentsExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/issues/comments"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));
        GithubServiceImplSupportable githubClient = new GithubServiceImplSupportable(wireMockExtension.baseUrl());

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
    public void getListComments_shouldThrowErrorIFRequestIncorrect() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/issues/comments}"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));
        GithubServiceImplSupportable githubClient = new GithubServiceImplSupportable(wireMockExtension.baseUrl());

        assertThrows(
                WebClientResponseException.class,
                () -> githubClient.getListComments(GITHUB_OWNER, GITHUB_REPOSITORY)
        );
    }

    @Test
    public void getPullRequests_shouldReturnListCommentsOfUser() throws IOException {
        String response = String.join("", Files.readAllLines(pathToCorrectPullRequestsExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/pulls"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(200)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));
        GithubServiceImplSupportable githubClient = new GithubServiceImplSupportable(wireMockExtension.baseUrl());

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
    public void getPullRequests_shouldThrowErrorIFRequestIncorrect() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repo}/pulls"))
                                          .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                                          .withPathParam("repo", WireMock.equalTo(GITHUB_REPOSITORY))
                                          .willReturn(WireMock.aResponse()
                                                              .withStatus(404)
                                                              .withHeader("Content-Type", "application/json")
                                                              .withBody(response)));
        GithubServiceImplSupportable githubClient = new GithubServiceImplSupportable(wireMockExtension.baseUrl());

        assertThrows(
                WebClientResponseException.class,
                () -> githubClient.getListPullRequests(GITHUB_OWNER, GITHUB_REPOSITORY)
        );
    }

}
