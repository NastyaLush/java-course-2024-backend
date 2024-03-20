package edu.java.linkClients.stackoverflow;

import edu.java.exceptions.CustomWebClientException;
import edu.java.linkClients.LinkUpdateResponse;
import edu.java.linkClients.stackoverflow.dto.AnswerResponse;
import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;

@Log4j2
public class StackoverflowServiceImplSupportable implements StackoverflowServiceSupportable {
    public static final String DOMAIN = "stackoverflow.com";
    private static final String STACKOVERFLOW_API_BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackoverflowServiceImplSupportable() {
        this.webClient = WebClient.builder()
                                  .baseUrl(STACKOVERFLOW_API_BASE_URL)
                                  .build();
    }

    public StackoverflowServiceImplSupportable(String baseUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(baseUrl)
                                  .build();
    }

    private URI createUri(UriBuilder uriBuilder, String url, String arg) {
        return uriBuilder.path(url)
                         .queryParam("order", "desc")
                         .queryParam("sort", "activity")
                         .queryParam("site", "stackoverflow")
                         .build(arg);
    }

    @Override
    public QuestionResponse getQuestions(@NotNull List<Integer> idList) throws CustomWebClientException {
        String ids = idList.stream()
                           .map(String::valueOf)
                           .collect(Collectors.joining(";"));
        try {
            return webClient.get()
                            .uri(uriBuilder -> createUri(uriBuilder, "/2.3/questions/{ids}", ids))
                            .retrieve()
                            .bodyToMono(QuestionResponse.class)
                            .block();
        } catch (WebClientResponseException | WebClientRequestException ex) {
            log.warn(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }

    @Override
    public AnswerResponse getAnswers(Long id) throws CustomWebClientException {
        try {
            return webClient.get()
                            .uri(uriBuilder -> createUri(uriBuilder, "/2.3/questions/{ids}/answers", String.valueOf(id)))
                            .retrieve()
                            .bodyToMono(AnswerResponse.class)
                            .block();
        } catch (WebClientResponseException | WebClientRequestException ex) {
            log.warn(ex.getMessage());
            throw new CustomWebClientException(ex.getMessage());
        }
    }

    @Override
    public String getDomain() {
        return DOMAIN;
    }

    @Override
    public LinkUpdateResponse getLastUpdateDate(String pathOfUrl,
                                                OffsetDateTime lastUpdate) throws CustomWebClientException {
        OffsetDateTime newLastUpdate = lastUpdate;
        StringBuilder descriptionBuilder = new StringBuilder();
        String[] split = pathOfUrl.split("/");
        List<Integer> ids = Arrays.stream(split[2].split(";"))
                                  .map(Integer::parseInt)
                                  .toList();
        QuestionResponse questions = getQuestions(ids);
        for (QuestionResponse.ItemResponse itemResponse : questions.items()) {
            String questionBeginning = "question ";
            if (itemResponse.lastEditDate()
                            .isAfter(lastUpdate)) {
                newLastUpdate = newLastUpdate.isAfter(itemResponse.lastEditDate()) ? newLastUpdate
                        : itemResponse.lastEditDate();
                descriptionBuilder.append(questionBeginning)
                                  .append(itemResponse.lastEditDate())
                                  .append(" has been edited\n");
            }
            AnswerResponse answerResponseList = getAnswers(itemResponse.id());
            for (AnswerResponse.ItemResponse answerResponse : answerResponseList.items()) {
                if (answerResponse.createdAt()
                                  .isAfter(lastUpdate)) {
                    newLastUpdate = newLastUpdate.isAfter(answerResponse.createdAt()) ? newLastUpdate
                            : answerResponse.createdAt();
                    descriptionBuilder.append(questionBeginning)
                                      .append(itemResponse.lastEditDate())
                                      .append(" has new answer by")
                                      .append(answerResponse.owner()
                                                            .ownerLink())
                                      .append("\n");

                }
            }
        }

        return new LinkUpdateResponse(lastUpdate, descriptionBuilder.toString());
    }

}
