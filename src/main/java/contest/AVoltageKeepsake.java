package contest;

import template.algo.DoubleBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.stream.Stream;

public class AVoltageKeepsake {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            b[i] = in.readInt();
        }

        long sum = Arrays.stream(a).mapToLong(Long::valueOf).sum();
        if (sum <= p) {
            out.println(-1);
            return;
        }

        double[] cost = new double[n];
        DoubleBinarySearch dbs = new DoubleBinarySearch(1e-12, 1e-12) {

            public boolean check(double mid) {
                for(int i = 0; i < n; i++){
                    cost[i] = Math.max(0, a[i] - b[i] / mid);
                }
                double req = DigitUtils.sum(i -> cost[i], 0, n - 1);
                return req >= p;
            }
        };

        double ans = dbs.binarySearch(0, 1e20);
        out.println(ans);
    }
}
