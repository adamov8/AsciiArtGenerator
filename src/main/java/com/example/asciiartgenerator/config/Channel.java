package com.example.asciiartgenerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

@Configuration
public class Channel {

    public static final String INBOUND_CHANNEL = "image-in-channel";
    public static final String OUTBOUND_CHANNEL = "image-out-channel";
    public static final String ERROR_CHANNEL = MessageHeaders.ERROR_CHANNEL;

    @Bean(name = ERROR_CHANNEL)
    public MessageChannel errorChannel() {
        return MessageChannels.publishSubscribe().get();
    }
}
