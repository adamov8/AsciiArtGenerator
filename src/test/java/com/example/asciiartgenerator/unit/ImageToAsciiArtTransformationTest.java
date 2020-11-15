package com.example.asciiartgenerator.unit;

import com.example.asciiartgenerator.flows.ImageToAsciiArtTransformation;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.Message;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ImageToAsciiArtTransformationTest {

    private static final String TEST_JPG = "test.jpg";
    private static final String TEST_TXT = "test.txt";
    private static final String SOURCE = "source";
    private static final File TEST_FILE_SOURCE = new File(SOURCE, TEST_JPG);

    @Mock
    private Environment environment;

    @InjectMocks
    private ImageToAsciiArtTransformation imageToAsciiArtTransformation;

    @BeforeClass
    public static void beforeClass() throws Exception {
        if (Files.exists(Path.of(SOURCE + File.separator + TEST_JPG))) {
            FileUtils.forceDelete(new File(SOURCE, TEST_JPG));
        }
    }

    @Before
    public void setUp() throws Exception {
        Mockito.when(environment.getProperty(any(), eq(Integer.class), any())).thenReturn(4);
        Mockito.when(environment.getProperty(any(), eq(Boolean.class), any())).thenReturn(false);

        Path testJpgPath = Paths.get("src", "test", "resources", TEST_JPG);
        Files.copy(testJpgPath, TEST_FILE_SOURCE.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Assert.assertTrue(Files.exists(Path.of(SOURCE + File.separator + TEST_JPG)));
    }

    @Test
    public void testShouldTransformImageToTextFile() {
        Message<String> message = imageToAsciiArtTransformation.transform(TEST_FILE_SOURCE);
        Assert.assertEquals(TEST_FILE_SOURCE, message.getHeaders().get(FileHeaders.ORIGINAL_FILE));
        Assert.assertEquals(TEST_TXT, message.getHeaders().get(FileHeaders.FILENAME));
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(Path.of(SOURCE + File.separator + TEST_JPG))) {
            FileUtils.forceDelete(new File(SOURCE, TEST_JPG));
        }
    }
}
