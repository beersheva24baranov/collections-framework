package util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

public class TreeSet<T> implements SortedSet<T> {
    private Node<T> root;
    private Comparator<T> comparator;
    private String printingSymbol = " ";
    int size;
    private int countOfSymbols = 3;

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
            previous = current;
            current = getNextCurrent(current);
            return previous.obj;
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

    public void setPrintingSymbol(String printingSymbol) {
        this.printingSymbol = printingSymbol;
    }

    public void setCountOfSymbols(int countOfSymbols) {
        this.countOfSymbols = countOfSymbols;
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
        return node.right != null ? getLeastFrom(node.right) : getGreaterParent(node);
    }

    private Node<T> getGreaterParent(Node<T> node) {
        Node<T> current = node;
        T nodeObj = node.obj;
        while (current != null && comparator.compare(nodeObj, current.obj) >= 0) {
            current = current.parent;
        }
        return current;
    }

    private Node<T> getLessParent(Node<T> node) {
        Node<T> current = node;
        T nodeObj = node.obj;
        while (current != null && comparator.compare(nodeObj, current.obj) <= 0) {
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
        return node.left != null ? node.left : node.right;
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

    @Override
    public T first() {
        Node<T> node = getLeastFrom(root);
        return  node == null ? null : node.obj;
    }

    @Override
    public T last() {
        Node<T> node = getGretestFrom(root);
        return  node == null ? null : node.obj;
    }

    @Override
    public T floor(T key) {
        Node<T> res = getFloorNode(key);
        return res != null ? res.obj : null;
    }

    private Node<T> getFloorNode(T key) {
        Node<T> node = getNode(key);
        Node<T> res = null;
        if (node != null) {
            res = node;
        } else {
            Node<T> parent = getParent(key);
            if (comparator.compare(key, parent.obj) < 0) {
                res = getLessParent(parent);
            } else {
                res = parent;
            }
        }   
        return res;
    }

    @Override
    public T ceiling(T key) {
        Node<T> res = getCeilingNode(key);
        return res != null ? res.obj : null;
    }

    private Node<T> getCeilingNode(T key) {
        Node<T> node = getNode(key);
        Node<T> res = null;
        if (node != null) {
            res = node;
        } else {
            Node<T> parent = getParent(key);
            if (comparator.compare(key, parent.obj) > 0) {
                res = getGreaterParent(parent);
            } else {
                res = parent;
            }
        }
        return res;
    }

    @Override
    public SortedSet<T> subSet(T keyFrom, T keyTo) {
        if(comparator.compare(keyFrom, keyTo) > 0) {
            throw new IllegalArgumentException();
           }
        SortedSet<T> res = new TreeSet<>(comparator);
        Node<T> node = getFloorNode(keyFrom);
        while (node != null && comparator.compare(node.obj, keyTo) < 0) {
            res.add(node.obj);
            node = getNextCurrent(node);
        }
        return res;
    }

    public void displayTreeRotated() {
        displayTreeRotated(root, 0);
    }

    private void displayTreeRotated(Node<T> root, int level) {
        if (root != null) {
            displayTreeRotated(root.right, level + 1);
            displayRootObject(root, level);
            displayTreeRotated(root.left, level + 1);
        }
    }

    private void displayRootObject(Node<T> root, int level) {
        System.out.printf("%s%s\n", printingSymbol.repeat(level * countOfSymbols), root.obj.toString());
    }

    public void displayTreeParentChildren() {
        displayTreeParentChildren(root, 0);
    }

    private void displayTreeParentChildren(Node<T> root, int level) {
        if (root != null) {
            displayRootObject(root, level);
            displayTreeParentChildren(root.left, level + 1);
            displayTreeParentChildren(root.right, level + 1);
        }
    }

    public int width() {
        return width(root);
    }

    private int width(Node<T> root) {
        int res = 0;
        if (root != null) {
            res = (root.left == null && root.right == null) ? 1 : width(root.left) + width(root.right);
        }
        return res;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> root) {
        int res = 0;
        if (root != null) {
            int heightLeft = height(root.left);
            int heightRight = height(root.right);
            res = heightLeft > heightRight ? heightLeft: heightRight;
            res++;
        }
        return res;
    }

    public void inversion() {
        inversion(root);
        comparator = comparator.reversed();
    }

    private void inversion(Node<T> root) {
        if (root != null) {
            Node<T> tmp = root.left;
            root.left = root.right;
            root.right = tmp;
            tmp = null;
            inversion(root.left);
            inversion(root.right);
        }
    }

    public void balance() {
        Node<T>[] nodes = getSortedNodesArray();
        root = balanceArray(nodes, 0, nodes.length - 1, null);
    }

    @SuppressWarnings("unchecked")
    private Node<T>[] getSortedNodesArray() {
        Node<T> current = getLeastFrom(root);
        Node<T>[] res = new Node[size]; 
        for (int i = 0; i < res.length; i++) {
            res[i] = current;
            current = getNextCurrent(current);
        }
        return res;
    }

    private Node<T> balanceArray(Node<T>[] nodes, int left, int right, Node<T> parent) {
        Node<T> root = null;
        if (left <= right) {
            int middle = left + (right - left) / 2;
            root = nodes[middle];
            root.parent = parent;
            root.left = balanceArray(nodes, left, middle - 1, root);
            root.right = balanceArray(nodes, middle + 1, right, root);
        }
        return root;
    }
}