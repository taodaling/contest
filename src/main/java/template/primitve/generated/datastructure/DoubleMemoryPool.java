package template.primitve.generated.datastructure;

import java.util.Arrays;

public class DoubleMemoryPool {
    private double[] data;
    private int indicator;

    public DoubleMemoryPool(int n) {
        data = new double[n];
    }

    public double get(int handle, int offset) {
        return data[handle + offset];
    }

    public void set(int handle, int offset, double x) {
        data[handle + offset] = x;
    }

    public void modify(int handle, int offset, double x) {
        data[handle + offset] += x;
    }

    public double get(int i) {
        return data[i];
    }

    public void set(int i, double x) {
        data[i] = x;
    }

    public void modify(int i, double x) {
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
