package template.primitve.generated.datastructure;

import java.util.Arrays;

public class IntegerMemoryPool {
    private int[] data;
    private int indicator;

    public IntegerMemoryPool(int n) {
        data = new int[n];
    }

    public int get(int handle, int offset) {
        return data[handle + offset];
    }

    public void set(int handle, int offset, int x) {
        data[handle + offset] = x;
    }

    public void modify(int handle, int offset, int x) {
        data[handle + offset] += x;
    }

    public int get(int i) {
        return data[i];
    }

    public void set(int i, int x) {
        data[i] = x;
    }

    public void modify(int i, int x) {
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
