package com.example.asciiartgenerator.flows;

import com.example.asciiartgenerator.config.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.stereotype.Component;

@Component
public class Error {

    @Bean
    IntegrationFlow errorFlow() {
        return IntegrationFlows
                .from(Channel.ERROR_CHANNEL)
                .handle("errorHandler", "handleError")
                .get();
    }
}
