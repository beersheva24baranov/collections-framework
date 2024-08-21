package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;


import org.junit.jupiter.api.Test;
public abstract class CollectionTest {
    protected Collection<Integer> collection;
    Integer[] array = {3, -10, 20, 1, 10, 8, 100 , 17};
    void setUp() {
        Arrays.stream(array).forEach(collection::add);
    }
    @Test
    void addTest() {
        assertTrue(collection.add(200));
        assertTrue(collection.add(17));
        assertEquals(array.length + 2, collection.size());
    }
    @Test
    void sizeTest() {
        assertEquals(array.length, collection.size());
    }
    
    @Test
    void removeTest() {
        Integer[] expected = { -10, 20, 1, 10, 8, 100 };
        assertFalse(collection.remove(4));
        assertTrue(collection.remove(3));
        assertTrue(collection.remove(17));
        assertArrayEquals(expected, collection.stream().toArray());
    }

    @Test
    void isEmptyTest() {
        assertFalse(collection.isEmpty());
        assertTrue(collection.remove(3));
        assertTrue(collection.remove(-10));
        assertTrue(collection.remove(20));
        assertTrue(collection.remove(1));
        assertTrue(collection.remove(10));
        assertTrue(collection.remove(8));
        assertTrue(collection.remove(100));
        assertTrue(collection.remove(17));
        assertTrue(collection.isEmpty());
    }

    @Test
    void containsTest() {
        assertTrue(collection.contains(-10));
        assertFalse(collection.contains(-11));
    }

    @Test
    void iteratorTest() {
        Iterator<Integer> iterator = collection.iterator();
        Integer[] expected = { 3, -10, 20, 1, 10, 8, 100, 17 };
        Integer[] actual = new Integer[expected.length];
        int i = 0;
        while (iterator.hasNext()) {
            actual[i++] = iterator.next();
        }
        assertArrayEquals(expected, actual);
    }
}
