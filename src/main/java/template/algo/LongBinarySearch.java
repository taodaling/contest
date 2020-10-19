package template.algo;

import template.math.DigitUtils;

public abstract class LongBinarySearch {
    public abstract boolean check(long mid);

    public long binarySearch(long l, long r) {
        return binarySearch(l, r, false);
    }

    public long binarySearch(long l, long r, boolean lastFalse) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        while (l < r) {
            long mid = DigitUtils.floorAverage(l, r);
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
