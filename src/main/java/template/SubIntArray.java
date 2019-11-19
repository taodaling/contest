package template;

public class SubIntArray {
    private int[] data;
    private int l;
    private int r;

    public SubIntArray(int[] data, int l, int r) {
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

    public SubIntArray subarray(int ll, int rr) {
        return new SubIntArray(data, ll + l, rr + l);
    }
}
