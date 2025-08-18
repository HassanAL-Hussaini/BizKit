package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service

public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder Builder) {
        chatClient = Builder.build();
    }

    public String chat(String prompt){
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
