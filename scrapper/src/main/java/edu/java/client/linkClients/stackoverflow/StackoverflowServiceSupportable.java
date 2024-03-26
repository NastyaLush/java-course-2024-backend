package edu.java.client.linkClients.stackoverflow;

import edu.java.client.linkClients.SupportableLinkService;
import edu.java.client.linkClients.stackoverflow.dto.AnswerResponse;
import edu.java.client.linkClients.stackoverflow.dto.QuestionResponse;
import edu.java.exception.CustomWebClientException;
import java.util.List;

public interface StackoverflowServiceSupportable extends SupportableLinkService {
    QuestionResponse getQuestions(List<Integer> idList) throws CustomWebClientException;

    AnswerResponse getAnswers(Long id) throws CustomWebClientException;
}
