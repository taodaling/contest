package template.algo;

public abstract class IntBinarySearch {
    public abstract boolean check(int mid);

    public int binarySearch(int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        while (l < r) {
            int mid = (l + r) >>> 1;
            if (check(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }
}
