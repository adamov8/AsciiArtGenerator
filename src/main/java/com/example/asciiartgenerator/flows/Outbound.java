package com.example.asciiartgenerator.flows;

import com.example.asciiartgenerator.config.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.dsl.Files;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Outbound {

    @Value("${outputDirectory:destination}")
    private File out;

    @Bean
    IntegrationFlow outboundFlow() {
        return IntegrationFlows
                .from(Channel.OUTBOUND_CHANNEL)
                .handle(Files.outboundAdapter(out).autoCreateDirectory(true).deleteSourceFiles(true))
                .get();
    }
}
