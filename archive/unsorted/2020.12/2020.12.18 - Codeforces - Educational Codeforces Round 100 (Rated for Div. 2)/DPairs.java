package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.stream.IntStream;

public class DPairs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] b = new int[n];
        boolean[] occupy = new boolean[2 * n];
        for (int i = 0; i < n; i++) {
            b[i] = in.ri() - 1;
            occupy[b[i]] = true;
        }
        Randomized.shuffle(b);
        Arrays.sort(b);
        int[] a = IntStream.range(0, 2 * n).filter(i -> !occupy[i]).toArray();
        int k = BinarySearch.lastFalse((int mid) -> {
            boolean ans = true;
            for (int i = 0; i < mid; i++) {
                if (b[i] >= a[n - mid + i]) {
                    ans = false;
                }
            }
            return !ans;
        }, 0, n);
        int k2 = BinarySearch.lastFalse((int mid) -> {
            boolean ans = true;
            for (int i = 0; i < mid; i++) {
                if (b[n - 1 - i] <= a[mid - 1 - i]) {
                    ans = false;
                }
            }
            return !ans;
        }, 0, n);

        int l = 0;
        int r = k;
        int ll = n - k2;
        int rr = k;
        l = Math.max(l, ll);
        r = Math.min(r, rr);
        int ans = Math.max(0, r - l + 1);
        out.println(ans);
    }
}
