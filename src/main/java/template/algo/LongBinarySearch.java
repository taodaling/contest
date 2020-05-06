package template.algo;

import template.math.DigitUtils;

public abstract class LongBinarySearch {
    public abstract boolean check(long mid);

    public long binarySearch(long l, long r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        while (l < r) {
            long mid = DigitUtils.average(l, r);
            if (check(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }
}
