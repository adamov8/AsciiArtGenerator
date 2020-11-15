package com.example.asciiartgenerator.flows;

import com.example.asciiartgenerator.config.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Inbound {

    private static final String JPG_PNG = ".*\\.(jpg|png)";

    @Value("${inputDirectory:source}")
    private File in;

    @Bean
    IntegrationFlow inboundFlow() {
        return IntegrationFlows
                .from(Files.inboundAdapter(in).autoCreateDirectory(true).preventDuplicates(true).regexFilter(JPG_PNG))
                .log(LoggingHandler.Level.WARN, getClass().getName(), file -> "Found new file:" + file.getHeaders().get("file_name") + " in " + in.getPath())
                .channel(Channel.INBOUND_CHANNEL)
                .get();
    }
}
