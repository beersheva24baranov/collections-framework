package util;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;

public class HashSetTest extends SetTest {
    @BeforeEach
    @Override
    void setUp() {
        collection = new HashSet<>();
        super.setUp();
    }

    @Override
    protected void runTest(Integer[] expected) {
        Integer[] expectedSorted = Arrays.copyOf(expected, expected.length);
        Arrays.sort(expectedSorted);
        Integer[] actualSorted = collection.stream().toArray(Integer[]::new);
        Arrays.sort(actualSorted);
        assertArrayEquals(expectedSorted, actualSorted);
        assertEquals(expected.length, collection.size());
    }
}