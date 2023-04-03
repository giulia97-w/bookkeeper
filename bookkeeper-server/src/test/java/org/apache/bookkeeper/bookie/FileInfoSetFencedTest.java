package org.apache.bookkeeper.bookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class FileInfoSetFencedTest {

    private final FileInfo fInfo;
    private final boolean initialFencedState;

    public FileInfoSetFencedTest(File file, byte[] data, boolean initialFencedState) throws IOException {
        this.fInfo = new FileInfo(file, data);
        this.initialFencedState = initialFencedState;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        File file1 = File.createTempFile("test", "file");
        FileInfo fInfo1 = new FileInfo(file1, new byte[0]);

        return Arrays.asList(new Object[][]{
                {file1, new byte[0], false}
                
        });
    }

    @Test
    public void testSetFenced() throws IOException {
        assertFalse(fInfo.isFenced());
        assertEquals(initialFencedState, fInfo.isFenced());

        fInfo.setFenced();

        assertTrue(fInfo.isFenced());
    }
}
