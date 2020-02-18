package template.primitve.generated.datastructure;

public class IntegerSubArray {
    private int[] data;
    private int l;
    private int r;

    public IntegerSubArray(int[] data, int l, int r) {
        this.data = data;
        this.l = l;
        this.r = r;
    }

    public int length() {
        return r - l + 1;
    }

    public int get(int i) {
        return data[l + i];
    }

    public void set(int i, int x) {
        data[i] = x;
    }

    public IntegerSubArray subArray(int ll, int rr) {
        return new IntegerSubArray(data, ll + l, rr + l);
    }
}
