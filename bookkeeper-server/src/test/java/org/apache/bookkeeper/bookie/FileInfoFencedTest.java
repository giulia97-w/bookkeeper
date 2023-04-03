package org.apache.bookkeeper.bookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;



import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class FileInfoFencedTest {
    private final FileInfo fInfo;

    public FileInfoFencedTest(FileInfo fInfo) {
        this.fInfo = fInfo;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        Path tmpDir = Files.createTempDirectory("test");
        File tmpFile = Files.createTempFile(tmpDir, "test", ".dat").toFile();

        FileInfo nullFileInfo = new FileInfo(tmpFile, new byte[] {});
        
        return Arrays.asList(new Object[][] {
                { nullFileInfo }
        });
    }

    @Test
    public void testIsFencedReturnsFalseWhenStateBitsAreNotSet() throws IOException {
        assertFalse(fInfo.isFenced());
    }
}
