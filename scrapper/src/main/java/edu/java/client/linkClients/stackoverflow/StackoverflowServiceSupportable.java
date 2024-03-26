package edu.java.client.linkClients.stackoverflow;

import com.example.exceptions.CustomWebClientException;
import edu.java.client.linkClients.SupportableLinkService;
import edu.java.client.linkClients.stackoverflow.dto.AnswerResponse;
import edu.java.client.linkClients.stackoverflow.dto.QuestionResponse;
import java.util.List;

public interface StackoverflowServiceSupportable extends SupportableLinkService {
    QuestionResponse getQuestions(List<Integer> idList) throws CustomWebClientException;

    AnswerResponse getAnswers(Long id) throws CustomWebClientException;
}
