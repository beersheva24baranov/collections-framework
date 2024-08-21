package util;

abstract public class ListTest extends CollectionTest{
    List<Integer> list;
    @Override
    void setUp() {
        super.setUp();
        list = (List<Integer>) collection;
    }
    //TODO's
    //specific tests for list
    //where list is the reference to collection being filled in the method setUp of CollectionTest class
}
