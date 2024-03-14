package edu.java.stackoverflow;

import edu.java.stackoverflow.dto.QuestionResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface StackoverflowClient {
    Mono<QuestionResponse> getQuestions(List<Integer> idList);
}
