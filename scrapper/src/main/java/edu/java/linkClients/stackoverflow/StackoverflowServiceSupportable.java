package edu.java.linkClients.stackoverflow;

import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface StackoverflowServiceSupportable extends SupportableLinkService {
    Mono<QuestionResponse> getQuestions(List<Integer> idList);
}
