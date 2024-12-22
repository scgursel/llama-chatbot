package dev.scgursel.llama2.Service;


import dev.scgursel.llama2.Model.Conversation;
import dev.scgursel.llama2.Model.Response;
import dev.scgursel.llama2.Repo.ConversationRepository;
import dev.scgursel.llama2.Model.History;
import dev.scgursel.llama2.Repo.HistoryRepo;
import jakarta.transaction.Transactional;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private static final String CURRENT_PROMPT_INSTRUCTIONS = """
    The user is asking or requesting something here. Your response should address the user's needs directly and provide a helpful answer.
    
    If the request is straightforward (e.g., asking to write an email or request a day off), respond directly without asking for unnecessary clarification.
    If the request can be answered without needing extra context (e.g., simply requesting a day off), provide the solution without delay.
    
    Here's the `user_main_prompt`:
""";



    private static final String PROMPT_GENERAL_INSTRUCTIONS = """
    Here are the general guidelines to answer the `user_main_prompt`
    
    You'll act as a personal assistant to help the user with their inquiries or tasks.
    
    - If the request is clear and can be answered directly (e.g., writing an email or requesting a day off), do so without asking for more details.
    - Do not repeatedly ask for context if the user's request is simple and straightforward.
    - Avoid excessive pleasantries, conversational fillers, or unnecessary elaboration.
    - Do not reference your internal processes, guidelines, or the conversation history explicitly.
    - Focus on delivering helpful, direct, and clear information.
""";



    private static final String PROMPT_CONVERSATION_HISTORY_INSTRUCTIONS = """
                The object `conversational_history` below represents past interactions between the user and you (the AI assistant).
                Each `history_entry` contains a pair of `prompt` and `response`.
                
                If the user explicitly asks for a summary of past conversations, retrieve and summarize the relevant entries without mentioning the history object or how it is used.
                
                You should use the information in `conversational_history` to:
                    - Refer back to previous topics or answers when relevant.
                    - Build continuity in the conversation by linking responses to past prompts.
                    - Provide clear summaries without over-explaining or making the process visible to the user.
                
                Simply respond to the `user_main_prompt` based on your knowledge, relevant context, and past interactions.
                
                `conversational_history`:
            """;


//    private final static Map<Long, List<History>> conversationalHistoryStorage = new HashMap<>();

    private final OllamaChatModel ollamaChatModel;

    private final HistoryRepo historyRepo;
    private final ConversationRepository conversationRepository;

    public ChatService(OllamaChatModel ollamaChatModel, HistoryRepo historyRepo,
                       ConversationRepository conversationRepository) {
        this.ollamaChatModel = ollamaChatModel;
        this.historyRepo = historyRepo;
        this.conversationRepository = conversationRepository;
    }

    @Transactional
    public History call(String userMessage, Long conversationId) {
        if (conversationId == null) {
            conversationId = 0L;
        }

        Optional<Conversation> conversationOp = conversationRepository.findById(conversationId);
        Conversation saved = null;

        if (!conversationOp.isPresent()) {
            Conversation conversation = new Conversation();

            conversation.setTitle(
                    userMessage.length() > 22
                            ? capitalizeFirstLetter(userMessage.substring(0, 22))
                            : capitalizeFirstLetter(userMessage)
            );

            saved = conversationRepository.save(conversation);
            conversationId = saved.getId();
        }

        List<History> historyList = historyRepo.findByConversationId(conversationId);

        var historyPrompt = new StringBuilder(PROMPT_CONVERSATION_HISTORY_INSTRUCTIONS);
        for (History history : historyList) {
            historyPrompt.append(history.toString());
        }

        var contextSystemMessage = new SystemMessage(historyPrompt.toString());
        var generalSystemMessage = new SystemMessage(PROMPT_GENERAL_INSTRUCTIONS);

        var currentPromptMessage = new UserMessage(CURRENT_PROMPT_INSTRUCTIONS.concat(userMessage));

        Prompt prompt = new Prompt(List.of(contextSystemMessage, generalSystemMessage, currentPromptMessage));
        String response = ollamaChatModel.call(prompt).getResult().getOutput().getContent();
//        System.out.println("Message: " + userMessage);
//        System.out.println("Response: " + response);

        History history = new History(userMessage, response);
        history.setConversation(conversationOp.orElse(saved));

        historyRepo.save(history);

        return history;
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAllOrder();
    }

    @Transactional
    public List<History> getHistory(Long conversationId) {
        return historyRepo.findByConversationId(conversationId);
    }

    @Transactional
    public Long deleteConversation(Long conversationId) {
        historyRepo.deleteByConversationId(conversationId);
        return conversationRepository.deleteByIdAllIgnoreCase(conversationId);
    }
}
