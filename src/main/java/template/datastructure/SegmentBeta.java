package template.datastructure;

public class SegmentBeta {
    private int[] segments;
    private int alloc;

    private int left(int x) {
        return x << 1;
    }

    private int right(int x) {
        return (x << 1) | 1;
    }

    public SegmentBeta(int l, int r) {
        int range = r - l + 1;
    }

    private void pushUp(int seg) {
    }

    private void pushDown(int seg) {
    }

    private void build(int l, int r) {
        int m = (l + r) >> 1;
        build(l, m - 1);

    }
}
