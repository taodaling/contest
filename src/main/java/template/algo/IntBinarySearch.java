package template.algo;


import template.math.DigitUtils;

public abstract class IntBinarySearch {
    public abstract boolean check(int mid);

    public int binarySearch(int l, int r) {
        return binarySearch(l, r, false);
    }

    public int binarySearch(int l, int r, boolean lastFalse) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        while (l < r) {
            int mid = DigitUtils.floorAverage(l, r);
            if (check(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (lastFalse) {
            if (check(l)) {
                l--;
            }
        }
        return l;
    }
}
