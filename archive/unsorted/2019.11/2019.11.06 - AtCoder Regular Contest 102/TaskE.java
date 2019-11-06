package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int K = in.readInt();
        int N = in.readInt();

        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        NumberTheory.Factorial fact = new NumberTheory.Factorial(10000, mod);
        NumberTheory.Composite comp = new NumberTheory.Composite(fact);

        for (int i = 2; i <= 2 * K; i++) {
            int n = 0;
            for (int j = 1; j < i; j++) {
                int t = i - j;
                if (j > K || t > K || j < t) {
                    continue;
                }
                n++;
            }
            int ans = 0;
            for (int t = 0; t <= n; t++) {
                if (t * 2 > N) {
                    continue;
                }
                int remain = N - t * 2;
                int local = comp.composite(remain + K - 1, remain);
                local = mod.mul(local, comp.composite(n, t));
                if (t % 2 == 1) {
                    local = mod.valueOf(-local);
                }
                ans = mod.plus(ans, local);
            }

            out.println(ans);
        }
    }
}
