package template.primitve.generated.datastructure;

import template.utils.SequenceUtils;

/**
 * Minimum priority queue
 */
public class IntegerPriorityQueue implements Cloneable {
    private int[] data;
    private IntegerComparator comparator;
    private int size;

    public IntegerPriorityQueue(int n, IntegerComparator comparator) {
        this.data = new int[n + 1];
        this.comparator = comparator;
    }

    private int left(int i) {
        return i << 1;
    }

    private int right(int i) {
        return (i << 1) | 1;
    }

    private int parent(int i) {
        return i >> 1;
    }

    public int size() {
        return this.size;
    }

    public void add(int x) {
        data[++size] = x;
        shiftUp(size);
    }

    public int peek() {
        return data[1];
    }

    private void shiftUp(int index) {
        while (index != 1) {
            int pid = parent(index);
            if (comparator.compare(data[pid], data[index]) <= 0) {
                break;
            }
            SequenceUtils.swap(data, index, pid);
            index = pid;
        }
    }

    private void shiftDown(int index) {
        while (true) {
            int lid = left(index);
            int rid = right(index);
            if (lid > size) {
                break;
            }
            int challenger = lid;
            if (rid <= size && comparator.compare(data[rid], data[lid]) < 0) {
                challenger = rid;
            }
            if (comparator.compare(data[challenger], data[index]) >= 0) {
                break;
            }
            SequenceUtils.swap(data, index, challenger);
            index = challenger;
        }
    }

    public int pop() {
        int ans = data[1];
        SequenceUtils.swap(data, 1, size);
        size--;
        shiftDown(1);
        return ans;
    }

    public IntegerIterator iterator() {
        return new IntegerIterator() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public int next() {
                return data[++index];
            }
        };
    }

    public void clear() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public IntegerPriorityQueue clone() {
        try {
            IntegerPriorityQueue c = (IntegerPriorityQueue) super.clone();
            c.data = data.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        IntegerPriorityQueue queue = clone();
        while (!queue.isEmpty()) {
            builder.append(queue.pop()).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
