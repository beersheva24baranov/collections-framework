package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashSet<T> implements Set<T> {
    private static final int DEFAULT_HASH_TABLE_LENGTH = 16;
    private static final float DEFAULT_FACTOR = 0.75f;
    List<T>[] hashTable;
    float factor;
    int size;

    @SuppressWarnings("unchecked")
    public HashSet(int hashTableLength, float factor) {
        hashTable = new List[hashTableLength];
        this.factor = factor;
    }

    public HashSet() {
        this(DEFAULT_HASH_TABLE_LENGTH, DEFAULT_FACTOR);
    }

    @Override
    public boolean add(T obj) {
        boolean res = false;
        if (!contains(obj)) {
            res = true;
            if (size >= hashTable.length * factor) {
                hashTableReallocation();
            }
            addObjInHashTable(obj, hashTable);
            size++;
        }
        return res;
    }

    private void addObjInHashTable(T obj, List<T>[] table) {
        int index = getIndex(obj, hashTable.length);
        List<T> list = table[index];
        if (list == null) {
            list = new ArrayList<>(3);
        }
        list.add(obj);
        table[index] = list;
    }

    private int getIndex(T obj, int length) {
        int hashCode = obj.hashCode();
        return Math.abs(hashCode % length);
    }

    @SuppressWarnings("unchecked")
    private void hashTableReallocation() {
        List<T>[] tempTable = new List[hashTable.length * 2];
        for (List<T> list : hashTable) {
            if (list != null) {
                list.forEach(obj -> addObjInHashTable(obj, tempTable));
                list.clear(); // ??? for testing if it doesn't work remove this statement
            }
        }
        hashTable = tempTable;
    }

    @Override
    public boolean remove(T pattern) {
        int index = getIndex(pattern, hashTable.length);
        List<T> list = hashTable[index];
        boolean res = list.remove(pattern);
        if (res) {
            size--;
        }
        clearIfListIsEmpty(index);
        return res;
    }

    private void clearIfListIsEmpty(int index) {
        if (hashTable[index].isEmpty()) {
            hashTable[index] = null;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T pattern) {
        int index = getIndex(pattern, hashTable.length);
        List<T> list = hashTable[index];
        return list != null && list.contains(pattern);
    }

    @Override
    public Iterator<T> iterator() {
        return new HashSetIterator();
    }

    private class HashSetIterator implements Iterator<T> {
        int indexHashTable = -1;
        int prevIndexHashTable = -1;
        boolean flagNext = false;
        Iterator<T> currentIterator = nextNonEmptyListIterator();
        Iterator<T> prevIterator;

        @Override
        public boolean hasNext() {
            return currentIterator != null;
        }

        private Iterator<T> nextNonEmptyListIterator() {
            indexHashTable++;
            while (indexHashTable < hashTable.length && hashTable[indexHashTable] == null) {
                indexHashTable++;
            }
            return indexHashTable == hashTable.length ? null : hashTable[indexHashTable].iterator();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            flagNext = true;
            T res = currentIterator.next();
            if (!currentIterator.hasNext()) {
                prevIterator = currentIterator;
                prevIndexHashTable = indexHashTable;
                currentIterator = nextNonEmptyListIterator();
            } else {
                prevIterator = null;
            }
            return res;
        }

        @Override
        public void remove() {
            if (!flagNext) {
                throw new IllegalStateException();
            }
            if (prevIterator == null) {
                currentIterator.remove();
                clearIfListIsEmpty(indexHashTable);
            } else {
                prevIterator.remove();
                clearIfListIsEmpty(prevIndexHashTable);
            }
            size--;
            flagNext = false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Object obj) {
        T res = null;
        T pattern = (T) obj;
        int index = getIndex(pattern, hashTable.length);
        List<T> list = hashTable[index];
        if (list != null) {
            int indexInList = hashTable[index].indexOf(pattern);
            if (indexInList >= 0) {
                res = hashTable[index].get(indexInList);
            }
        }
        return res;
    }
}