package util;

import org.junit.jupiter.api.BeforeEach;

public class TreeSetStandartTest extends SortedSetTest {
    @BeforeEach
    @Override
    void setUp() {
        collection = new TreeSetStandard<>();
        super.setUp();
    }
}