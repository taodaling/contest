package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class DCandiesForChildren {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long l = in.readLong();
        long r = in.readLong();
        long k = in.readLong();
        long dist = DigitUtils.mod(r - l, n) + 1;

        long max = -1;
        int threshold = 1000000;

        if (n < threshold) {
            for (int i = 0; i <= n; i++) {
                long remain = k % (i + n);
                if (dist == n) {
                    if(remain != 0){
                        continue;
                    }
                    remain = Math.min(i + n, k);
                }
                if (remain - dist <= i && remain >= dist && remain <= 2 * dist &&
                        remain - dist + (n - dist) >= i) {
                    max = Math.max(max, i);
                }
            }
        } else {
            if (dist <= k && k <= dist * 2) {
                max = Math.max(max, k - dist + n - dist);
            }
            for (int i = 1; i * n <= k; i++) {
                if (i * 2 * n + dist * 2 < k) {
                    continue;
                }
                long remain = k - i * n - dist;
                long tMax = n;
                tMax = Math.min(tMax, remain / i);
                tMax = Math.min(tMax, (remain + n - dist) / (i + 1));
                long tMin = DigitUtils.ceilDiv(remain, i + 1);
                tMin = Math.max(tMin, DigitUtils.ceilDiv(remain - dist, i));

                if (tMin <= tMax) {
                    max = Math.max(tMax, max);
                }
            }
        }

        //take one last time
        k++;
        if (n < threshold) {
            for (int i = 1; i <= n; i++) {
                long remain = k % (i + n);
                if (dist == n) {
                    if(remain != 0){
                        continue;
                    }
                    remain = Math.min(i + n, k);
                }
                if (remain - dist <= i && remain >= dist + 1 && remain <= 2 * dist &&
                        remain - dist + (n - dist) >= i) {
                    max = Math.max(max, i);
                }
            }
        } else {
            if (dist + 1 <= k && k <= dist * 2) {
                max = Math.max(max, k - dist + n - dist) ;
            }
            for (int i = 1; i * n <= k; i++) {
                if (i * 2 * n + dist * 2 < k) {
                    continue;
                }
                long remain = k - i * n - dist;
                long tMax = n;
                tMax = Math.min(tMax, remain / i);
                tMax = Math.min(tMax, (remain + n - dist) / (i + 1));
                long tMin = DigitUtils.ceilDiv(remain, i + 1);
                tMin = Math.max(tMin, DigitUtils.ceilDiv(remain - dist, i));
                tMax = Math.min(tMax, DigitUtils.ceilDiv(k - 1, i));

                if (tMin <= tMax) {
                    max = Math.max(tMax, max);
                }
            }
        }

        out.println(max);
    }
}
