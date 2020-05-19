package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Modular;

public class BKarenAndTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(1e9 + 7);
        Combination comb = new Combination(200000, mod);
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = mod.valueOf(in.readInt());
        }
        if (n == 1) {
            out.println(a[0]);
            return;
        }

        if (n % 2 == 1) {
            int[] b = a;
            a = new int[n - 1];
            for (int i = 0; i < n - 1; i++) {
                if (i % 2 == 0) {
                    a[i] = mod.plus(b[i], b[i + 1]);
                } else {
                    a[i] = mod.subtract(b[i], b[i + 1]);
                }
            }
            n--;
        }

        int sumL = 0;
        int sumR = 0;
        int time = n / 2 - 1;
        for (int i = 0; i < n; i += 2) {
            sumL = mod.plus(sumL, mod.mul(a[i], comb.combination(time, i / 2)));
        }
        for (int i = 1; i < n; i += 2) {
            sumR = mod.plus(sumR, mod.mul(a[i], comb.combination(time, i / 2)));
        }
        int ans;
        if ((n - 2) / 2 % 2 == 1) {
            ans = mod.subtract(sumL, sumR);
        } else {
            ans = mod.plus(sumL, sumR);
        }

        out.println(ans);
    }
}
