package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    public int height() {
        return height(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    //Затраты по памяти-O(1)
    //Трудоёмкость-O(высота бинарного дерева),самый быстрый случай O(log(n))
    @Override
    public boolean remove(Object o) {
        if (root == null)
            return false;
        root = remove(root, (T) o);
        size--;
        return true;
    }

    private Node<T> remove(Node<T> root, T t) {
        if (root == null)
            return null;
        int com = t.compareTo(root.value);
        if (com < 0)
            root.left = remove(root.left, t);
        else if (com > 0)
            root.right = remove(root.right, t);
        else {
            if (root.left != null && root.right != null) {
                Node<T> newRoot = new Node<>(min(root.right).value);
                newRoot.left = root.left;
                newRoot.right = root.right;
                root = newRoot;
                root.right = remove(root.right, root.value);
            } else if (root.left != null)
                root = root.left;
            else root = root.right;
        }
        return root;
    }

    private Node<T> min(Node<T> root) {
        if (root.left == null) {
            return root;
        } else {
            return min(root.left);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {
        private Node<T> current;
        private LinkedList<Node<T>> nodeList;

        private BinaryTreeIterator() {
            nodeList = new LinkedList<>();
            current = root;
            while (current != null) {
                nodeList.addFirst(current);
                current = current.left;
            }
        }

        /**
         * Проверка наличия следующего элемента
         * Средняя
         * Затраты по памяти O(1)
         * Сложность алгоритма O(1)
         */
        @Override
        public boolean hasNext() {
            return !nodeList.isEmpty();
        }

        /**
         * Поиск следующего элемента
         * Средняя
         * Затраты по памяти O(1)
         * Сложность алгоритма O(n)
         */
        @Override
        public T next() {
            if (hasNext())
                current = nodeList.pop();
            Node node = current;
            if (current == null) throw new IllegalArgumentException();
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    nodeList.addFirst(node);
                    node = node.left;
                }
            }
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         * Сложность - O(height)
         * Ресурсоемкость - O(1)
         */
        @Override
        public void remove() {
            if (current != null)
                BinaryTree.this.remove(current.value);
            else throw new NoSuchElementException();

        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
