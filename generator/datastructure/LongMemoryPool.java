package template.primitve.generated.datastructure;

import java.util.Arrays;

public class LongMemoryPool {
    private long[] data;
    private int indicator;

    public LongMemoryPool(int n) {
        data = new long[n];
    }

    public long get(int handle, int offset) {
        return data[handle + offset];
    }

    public void set(int handle, int offset, long x) {
        data[handle + offset] = x;
    }

    public void modify(int handle, int offset, long x) {
        data[handle + offset] += x;
    }

    public long get(int i) {
        return data[i];
    }

    public void set(int i, long x) {
        data[i] = x;
    }

    public void modify(int i, long x) {
        data[i] += x;
    }

    public void reset(boolean fillZero) {
        if (fillZero) {
            Arrays.fill(data, 0, indicator, 0);
        }
        indicator = 0;
    }

    public int alloc(int n) {
        int ans = indicator;
        indicator += n;
        if (indicator > data.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return ans;
    }
}
