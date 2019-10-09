package template;

import java.util.Comparator;

/**
 * Created by dalt on 2018/5/31.
 */
public class MinHeap<T> {
    final Key<T>[] data;
    final Comparator<T> comparator;
    int size;
    int cap;

    public MinHeap(int cap, Comparator<T> comparator) {
        this.cap = cap;
        data = new Key[cap + 1];
        this.comparator = comparator;
        size = 0;
    }

    public Key<T> add(T val) {
        Key<T> key = new Key<>();
        key.val = val;
        data[++size] = key;
        key.index = size;

        shiftUpward(key);

        return key;
    }

    public void shiftUpward(Key<T> key) {
        int index = key.index;
        T val = key.val;
        while (index != 1) {
            int fIndex = index >> 1;
            if (comparator.compare(val, data[fIndex].val) >= 0) {
                break;
            }
            data[index] = data[fIndex];
            data[index].index = index;
            index = fIndex;
        }
        data[index] = key;
        key.index = index;
    }

    public Key<T> peek() {
        return data[1];
    }

    public Key<T> remove() {
        Key<T> key = data[1];
        data[1] = data[size--];
        data[1].index = 1;

        shiftDownward(data[1]);

        return key;
    }

    public void shiftDownward(Key<T> key) {
        int index = key.index;
        T val = key.val;
        int until = size >> 1;
        while (index <= until) {
            int lIndex = index << 1;
            int rIndex = lIndex + 1;
            int minIndex = rIndex > size || comparator.compare(data[lIndex].val, data[rIndex].val) <= 0 ?
                    lIndex : rIndex;

            if (comparator.compare(val, data[minIndex].val) <= 0) {
                break;
            }
            data[index] = data[minIndex];
            data[index].index = index;
            index = minIndex;
        }
        data[index] = key;
        key.index = index;
    }

    public int size() {
        return size;
    }

    public static class Key<T> {
        T val;
        int index;
    }
}
