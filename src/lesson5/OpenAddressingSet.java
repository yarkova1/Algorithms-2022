package lesson5;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;
    // 0x7FFFFFFF = 11111111 11111111 11111111 1111111 >> (31-3) = 111 ( или 100 101 110 001...
    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    private final boolean[] deleted;

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        // 1 << 3 = 1000 (2) -- cap == 8 (10)
        capacity = 1 << bits;
        storage = new Object[capacity];
        deleted = new boolean[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o) && !deleted[index]) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
            if (index == startingIndex(o))
                return false;
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        int deleted_index = -1;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(t) && !deleted[index]) {
                return false;
            }
            if (deleted[index]) {
                deleted_index = index;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        if (deleted_index != -1) {
            index = deleted_index;
        }
        deleted[index] = false;
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Трудоемкость: O(1/(1-size/capacity)))
     * Ресурсоемкость: O(1)
     *
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o) && !deleted[index]) {
                 deleted[index] = true;
                 size--;
                 return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                return false;
            }
            current = storage[index];
        }
        return false;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     *
     * Трудоемкость: O(N), где N - трудоемкость операции next
     * Ресурсоемкость: O(1)
     *
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>();
    }

    private class SetIterator<T> implements Iterator<T> {

        Object next;
        int index = 0;
        int last_index = -1;

        public SetIterator() {
            next = SetIteratorNext();
        }

        public Object SetIteratorNext () {
            while ((index < capacity) && (storage[index] == null || deleted[index])) {
                index++;
            }
            int return_index = index++;
            if (return_index < capacity) {
                return storage[return_index];
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            Object current_next = next;
            last_index = index - 1;
            next = SetIteratorNext();
            return (T) current_next;
        }

        @Override
        public void remove() {
            if (last_index < 0) {
                throw new IllegalStateException();
            }
            deleted[last_index] = true;
            size--;
            last_index = -1;
        }
    }
}
