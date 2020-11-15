package com.example.asciiartgenerator.unit;

import com.example.asciiartgenerator.handlers.ErrorHandler;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.messaging.Message;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ErrorHandlerTest {

    private static final String TEST_JPG = "test.jpg";
    private static final String SOURCE = "source";
    private static final String ERROR = "error";
    private static final File TEST_FILE_SOURCE = new File(SOURCE, TEST_JPG);

    @Mock
    private File error;

    @InjectMocks
    private ErrorHandler errorHandler;

    @BeforeClass
    public static void beforeClass() throws Exception {
        if (Files.exists(Path.of(SOURCE + File.separator + TEST_JPG))) {
            FileUtils.forceDelete(new File(SOURCE, TEST_JPG));
        }
    }

    @Before
    public void setUp() throws Exception {
        Mockito.when(error.getPath()).thenReturn(ERROR);
        Assert.assertTrue(TEST_FILE_SOURCE.createNewFile());
    }

    @Test
    public void testShouldMoveFileToDestinationDir() {
        Message<File> messageFile = MessageBuilder.withPayload(TEST_FILE_SOURCE)
                .build();
        MessageTransformationException mte = new MessageTransformationException(messageFile, "test message");

        Assert.assertTrue(Files.exists(TEST_FILE_SOURCE.toPath()));
        errorHandler.handleError(mte);
        Assert.assertFalse(Files.exists(TEST_FILE_SOURCE.toPath()));
        Assert.assertTrue(Files.exists(Path.of(error.getPath() + File.separator + TEST_JPG)));
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(Path.of(ERROR + File.separator + TEST_JPG))) {
            FileUtils.forceDelete(new File(ERROR, TEST_JPG));
        }
    }
}
