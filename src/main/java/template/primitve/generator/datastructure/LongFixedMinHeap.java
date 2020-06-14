package template.primitve.generated.datastructure;

import java.util.Arrays;

public class LongFixedMinHeap {
    private long[] data;
    private static long[] buf = new long[1 << 8];

    public LongFixedMinHeap(int cap) {
        if (cap <= 0 || cap > buf.length) {
            throw new IllegalArgumentException();
        }
        data = new long[cap];
        clear();
    }

    public void addAll(LongFixedMinHeap heap) {
        int i = 0;
        int j = 0;
        while (i + j < data.length) {
            if (j >= heap.data.length || data[i] <= heap.data[j]) {
                buf[i + j] = data[i];
                i++;
            } else {
                buf[i + j] = heap.data[j];
                j++;
            }
        }
        System.arraycopy(buf, 0, data, 0, data.length);
    }

    public void clear() {
        Arrays.fill(data, Long.MAX_VALUE);
    }

    public long get(int i) {
        return data[i];
    }

    public void add(long x) {
        if (data[data.length - 1] <= x) {
            return;
        }
        int j = data.length - 1;
        while (j > 0 && data[j - 1] > x) {
            data[j] = data[j - 1];
            j--;
        }
        data[j] = x;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
