package com.example.asciiartgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class DefaultPoller {

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    PollerMetadata poller() {
        return Pollers
                .fixedDelay(1000)
                .get();
    }
}
