package org.apache.bookkeeper.bookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EntryKeyEqualTest {

    private EntryKey key1;
    private EntryKey key2;
    private EntryKey key3;

    public EntryKeyEqualTest(EntryKey key1, EntryKey key2, EntryKey key3) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new EntryKey(1, 2), new EntryKey(1, 2), new EntryKey(3, 4)},
                {new EntryKey(5, 6), new EntryKey(5, 6), new EntryKey(7, 8)},
                {new EntryKey(9, 10), new EntryKey(9, 10), new EntryKey(11, 12)}
        });
    }

    @Test
    public void testEquals() {
        // Two objects that are equal must return true for equals()
        assertTrue(key1.equals(key2));

        // Objects that are not equal must return false for equals()
        assertFalse(key1.equals(key3));
    }
}
