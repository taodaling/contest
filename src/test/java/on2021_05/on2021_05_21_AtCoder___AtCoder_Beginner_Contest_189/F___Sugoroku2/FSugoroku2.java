package on2021_05.on2021_05_21_AtCoder___AtCoder_Beginner_Contest_189.F___Sugoroku2;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DoubleLinearFunction;
import template.math.ModLinearFunction;
import template.utils.Debug;

public class FSugoroku2 {
    DoubleLinearFunction interval(DoubleLinearFunction[] ps, int l, int r) {
        if (l > r) {
            return new DoubleLinearFunction(0, 0);
        }
        DoubleLinearFunction ans = ps[l];
        if (r + 1 < ps.length) {
            ans = DoubleLinearFunction.subtract(ans, ps[r + 1]);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int[] a = in.ri(k);
        int maxContinuous = 1;
        int last = -1;
        for (int x : a) {
            if (x == last + 1) {
                maxContinuous++;
            } else {
                maxContinuous = 1;
            }
            last = x;
        }
        if (maxContinuous >= m) {
            out.println(-1);
            return;
        }
        DoubleLinearFunction[] exp = new DoubleLinearFunction[n + 1];
        DoubleLinearFunction[] ps = new DoubleLinearFunction[n + 1];
        exp[n] = new DoubleLinearFunction(0, 0);
        ps[n] = new DoubleLinearFunction(0, 0);
        for (int i = n - 1; i >= 0; i--) {
            boolean find = false;
            for (int x : a) {
                if (x == i) {
                    find = true;
                }
            }
            if (find) {
                exp[i] = new DoubleLinearFunction(1, 0);
            } else {
                int l = i + 1;
                int r = Math.min(n - 1, i + m);
                DoubleLinearFunction sum = interval(ps, l, r);
                exp[i] = DoubleLinearFunction.mul(sum, 1.0 / m);
                exp[i] = DoubleLinearFunction.plus(exp[i], 0, 1);
            }
            ps[i] = DoubleLinearFunction.plus(ps[i + 1], exp[i]);
        }

        debug.debug("exp", exp);
        debug.debug("ps", ps);
        double ans = exp[0].b / (1 - exp[0].a);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
