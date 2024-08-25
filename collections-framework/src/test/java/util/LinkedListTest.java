package util;
import org.junit.jupiter.api.*;
public class LinkedListTest extends ListTest {

    @Override
    @BeforeEach
    void setUp() {
        collection = new LinkedList<>();
        super.setUp();
    }
}