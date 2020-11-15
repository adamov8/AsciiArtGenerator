package com.example.asciiartgenerator.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component("errorHandler")
public class ErrorHandler implements org.springframework.util.ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    @Value("${errorDirectory:error}")
    private File errorPath;

    @Override
    public void handleError(Throwable throwable) {
        if (throwable instanceof MessageTransformationException) {
            LOGGER.info("An error occurred while transforming the message: {}", throwable.getMessage());
            Message<?> failedMessage = ((MessageTransformationException) throwable).getFailedMessage();
            File failedFile = ((File) failedMessage.getPayload());
            try {
                String failedPath = errorPath.toString() + File.separatorChar + failedFile.getName();
                Files.move(failedFile.toPath(), Path.of(failedPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            LOGGER.info("Moved {} to error directory.", failedFile);
        } else if (throwable instanceof MessageHandlingException) {
            Throwable cause = throwable.getCause();
            LOGGER.error("An error occurred while handling the message: ", cause);
        } else {
            LOGGER.error("Unhandled flow error occurred: ", throwable);
        }
    }
}
