package util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;

public abstract class ListTest extends CollectionTest {
    List<Integer> list;

    @Override
    void setUp() {
        super.setUp();
        list = (List<Integer>) collection;
    }

    @Test
    @Override
    void addTest() {
        Integer[] expected = { 3, -10, 20, 1, 1, 10, 8, 100, 17, 17 };
        list.add(3, 1);
        list.add(8, 17);
        assertArrayEquals(expected, list.stream().toArray());
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> list.add(13, 20));
    }

    @Test
    @Override
    void removeTest() {
        Integer[] expected = { -10, 20, 1, 10, 8, 100 };
        list.remove(0);
        list.remove(6);
        assertArrayEquals(expected, list.stream().toArray());
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> list.remove(20));
    }

    @Test
    void indexOfTest() {
        list.add(0, null);
        assertEquals(3, list.indexOf(20));
    }

    @Test
    void getTest() {
        assertEquals(100, list.get(6));
        assertThrowsExactly(IndexOutOfBoundsException.class, () -> list.get(20));
    }

    @Test
    void lastIndexOfTest() {
        list.add(3, 20);
        list.add(0, null);
        assertEquals(4, list.lastIndexOf(20));
    }
}