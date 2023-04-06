package org.apache.bookkeeper.bookie;

import static org.junit.Assert.*;
import java.io.RandomAccessFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.nio.file.StandardOpenOption;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.lang.NegativeArraySizeException;
import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;



@RunWith(Parameterized.class)
public class BufferedChannelTest {
    private static final int BUFFER_CAPACITY = 1024 * 1024; // 1 MB
    private static final int FILE_SIZE = 10 * BUFFER_CAPACITY;
    private static final String TEST_FILE_PATH = "testfile.dat";
    
    private Path tmpFile;
    private FileChannel fileChannel;
    private BufferedChannel bufferedChannel;
    private int bufferSize;

    public BufferedChannelTest(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    @Before
    public void setUp() throws IOException {
    tmpFile = Files.createTempFile("BufferedChannelTest", null);
    fileChannel = FileChannel.open(tmpFile, StandardOpenOption.WRITE);
    bufferedChannel = new BufferedChannel(fileChannel, BUFFER_CAPACITY);
}


    @After
    public void tearDown() throws IOException {
        fileChannel.close();
        Files.deleteIfExists(tmpFile);
    }

    @Parameterized.Parameters
    public static Integer[] data() {
        return new Integer[] {-1 ,1,100,1024, 2048, 4096*100 };
    }

    @Test
    public void testPosition() throws IOException {
    if (bufferSize == -1) {
        try {
            ByteBuffer data = ByteBuffer.wrap(new byte[bufferSize]);
        } catch (NegativeArraySizeException e) {
            return;
        }}
        // Write some data to the buffered channel
        ByteBuffer data = ByteBuffer.wrap(new byte[bufferSize]);
        bufferedChannel.write(data);

        // The position of the buffered channel should be equal to the size of the data written
        assertEquals(bufferSize, bufferedChannel.position());
        
    }
    @Test
    public void testReadPastEOF() throws IOException {
        if (bufferSize == -1) {
        try {
            ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
        } catch (IllegalArgumentException e) {
            return;
        }}
        // Write some data to the file channel
        ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
        writeBuffer.put("".getBytes());
        writeBuffer.flip();
        fileChannel.write(writeBuffer);
        
        // Reset the buffer and try to read beyond the end of the file
        ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
        readBuffer.clear();
        try {
            bufferedChannel.read(readBuffer, bufferSize);
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }


    @Test
    public void testFlush() throws IOException {
        if (bufferSize == -1) {
        try {
            ByteBuffer data = ByteBuffer.wrap(new byte[bufferSize]);
        } catch (NegativeArraySizeException e) {
            return;
        }}
    // Write some data to the buffered channel
    ByteBuffer data = ByteBuffer.wrap(new byte[bufferSize]);
    bufferedChannel.write(data);

    // Flush the buffer and ensure all data is written to the file channel
    bufferedChannel.flush(true);
    assertEquals(bufferSize, fileChannel.size());
}
    @Test
    public void testWrite() throws IOException {
    if (bufferSize == -1) {
        try {
            byte[] dataToWrite = new byte[bufferSize];
        } catch (NegativeArraySizeException e) {
            return;
        }
    }

    // Generate random data to write
    byte[] dataToWrite = new byte[bufferSize];
    new Random().nextBytes(dataToWrite);

    // Write the data to the buffered channel
    ByteBuffer writeBuffer = ByteBuffer.wrap(dataToWrite);
    bufferedChannel.write(writeBuffer);

    // Reset the buffer and read the data back from the file channel
    ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
    readBuffer.clear();
    long position = bufferedChannel.position() - bufferSize; // get position to read from
    bufferedChannel.read(readBuffer, position); // pass position as second argument

    // Verify that the data read back matches the data written
    assertArrayEquals(dataToWrite, readBuffer.array());
}



    @Test
    public void testClear() throws IOException {

        
        
        // Create a temporary file to be used in the test
        File file = File.createTempFile("testfile", null);
        FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.WRITE, StandardOpenOption.READ);
        
        if (bufferSize == -1) {
        try {
            ByteBuffer buf = ByteBuffer.allocateDirect(bufferSize);
        } catch (IllegalArgumentException e) {
            return;
        }
    }

        ByteBuffer buf = ByteBuffer.allocateDirect(bufferSize);

        // Create a BufferedChannel instance with the temporary file and ByteBuffer
        BufferedChannel bc = new BufferedChannel(fc, bufferSize);

        // Call clear() method
        bc.clear();

        // Verify that the write buffer has been cleared by checking the file size
        assertTrue(file.length() == 0);

        // Close the channel and delete the temporary file
        fc.close();
        file.delete();
    }

    @Test
    public void testRead() throws IOException {
    File tempFile = File.createTempFile("test", "txt");
    tempFile.deleteOnExit();
    RandomAccessFile file = new RandomAccessFile(tempFile, "rw");
    if (bufferSize == -1) {
        try {
            BufferedChannel bufferedChannel = new BufferedChannel(file.getChannel(), bufferSize);
        } catch (IllegalArgumentException e) {
            return;
        }
    }
    BufferedChannel bufferedChannel = new BufferedChannel(file.getChannel(), bufferSize);
    
    // Write data to the file
    byte[] data = "Hello, world!".getBytes(StandardCharsets.UTF_8);
    ByteBuffer src = ByteBuffer.wrap(data);
    bufferedChannel.write(src);
    bufferedChannel.flush(true);

    // Read data from the file
    ByteBuffer dest = ByteBuffer.allocate(data.length);
    int bytesRead = bufferedChannel.read(dest, 0);
    assertEquals(data.length, bytesRead);
    dest.flip();
    byte[] actualData = new byte[data.length];
    dest.get(actualData);
    assertArrayEquals(data, actualData);
}


    
}

   

    

    
   

    

    



   

    
   





    
    
    
    