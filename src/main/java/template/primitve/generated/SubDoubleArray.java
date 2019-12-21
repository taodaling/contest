package template.primitve.generated;

public class SubDoubleArray {
    private double[] data;
    private int l;
    private int r;

    public SubDoubleArray(double[] data, int l, int r) {
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

    public SubDoubleArray subArray(int ll, int rr) {
        return new SubDoubleArray(data, ll + l, rr + l);
    }
}
