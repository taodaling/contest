package template.primitve.generated.datastructure;

public class DoubleSubArray {
    private double[] data;
    private int l;
    private int r;

    public DoubleSubArray(double[] data, int l, int r) {
        this.data = data;
        this.l = l;
        this.r = r;
    }

    public int length() {
        return r - l + 1;
    }

    public double get(int i) {
        return data[l + i];
    }

    public void set(int i, double x) {
        data[i] = x;
    }

    public DoubleSubArray subArray(int ll, int rr) {
        return new DoubleSubArray(data, ll + l, rr + l);
    }
}
