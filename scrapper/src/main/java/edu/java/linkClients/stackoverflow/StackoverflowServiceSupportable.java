package edu.java.linkClients.stackoverflow;

import edu.java.exceptions.WebClientException;
import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.stackoverflow.dto.AnswerResponse;
import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import java.util.List;
import java.util.Optional;

public interface StackoverflowServiceSupportable extends SupportableLinkService {
    QuestionResponse getQuestions(List<Integer> idList) throws WebClientException;
   AnswerResponse getAnswers(Long id) throws WebClientException;
}
