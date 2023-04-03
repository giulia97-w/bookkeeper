package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class FileInfoExplicitTest {
    private final FileInfo fInfo;

    public FileInfoExplicitTest(FileInfo fInfo) {
        this.fInfo = fInfo;
    }

    @Parameterized.Parameters
    public static Object[] data() {

    try {
        return new Object[]{
                new FileInfo(new java.io.File(""), new byte[]{}), // Test with null explicitLac
                new FileInfo(new java.io.File(""), new byte[]{0, 1, 2}) // Test with non-null explicitLac
        };
    } catch (java.io.IOException e) {
            throw new RuntimeException(e);}
    }

    @Test
    public void getExplicitLac_shouldReturnNullIfExplicitLacIsNull() {
        // Ensure that the explicitLac member is null
        assertNull(fInfo.getExplicitLac());

        // Call getExplicitLac and ensure that it returns null
        ByteBuf explicitLac = fInfo.getExplicitLac();
        assertNull(explicitLac);
    }
}


