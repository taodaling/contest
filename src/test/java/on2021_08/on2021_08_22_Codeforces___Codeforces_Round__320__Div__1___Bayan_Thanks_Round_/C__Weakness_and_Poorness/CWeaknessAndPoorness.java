package on2021_08.on2021_08_22_Codeforces___Codeforces_Round__320__Div__1___Bayan_Thanks_Round_.C__Weakness_and_Poorness;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.SumOfFloat;

import java.util.function.DoublePredicate;

public class CWeaknessAndPoorness {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        double[] b = new double[n];

        DoublePredicate pred = m -> {
            for (int i = 0; i < n; i++) {
                b[i] = a[i] + m;
            }
            double[] res = lcs(b);
            return res[0] >= res[1];
        };
        double l = -1e4;
        double r = 1e4;
        for (int i = 0; i < 100; i++) {
            double m = (l + r) / 2;
            if (pred.test(m)) {
                r = m;
            } else {
                l = m;
            }
        }
        for (int i = 0; i < n; i++) {
            b[i] = a[i] + l;
        }
        double[] res = lcs(b);
        out.println(res[0]);
    }

    public double[] lcs(double[] data) {
        int n = data.length;
        double[] ans = new double[2];
        SumOfFloat last = new SumOfFloat();
        for (int i = 0; i < n; i++) {
            last.add(data[i]);
            if (last.sum() < 0) {
                last.reset();
            }
            ans[0] = Math.max(ans[0], last.sum());
        }
        last.reset();
        for (int i = 0; i < n; i++) {
            last.add(data[i]);
            if (last.sum() > 0) {
                last.reset();
            }
            ans[1] = Math.min(ans[1], last.sum());
        }
        ans[1] = -ans[1];
        return ans;
    }
}
