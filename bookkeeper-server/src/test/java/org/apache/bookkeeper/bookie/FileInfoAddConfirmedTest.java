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
public class FileInfoAddConfirmedTest {

    private final FileInfo fInfo;
    private final Long expected;
    
    public FileInfoAddConfirmedTest(FileInfo fInfo, Long expected) {
        this.fInfo = fInfo;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        File file1 = File.createTempFile("test", "file");
        FileInfo fInfo1 = new FileInfo(file1, new byte[0]);

        File file2 = File.createTempFile("test", "file");
        FileInfo fInfo2 = new FileInfo(file2, new byte[0]);

        Long expected1 = 5L;
        long expected2 = 0L;
        fInfo1.setLastAddConfirmed(expected2); // initialize to null
        fInfo2.setLastAddConfirmed(expected1);
        return Arrays.asList(new Object[][]{
                
                {fInfo1, expected2},
                {fInfo2, expected1}
        });
    }

    @Test
    public void testGetLastAddConfirmed() {
        Long actual = fInfo.getLastAddConfirmed();
        assertEquals(expected, actual);
        
        fInfo.setLastAddConfirmed(10L); // change value to non-null
        actual = fInfo.getLastAddConfirmed();
        assertEquals(Long.valueOf(10), actual);
    }
}
