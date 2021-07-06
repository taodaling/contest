package on2021_06.on2021_06_26_Codeforces___Codeforces_Round__728__Div__1_.C1__Converging_Array__Easy_Version_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.RandomWrapper;
import template.utils.Debug;

import java.util.Arrays;

public class C1ConvergingArrayEasyVersion {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        solve0(0, in, out);

        int n = in.ri();
        int[] c = in.ri(n);
        int[] b = in.ri(n - 1);

        //double[] a = new double[n];
        //solve(a, b);
        //a[0] + t/n >= x
        int[] guess = new int[n];
        long sum = 0;
        for (int i = 1; i < n; i++) {
            guess[i] = guess[i - 1] + b[i - 1];
            sum += guess[i];
        }


        int lim = 10000 + 1;
        long[] prev = new long[lim + 1];
        long[] next = new long[lim + 1];
        prev[0] = 1;
        for (int i = 0; i < n; i++) {
            Arrays.fill(next, 0);
            for (int j = 0; j <= lim; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                for (int t = 0; t <= c[i] && j + t <= lim; t++) {
                    next[j + t] += prev[j];
                }
            }
            for (int j = 0; j <= lim; j++) {
                next[j] = DigitUtils.modWithoutDivision(next[j], mod);
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long[] finalPrev = prev;
        LongPreSum ps = new LongPreSum(i -> finalPrev[i], lim + 1);
        int q = in.ri();
        for (int t = 0; t < q; t++) {
            int x = in.ri();
            //sum + t * n = 0
//        long t = -sum;
            if (n == 2) {
                long ans = way(x, c[0]);
                for (int i = 1; i < n; i++) {
                    ans = ans * (c[i] + 1) % mod;
                }
                out.println(ans);
                return;
            }

            long L = (long) x * n + sum;
            if (L > lim) {
                out.println(0);
                continue;
            }
            long ans = ps.post((int) L);
            ans = DigitUtils.mod(ans, mod);
            out.println(ans);
        }
//        solve0(testNumber, in, out);
    }

    Debug debug = new Debug(true);

    public long way(long l, long r) {
        l = Math.max(l, 0);
        if (l > r) {
            return 0;
        }
        return r - l + 1;
    }

    public void solve0(int testNumber, FastInput in, FastOutput out) {
        int succ = 0;
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 59; j++) {
//                if (i + j < 40) {
//                    continue;
//                }
                double[] a = new double[]{i, j, 100};
                int[] b = new int[]{22, 23};
//                for (int j = 0; j < i; j++) {
//                    b[j] = RandomWrapper.INSTANCE.nextInt(i + 1);
//                }

                double[] ans = solve(a.clone(), b);
                debug.debugArray("a", a);
                debug.debugArray("ans", ans);
                if (a[0] >= 9) {
                    succ++;
                }
            }

        }

        debug.debug("succ", succ);
    }


    public double[] solve(double[] a, int[] b) {
        for (int r = 0; r < 1000000; r++) {
            int i = RandomWrapper.INSTANCE.nextInt(a.length - 1);
            double ai = a[i];
            double aii = a[i + 1];
            a[i] = Math.min(ai, (ai + aii - b[i]) / 2);
            a[i + 1] = Math.max(aii, (ai + aii + b[i]) / 2);
        }
        return a;
    }
}
