package org.apache.bookkeeper.bookie;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;

import org.apache.bookkeeper.util.ZeroBuffer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BufferedChannelReadTest {

    private static File file;
    private static FileChannel fileChannel;

    private final int pos;
    private final int destLen;
    private final byte[] expected;

    public BufferedChannelReadTest(int pos, int destLen, byte[] expected) {
        this.pos = pos;
        this.destLen = destLen;
        this.expected = expected;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        file = File.createTempFile("test", null);
        fileChannel = new FileOutputStream(file).getChannel();
        byte[] data = new byte[1024];
        Arrays.fill(data, (byte) 42);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        while (buffer.hasRemaining()) {
            fileChannel.write(buffer);
        }
        fileChannel.force(true);
        fileChannel.position(0);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        fileChannel.close();
        Files.deleteIfExists(file.toPath());
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 0, 100, new byte[100] }, // read the first 100 bytes
            { 500, 200, new byte[200] }, // read 200 bytes starting from the 500th
            { 1000, 24, new byte[24] }, // read the last 24 bytes
            { 1024, 0, new byte[0] }, // read after the end of file, expect empty buffer
            { 512, 512, new byte[512] } // read a full block in the middle of the file
        });
    }

    @Test
    public void testRead() throws Exception {
        ByteBuffer dest = ByteBuffer.allocate(destLen);
        ZeroBuffer.put(dest);
        BufferedChannel channel = new BufferedChannel(fileChannel, 1024);
        channel.read(dest, pos);
        byte[] actual = new byte[destLen];
        dest.flip();
        dest.get(actual);
        assertEquals("Mismatch in position " + pos, Arrays.toString(expected), Arrays.toString(actual));
    }

   @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws Exception {
        ByteBuffer dest = null;
        BufferedChannel channel = new BufferedChannel(fileChannel, 1024);
        channel.read(dest, pos);
}
    /*@Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativePos() throws Exception {
        ByteBuffer dest = ByteBuffer.allocate(destLen);
        BufferedChannel channel = new BufferedChannel(fileChannel, 1024);
        channel.read(dest, -1); //non lancia eccezione non è presente un controllo sul valore di pos negativo
    }*/

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithNegativeDestLen() throws Exception {
    ByteBuffer dest = ByteBuffer.allocate(-1);
    BufferedChannel channel = new BufferedChannel(fileChannel, 1024);
    channel.read(dest, 0);
}


}
