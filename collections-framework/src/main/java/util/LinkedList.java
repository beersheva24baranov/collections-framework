package util;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements List<T> {
    static class Node<T> {
        T obj;
        Node<T> next;
        Node<T> prev;

        Node(T obj) {
            this.obj = obj;
        }
    }

    Node<T> head;
    Node<T> tail;
    int size = 0;

    private Node<T> getNode(int index) {
        return index > size / 2 ? getNodeFromTail(index) : getNodeFromHead(index);
    }

    private Node<T> getNodeFromHead(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    private Node<T> getNodeFromTail(int index) {
        Node<T> current = tail;
        for (int i = size - 1; i > index; i--) {
            current = current.prev;
        }
        return current;
    }

    void addNode(Node<T> node, int index) {
        if (index == 0) {
            addHead(node);
        } else if (index == size) {
            addTail(node);
        } else {
            addMiddle(node, index);
        }
        size++;
    }

    private void addMiddle(Node<T> node, int index) {
        Node<T> nodeAfter = getNode(index);
        Node<T> nodeBefore = nodeAfter.prev;
        node.prev = nodeBefore;
        nodeBefore.next = node;
        node.next = nodeAfter;
        nodeAfter.prev = node;
    }

    private void addTail(Node<T> node) {
        tail.next = node;
        node.prev = tail;
        node.next = null;
        tail = node;
    }

    private void addHead(Node<T> node) {
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            node.prev = null;
            head = node;
        }
    }

    Node<T> removeNode(Node<T> node) {
        Node<T> res;
        if (node == head) {
            res = removeHead();
        } else if (node == tail) {
            res = removeTail();
        } else {
            res = removeMiddle(node);
        }
        size--;
        return res;
    }

    private Node<T> removeMiddle(Node<T> nodeToRemove) {
        Node<T> nodeBefore = nodeToRemove.prev;
        Node<T> nodeAfter = nodeToRemove.next;
        nodeBefore.next = nodeAfter;
        nodeAfter.prev = nodeBefore;
        return nodeToRemove;
    }

    private Node<T> removeTail() {
        Node<T> res = tail;
        tail = tail.prev;
        tail.next = null;
        return res;
    }

    private Node<T> removeHead() {
        Node<T> res = head;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        return res;
    }

    @Override
    public boolean add(T obj) {
        Node<T> node = new Node<T>(obj);
        addNode(node, size);
        return true;
    }

    @Override
    public boolean remove(T pattern) {
        boolean isFound = false;
        int index = indexOf(pattern);
        if (index >= 0) {
            isFound = true;
            remove(index);
        }
        return isFound;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T pattern) {
        return indexOf(pattern) > -1;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T>{
        int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(currentIndex++);
        }
    }

    private void checkBounds(int index, boolean inclusiveBounds) {
        int limit = inclusiveBounds ? size : size - 1;
        if (index > limit || index < 0) {
            throw new IndexOutOfBoundsException(String.format("Index must be in range from 0 to %d", limit));
        }
    }

    @Override
    public void add(int index, T obj) {
        checkBounds(index, true);
        Node<T> node = new Node<T>(obj);
        addNode(node, index);
    }

    @Override
    public T remove(int index) {
        checkBounds(index, false);
        Node<T> node = removeNode(getNode(index));
        return node.obj;
    }

    @Override
    public T get(int index) {
        checkBounds(index, false);
        Node<T> node = getNode(index);
        return node.obj;
    }

    @Override
    public int indexOf(T pattern) {
        int index = 0;
        Node<T> current = head;
        while (current != null && current.obj != pattern) {
            current = current.next;
            index++;
        }
        return current == null ? -1 : index;
    }

    @Override
    public int lastIndexOf(T pattern) {
        int index = size - 1;
        Node<T> current = tail;
        while (current != null && current.obj != pattern) {
            current = current.prev;
            index--;
        }
        return current == null ? -1 : index;
    }
}