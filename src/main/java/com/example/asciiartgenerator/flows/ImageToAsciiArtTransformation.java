package com.example.asciiartgenerator.flows;

import com.example.asciiartgenerator.config.Channel;
import org.springframework.boot.ImageBanner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

@Component
public class ImageToAsciiArtTransformation {

    @Inject
    private Environment environment;

    @Bean
    IntegrationFlow imageToAsciiArtTransformationFlow() {
        return IntegrationFlows
                .from(Channel.INBOUND_CHANNEL)
                .transform(imageFile -> transform((File) imageFile))
                .log(LoggingHandler.Level.WARN, getClass().getName(), file -> "Successfully transformed image file " + file.getHeaders().get("file_name") + " to ASCII art.")
                .channel(Channel.OUTBOUND_CHANNEL)
                .get();
    }

    public Message<String> transform(File source) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        ImageBanner imageBanner = new ImageBanner(new FileSystemResource(new File(source.getPath())));
        imageBanner.printBanner(environment, getClass(), printStream);

        // for testing
//        Message<File> messageFile = MessageBuilder.withPayload(source)
//                .build();
//        throw new MessageTransformationException(messageFile, "test");

        return MessageBuilder.withPayload(new String(baos.toByteArray()))
                .setHeader(FileHeaders.FILENAME, source.getAbsoluteFile().getName().split("\\.")[0] + ".txt")
                .setHeader(FileHeaders.ORIGINAL_FILE, source)
                .build();
    }
}
