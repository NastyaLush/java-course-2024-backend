package edu.java.linkClients.stackoverflow;

import edu.java.linkClients.SupportableLinkService;
import edu.java.linkClients.stackoverflow.dto.QuestionResponse;
import java.util.List;

public interface StackoverflowServiceSupportable extends SupportableLinkService {
    QuestionResponse getQuestions(List<Integer> idList);
}
