package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.Map;
import java.util.TreeMap;

public class DNearestCardGame {
    TreeMap<Integer, Integer> map;
    int[] a;
    int n;

    public int step(int mid, int x) {
        int r = n - mid;
        int step = 0;
        if (a[r] > x) {
            int lowbound = x - (a[r] - x);
            Map.Entry<Integer, Integer> ceilEntry = map.ceilingEntry(lowbound);
            step = r - ceilEntry.getValue();
        }
        return step;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        a = new int[n];
        in.populate(a);
        map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            map.put(a[i], i);
        }
        LongPreSum lps = new LongPreSum(i -> a[i], n);
        long[][] ps = new long[2][n];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) {
                if (j > 0) {
                    ps[i][j] += ps[i][j - 1];
                }
                if (j % 2 == i) {
                    ps[i][j] += a[j];
                }
            }
        }


        for (int i = 0; i < q; i++) {
            int x = in.readInt();
            IntBinarySearch ibs = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    return !(step(mid, x) + 1 >= mid);
                }
            };
            int go = ibs.binarySearch(1, DigitUtils.ceilDiv(n, 2), true);
            int remain = n - go * 2;
            long ans = lps.post(n - go);
            if (remain > 0) {
                ans += ps[(remain - 1) % 2][remain - 1];
            }
            out.println(ans);
        }
    }
}
