package by.it.group510901.sidorov.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private E[] heap = (E[]) new Object[16];
    private int size = 0;

    // Вспомогательные методы кучи

    // Сравнение двух элементов (они должны быть Comparable)
    private int compare(E a, E b) {
        return ((Comparable<E>) a).compareTo(b);
    }

    // "Всплытие" элемента
    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) / 2; // Индекс родителя
            E p = heap[parent];
            if (compare(x, p) >= 0) {
                break;
            }
            heap[k] = p;
            k = parent;
        }
        heap[k] = x;
    }

    // "Погружение" элемента
    private void siftDown(int k, E x) {
        int half = size / 2; // Граница между элементами с и без родителей
        while (k < half) {
            int child = 2 * k + 1; // Индекс левого ребёнка
            E c = heap[child];
            int right = child + 1; // Индекс правого ребёнка
            if (right < size && compare(c, heap[right]) > 0) { // Если левый больше правого, сохраняем правого
                child = right;
                c = heap[child];
            }
            if (compare(x, c) <= 0) {
                break;
            }
            heap[k] = c;
            k = child;
        }
        heap[k] = x;
    }

    // Постройка кучи из массива
    private void heapify() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i, heap[i]);
        }
    }

    // Обязательные методы

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(heap[i]);
        }
        return sb.append("]").toString();
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
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (heap[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (size == heap.length) {
            E[] bigger = (E[]) new Object[heap.length * 2];
            for (int i = 0; i < size; i++) {
                bigger[i] = heap[i];
            }
            heap = bigger;
        }
        size++;
        siftUp(size - 1, element);
        return true;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        return heap[0];
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        E result = heap[0];
        size--;
        E last = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0, last);
        }
        return result;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            add(e);
            changed = true;
        }
        return changed;
    }

    // Удаляет элементы, для которых (c.contains(e) == removeFound)
    // removeAll: removeFound = true, retainAll: removeFound = false
    private boolean removeByCollection(Collection<?> c, boolean removeFound) {
        int newSize = 0;
        for (int i = 0; i < size; i++) {
            if (c.contains(heap[i]) != removeFound) {
                heap[newSize] = heap[i];
                newSize++;
            }
        }
        if (newSize == size) {
            return false;
        }
        for (int i = newSize; i < size; i++) {
            heap[i] = null;
        }
        size = newSize;
        heapify();
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeByCollection(c, true);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeByCollection(c, false);
    }

    @Override public boolean remove(Object o) {throw new UnsupportedOperationException();}
    @Override public Iterator<E> iterator() {throw new UnsupportedOperationException();}
    @Override public Object[] toArray() {throw new UnsupportedOperationException();}
    @Override public <T> T[] toArray(T[] a) {throw new UnsupportedOperationException();}
}