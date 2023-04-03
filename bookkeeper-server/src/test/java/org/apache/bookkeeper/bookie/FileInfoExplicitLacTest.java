package org.apache.bookkeeper.bookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
@RunWith(Parameterized.class)
public class FileInfoExplicitLacTest {
    private FileInfo fInfo;
    
    @Parameterized.Parameters(name = "LAC = {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 123L },
            { 456L },
            { 789L }
        });
    }
    
    private long lac;
    
    public FileInfoExplicitLacTest (long lac) {
        this.lac = lac;
    }
    
    @Test
    public void testSetExplicitLac() throws Exception {
        File tmpFile = File.createTempFile("test-file", "txn");
        tmpFile.deleteOnExit();
        FileInfo fileInfo = new FileInfo(tmpFile, null);

        // Create a buffer with LAC and some additional data
        ByteBuf lacBuf = Unpooled.buffer(Long.BYTES + 8);
        lacBuf.writeLong(0L);
        lacBuf.writeLong(lac);

        // Set explicit LAC
        fileInfo.setExplicitLac(lacBuf);

        // Verify that LAC was set
        assertEquals(lac, (long) fileInfo.getLastAddConfirmed());

        // Verify that explicit LAC was set correctly
        ByteBuf explicitLacBuf = fileInfo.getExplicitLac();
        assertEquals(Long.BYTES + 8, explicitLacBuf.readableBytes());
        assertEquals(0L, explicitLacBuf.readLong()); // First 8 bytes should be 0
        assertEquals(lac, explicitLacBuf.readLong()); // Next 8 bytes should be the LAC
    }

    
}

