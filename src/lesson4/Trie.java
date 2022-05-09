package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        SortedMap<Character, Node> children = new TreeMap<>();
    }

    private final Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     *
     * Трудоемкость: O(1)
     * Ресурсоемкость: O(N)
     *
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    private class TrieIterator implements Iterator<String> {

        private final StringBuilder s = new StringBuilder();
        private final Deque<Iterator<Map.Entry<Character, Node>>> stack;
        private Iterator<Map.Entry<Character, Node>> del;

        private TrieIterator() {
            stack = new ArrayDeque<>();
            Iterator<Map.Entry<Character, Node>> iter = root.children.entrySet().iterator();
            if (iter.hasNext()) {
                stack.push(iter);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        // Трудоёмкость равна трудоёмкости операций PrefixTree:
        // a - мощность алфавита
        // O(maxLength * log(a)) - в худшем случае
        // O(averageLength * log(a)) - в среднем

        @Override
        public String next() {
            Iterator<Map.Entry<Character, Node>> current = stack.pop();
            Iterator<Map.Entry<Character, Node>> previous = current;
            del = current;

            while (current.hasNext()) {
                Map.Entry<Character, Node> next = current.next();
                if (current.hasNext())
                    previous = current;
                s.append(next.getKey());
                stack.push(current);
                current = next.getValue().children.entrySet().iterator();
            }

            del = previous;
            String str = s.substring(0, s.length() - 1);

            if (s.isEmpty())
                throw new NoSuchElementException();

            s.deleteCharAt(s.length() - 1);
            while (stack.size() > 0 && !stack.peek().hasNext()) {
                if (stack.size() == 1 && !stack.peek().hasNext()) {
                    stack.pop();
                    break;
                }
                stack.pop();
                s.deleteCharAt(s.length() - 1);
            }
            return str;
        }

        @Override
        public void remove() {
            if (del == null)
                throw new IllegalStateException();
            del.remove();
            size--;
        }
    }
}