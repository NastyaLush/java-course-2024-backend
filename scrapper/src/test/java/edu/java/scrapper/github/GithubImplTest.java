package edu.java.scrapper.github;


import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.ScrapperApplication;
import edu.java.github.GithubClientImpl;
import edu.java.github.dto.RepositoryResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {ScrapperApplication.class})
public class GithubImplTest {
    private static final String GITHUB_OWNER = "somebody";
    private static final String GITHUB_REPOSITORY = "something";
    private static final Path pathToCorrectExampleResponse = Path.of("src/test/java/edu/java/scrapper/github/testData/correctResponseExample.txt");
    private static final Path pathToIncorrectExampleResponse = Path.of("src/test/java/edu/java/scrapper/github/testData/incorrectResponseExample.txt");

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @Test
    public void getGithubRepository_shouldReturnRepositoryOfUser() throws IOException {
        String response = String.join("", Files.readAllLines(pathToCorrectExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repository}"))
                .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                .withPathParam("repository", WireMock.equalTo(GITHUB_REPOSITORY))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(response)));
        GithubClientImpl githubClient = new GithubClientImpl(wireMockExtension.baseUrl());

        Mono<RepositoryResponse> githubRepository = githubClient.getGithubRepository(GITHUB_OWNER, GITHUB_REPOSITORY);
        RepositoryResponse repositoryResponse = new RepositoryResponse(755652574, "java-course-2024-backend", "NastyaLush/java-course-2024-backend", OffsetDateTime.parse("2024-02-20T11:11:08Z"), OffsetDateTime.parse("2024-02-10T19:32:19Z"), OffsetDateTime.parse("2024-02-10T20:01:13Z"));

        StepVerifier.create(githubRepository).expectNext(repositoryResponse).verifyComplete();
    }

    @Test
    public void getGithubRepository_shouldThrowErrorIFRequestIncorrect() throws IOException {
        String response = String.join("", Files.readAllLines(pathToIncorrectExampleResponse));
        wireMockExtension.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/{owner}/{repository}"))
                .withPathParam("owner", WireMock.equalTo(GITHUB_OWNER))
                .withPathParam("repository", WireMock.equalTo(GITHUB_REPOSITORY))
                .willReturn(WireMock.aResponse().withStatus(404).withHeader("Content-Type", "application/json")
                        .withBody(response)));
        GithubClientImpl githubClient = new GithubClientImpl(wireMockExtension.baseUrl());

        Mono<RepositoryResponse> githubRepository = githubClient.getGithubRepository(GITHUB_OWNER, GITHUB_REPOSITORY);

        StepVerifier.create(githubRepository)
                .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException &&
                        throwable.getMessage()
                                .equals("404 Not Found from GET " + wireMockExtension.baseUrl() + "/repos/somebody/something")
                ).verify();
    }

}
