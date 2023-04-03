package org.apache.bookkeeper.bookie;

import org.apache.bookkeeper.test.BookKeeperClusterTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertNull;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class FileInfoReadTest extends BookKeeperClusterTestCase {

    public static final String VALID_STRING = "Hello,world";
    public static final String PASSWORD = "pwd";
    public static final String OK = "ok";
    public static final String NOT = "not";
    public static final String EXPECTED_TO_FAIL = "Nessun fallimento";
    public static final String TESTFILE_PATHNAME = "/tmp/readChannelTest";
    public static final String NOT_EXPECTED_EXCEPTION = "Nessuna eccezione ";
    

    private static ByteBuffer buff;
    private static long position;
    private static boolean bestEffort;
    private static String expectedBehavior;
    private File f; 
    private FileInfo fInfo;
    private ByteBuffer buffer;
    
    private static class FileInfoReadTestParams {
        private ByteBuffer buff;
        private long position;
        private boolean bestEffort;
        private String expectedBehavior;

        public FileInfoReadTestParams(ByteBuffer buff, long position, boolean bestEffort, String expectedBehavior) {
            this.buff = buff;
            this.position = position;
            this.bestEffort = bestEffort;
            this.expectedBehavior = expectedBehavior;
        }
    }

    public FileInfoReadTest(FileInfoReadTestParams params) {
        super(10);
        this.buff = params.buff;
        this.position = params.position;
        this.bestEffort = params.bestEffort;
        this.expectedBehavior = params.expectedBehavior;
    }
    

    @Before
    public void setup() throws IOException {
        File f = new File(TESTFILE_PATHNAME);
        buffer = ByteBuffer.allocate(1024);
        fInfo = new FileInfo(f, PASSWORD.getBytes());
        ByteBuffer[] toWrite = new ByteBuffer[] {ByteBuffer.wrap(VALID_STRING.getBytes())};
        fInfo.write(toWrite, 0);
       

    }


    @Parameterized.Parameters
    public static Collection<FileInfoReadTestParams[]> getTestParameters() {
        FileInfoReadTestParams params1 = new FileInfoReadTestParams(ByteBuffer.allocate(VALID_STRING.length()), 0, true, OK);
        FileInfoReadTestParams params2 = new FileInfoReadTestParams(ByteBuffer.allocate(VALID_STRING.length()), -1, true, OK);
        FileInfoReadTestParams params3 = new FileInfoReadTestParams(ByteBuffer.allocate(VALID_STRING.length()), 1, true, OK);
        FileInfoReadTestParams params4 = new FileInfoReadTestParams(ByteBuffer.allocate(VALID_STRING.length()), 1, false, NOT);
        FileInfoReadTestParams params5 = new FileInfoReadTestParams(null, 0, true, NOT);

        List<FileInfoReadTestParams[]> args = new ArrayList<>();
        args.add(new FileInfoReadTestParams[]{params1});
        args.add(new FileInfoReadTestParams[]{params2});
        args.add(new FileInfoReadTestParams[]{params3});
        args.add(new FileInfoReadTestParams[]{params4});
        args.add(new FileInfoReadTestParams[]{params5});
        

        return args;
    }


    @After
    public void teardown() throws IOException {
        if (buff != null) buff.clear();
        fInfo.flushHeader();
        fInfo.close(true);
        fInfo.delete();
        
    }

    @Test
    public void fileInfoWriteThenReadTest() {
        
        int readBytesNo = 0;
        long exp = (VALID_STRING.length() - position);
        try {
            readBytesNo = fInfo.read(buff, position, bestEffort);
        } catch (Exception e) {
            assertTrue(NOT_EXPECTED_EXCEPTION + e.toString(), expectedBehavior == NOT);
            return;
        }
        assertTrue("Invalid number of read bytes.", readBytesNo >= 0);
        assertTrue("Read a different amount of bytes than expected. Expected TODO" + ", read " + readBytesNo, (readBytesNo == (position < 0 ? exp + position : exp) && expectedBehavior == OK)
                || (readBytesNo != (position < 0 ? exp + position : exp) && expectedBehavior == NOT));
        if (expectedBehavior == NOT) return;
        if (position >= 0)
        assertEquals("Reading something different than what was written.", VALID_STRING.substring((int) position, (int) exp), new String(buff.array(), Charset.defaultCharset()).substring(0, (int) (exp - position)));
        assertEquals("Read bytes different than buffer's bytes", readBytesNo, buff.position());
        assertTrue(EXPECTED_TO_FAIL, expectedBehavior == OK);
    }

    

    
}

    

    
    




    
    








    
    
    




    
    
    
    
   
    
    






    


    

    
    



    
    

    

