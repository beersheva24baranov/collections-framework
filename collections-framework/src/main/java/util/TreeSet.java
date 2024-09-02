package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

public class TreeSet<T> implements Set<T> {
    private Node<T> root;
    private Comparator<T> comparator;
    int size;

    private static class Node<T> {
        T obj;
        Node<T> parent;
        Node<T> left;
        Node<T> right;

        Node(T obj) {
            this.obj = obj;
        }
    }

    private class TreeSetIterator implements Iterator<T> {
        Node<T> current = getLeastFrom(root);
        Node<T> previous = null;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T res = current.obj;
            previous = current;
            current = getNextCurrent(current);
            return res;
        }

        @Override
        public void remove() {
            if (previous == null) {
                throw new IllegalStateException();
            }
            removeNode(previous);
            previous = null;
        }
    }

    private Node<T> getLeastFrom(Node<T> node) {
        Node<T> current = node;
        Node<T> parent = current.parent;
        while (current != null) {
            parent = current;
            current = current.left;
        }
        return parent;
    }

    private Node<T> getGretestFrom(Node<T> node) {
        Node<T> current = node;
        Node<T> parent = current.parent;
        while (current != null) {
            parent = current;
            current = current.right;
        }
        return parent;
    }

    private Node<T> getNextCurrent(Node<T> node) {
        Node<T> current = node;
        if (current.right != null) {
            current = getLeastFrom(current.right);
        } else {
            current = getGreaterParent(current);
        }
        return current;
    }

    private Node<T> getGreaterParent(Node<T> node) {
        Node<T> current = node;
        T nodeObj = node.obj;
        while (current != null && comparator.compare(nodeObj, current.obj) >= 0) {
            current = current.parent;
        }
        return current;
    }

    private void removeNode(Node<T> node) {
        if (node.left != null && node.right != null) {
            removeJunction(node);
        } else {
            removeNonJunction(node);
        }
        size--;

    }

    private void removeJunction(Node<T> node) {
        Node<T> nodeForChange = getGretestFrom(node.left);
        node.obj = nodeForChange.obj;
        removeNonJunction(nodeForChange);
    }

    private void removeNonJunction(Node<T> node) {
        Node<T> parent = node.parent;
        Node<T> child = getChild(node);
        if (parent != null) {
            if (isRigthChild(node)) {
                parent.right = child;
            } else {
                parent.left = child;
            }
        } else {
            root = child;
        }
        if (child != null) {
            child.parent = parent;
        }
        deleteNode(node);
    }

    private Node<T> getChild(Node<T> node) {
        Node<T> res = null;
        if (node.left != null) {
            res = node.left;
        }
        if (node.right != null) {
            res = node.right;
        }
        return res;
    }

    private boolean isRigthChild(Node<T> node) {
        return node.parent.right == node;
    }

    private void deleteNode(Node<T> node) {
        node.parent = null;
        node.obj = null;
        node.left = null;
        node.right = null;
    }

    public TreeSet(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    private Node<T> getParentOrNode(T pattern) {
        Node<T> current = root;
        Node<T> parent = null;
        int compRes = 0;
        while (current != null && (compRes = comparator.compare(pattern, current.obj)) != 0) {
            parent = current;
            current = compRes > 0 ? current.right : current.left;
        }
        return current == null ? parent : current;
    }

    private Node<T> getNode(T pattern) {
        Node<T> res = getParentOrNode(pattern);
        if (res != null) {
            int compRes = comparator.compare(pattern, res.obj);
            if (compRes != 0) {
                res = null;
            }
        }
        return res;
    }

    private Node<T> getParent(T pattern) {
        Node<T> res = getParentOrNode(pattern);
        if (res != null) {
            int compRes = comparator.compare(pattern, res.obj);
            if (compRes == 0) {
                res = null;
            }
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public TreeSet() {
        this((Comparator<T>) Comparator.naturalOrder());
    }

    @Override
    public boolean add(T obj) {
        boolean res = false;
        if (!contains(obj)) {
            res = true;
            Node<T> node = new Node<>(obj);
            if (root == null) {
                addRoot(node);
            } else {
                addAfterParent(node);
            }
            size++;
        }
        return res;
    }

    private void addAfterParent(Node<T> node) {
        Node<T> parent = getParent(node.obj);
        if (comparator.compare(node.obj, parent.obj) > 0) {
            parent.right = node;
        } else {
            parent.left = node;
        }
        node.parent = parent;
    }

    private void addRoot(Node<T> node) {
        root = node;
    }

    @Override
    public boolean remove(T pattern) {
        boolean res = false;
        Node<T> node = getNode(pattern);
        if (node != null) {
            res = true;
            removeNode(node);
        }
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T pattern) {
        return getNode(pattern) != null;
    }

    @Override
    public Iterator<T> iterator() {
        return new TreeSetIterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(Object pattern) {
        T obj = (T) pattern;
        Node<T> node = getNode(obj);
        return node != null ? node.obj : null;
    }
}