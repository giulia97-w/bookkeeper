package org.apache.bookkeeper.bookie;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class EntryKeyTest {
    
    private long ledgerId;
    private long entryId;
    
    public EntryKeyTest(long ledgerId, long entryId) {
        this.ledgerId = ledgerId;
        this.entryId = entryId;
    }
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            
            {1234L, 5678L},
            {-1,-1},
            {0,0}
            
        });
    }
 
    @Test
    public void testGetEntryId() {
        EntryKey key = new EntryKey(ledgerId, entryId);
        assertEquals(entryId, key.getEntryId());
    }
    
    @Test
    public void testGetLedgerId() {
        EntryKey key = new EntryKey(ledgerId, entryId);
        assertEquals(ledgerId, key.getLedgerId());
    }

    

}
        