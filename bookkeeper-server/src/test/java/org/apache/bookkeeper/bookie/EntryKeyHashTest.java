package org.apache.bookkeeper.bookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EntryKeyHashTest {

    private EntryKey key1;
    private EntryKey key2;
    private EntryKey key3;

    public EntryKeyHashTest(EntryKey key1, EntryKey key2, EntryKey key3) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new EntryKey(123, 456), new EntryKey(123, 456), new EntryKey(123, 789)},
                {new EntryKey(456, 789), new EntryKey(456, 789), new EntryKey(456, 123)},
                {new EntryKey(789, 123), new EntryKey(789, 123), new EntryKey(789, 456)}
        });
    }

    @Test
    public void testHashCode() {
        // Two objects that are equal must have the same hashCode
        assertTrue(key1.equals(key2));
        assertEquals(key1.hashCode(), key2.hashCode());

        // Objects that are not equal can have the same hashCode, but this should be rare
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }
}
