package on2019_10.on2019_10_11_Atcoder_ARC061.TaskD;



import java.io.PrintWriter;

import template.FastInput;
import template.NumberTheory;

public class TaskD {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        NumberTheory.Modular mod = new NumberTheory.Modular((int) 1e9 + 7);
        NumberTheory.Power power = new NumberTheory.Power(mod);
        NumberTheory.Composite comp = new NumberTheory.Composite(1000000, mod);

        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        int ans = 0;

        int t = m + k;
        int[] preSumOfM = new int[t + 1];
        int[] preSumOfK = new int[t + 1];
        preSumOfM[0] = 1;
        preSumOfK[0] = 1;
        for (int i = 1; i <= t; i++) {
            preSumOfM[i] = mod.subtract(mod.mul(2, preSumOfM[i - 1]), comp.composite(i - 1, m));
            preSumOfK[i] = mod.subtract(mod.mul(2, preSumOfK[i - 1]), comp.composite(i - 1, k));
        }

        for (int i = 0; i <= m + k; i++) {
            int total = power.pow(2, i);
            int b = mod.subtract(total, preSumOfM[i]);
            int c = mod.subtract(total, preSumOfK[i]);
            int cnt = mod.subtract(total, b);
            cnt = mod.subtract(cnt, c);
            cnt = mod.mul(cnt, comp.composite(n - 1 + i, i));
            cnt = mod.mul(cnt, power.pow(3, m + k - i));
            ans = mod.plus(ans, cnt);
        }

        out.println(ans);
    }
}
