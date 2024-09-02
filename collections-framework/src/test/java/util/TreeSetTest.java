package util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;

public class TreeSetTest extends SetTest {
    @Override
    @BeforeEach
    void setUp() {
        collection = new TreeSet<>();
        super.setUp();
    }

    @Override
    protected void runTest(Integer[] expected) {
        Integer[] expectedSorted = Arrays.copyOf(expected, expected.length);
        Arrays.sort(expectedSorted);
        Integer[] actualSorted = collection.stream().toArray(Integer[]::new);
        assertArrayEquals(expectedSorted, actualSorted);
        assertEquals(expected.length, collection.size());
    }
}