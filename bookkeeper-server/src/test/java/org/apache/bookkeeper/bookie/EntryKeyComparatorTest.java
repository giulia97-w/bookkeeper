package org.apache.bookkeeper.bookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class EntryKeyComparatorTest {

    private EntryKey key1;
    private EntryKey key2;
    private EntryKey key3;
    private EntryKey key4;
    private EntryKey key5;

    public EntryKeyComparatorTest(EntryKey key1, EntryKey key2, EntryKey key3, EntryKey key4, EntryKey key5) {
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
        this.key5 = key5;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new EntryKey(1, 1), new EntryKey(1, 2), new EntryKey(2, 1), new EntryKey(2, 2), new EntryKey(2, 2)},
                {new EntryKey(3, 4), new EntryKey(3, 5), new EntryKey(6, 7), new EntryKey(6, 8), new EntryKey(6, 8)},
                {new EntryKey(9, 10), new EntryKey(9, 11), new EntryKey(12, 13), new EntryKey(14, 15), new EntryKey(14, 15)}
        });
    }

    @Test
    public void testEntryKeyComparator() {
        // Test comparator with different instances of EntryKey
        KeyComparator comparator = EntryKey.COMPARATOR;
        assertTrue(comparator.compare(key1, key1) == 0);
        assertTrue(comparator.compare(key1, key2) < 0);
        assertTrue(comparator.compare(key1, key3) < 0);
        assertTrue(comparator.compare(key1, key4) < 0);
        assertTrue(comparator.compare(key2, key3) < 0);
        assertTrue(comparator.compare(key2, key4) < 0);
        assertTrue(comparator.compare(key3, key4) < 0);
        assertTrue(comparator.compare(key4, key3) > 0);
        assertTrue(comparator.compare(key4, key5) == 0);
    }
}
