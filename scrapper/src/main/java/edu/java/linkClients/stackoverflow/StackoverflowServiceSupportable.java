package edu.java.linkClients.stackoverflow;

import edu.java.exceptions.CustomWebClientException;
import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.stackoverflow.dto.AnswerResponse;
import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import java.util.List;

public interface StackoverflowServiceSupportable extends SupportableLinkService {
    QuestionResponse getQuestions(List<Integer> idList) throws CustomWebClientException;

    AnswerResponse getAnswers(Long id) throws CustomWebClientException;
}
