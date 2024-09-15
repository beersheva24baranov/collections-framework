package telran.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//{3, -10, 20, 1, 10, 8, 100 , 17};
public class TreeSetTest extends SortedSetTest{
    TreeSet<Integer> treeSet;
    @BeforeEach
    @Override
    void setUp() {
        collection = new TreeSet<>();
        super.setUp();
        treeSet = (TreeSet<Integer>)collection;
    }
    @Override
    protected void runTest(Integer[] expected) {
        Integer[] expectedSorted = Arrays.copyOf(expected, expected.length);
        Arrays.sort(expectedSorted);
        Integer[] actual = collection.stream().toArray(Integer[]::new);
        
        assertArrayEquals(expectedSorted, actual);
        assertEquals(expected.length, collection.size());
    }
    @Test
    void displayTreeRotatedTest(){
        treeSet.setSymbolsPerLevel(5);
        treeSet.displayTreeRotated();
    }
    @Test
    void displayTreeParentChildrenTest(){
        treeSet.setSymbolsPerLevel(5);
        treeSet.displayTreeParentChildren();
    }
    @Test
    void widthtest() {
        assertEquals(4, treeSet.width());
    }
    @Test
    void heightTest() {
        assertEquals(4, treeSet.height());
    }
    @Test
    void inversionTest() {
            Integer[] expected = {100, 20, 17, 10, 8, 3, 1, -10};
            treeSet.inversion();
            Integer[] actual = treeSet.stream().toArray(Integer[]::new);
            assertArrayEquals(expected, actual);
            assertTrue(treeSet.contains(100));
    }

}
