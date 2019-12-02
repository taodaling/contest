package contest;

import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] ls = new int[n + 1];
        int[] rs = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ls[i] = in.readInt();
        }
        for (int i = 1; i <= n; i++) {
            rs[i] = in.readInt();
        }
        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);

        int[] range = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            range[i] = rs[i] - ls[i] + 1;
        }
        int[] invRange = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            invRange[i] = pow.inverse(range[i]);
        }

        int[] probF = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int p = mod.mul(invRange[i - 1], invRange[i]);
            int intersect = intersect(ls[i - 1], rs[i - 1], ls[i], rs[i]);
            p = mod.mul(p, intersect);
            probF[i] = mod.subtract(1, p);
        }
        PreSum ps = new PreSum(probF);

        int part1 = 0;
        for (int i = 2; i <= n; i++) {
            int local1 = mod.valueOf(ps.intervalSum(1, i - 2));
            local1 = mod.mul(local1, probF[i]);

            int intersect = intersect(ls[i - 2], rs[i - 2], ls[i - 1], rs[i - 1],
                    ls[i], rs[i]);
            int probBoth = intersect;
            probBoth = mod.mul(probBoth, invRange[i - 2]);
            probBoth = mod.mul(probBoth, invRange[i - 1]);
            probBoth = mod.mul(probBoth, invRange[i]);
            int local2 = 1;
            local2 = mod.subtract(local2, mod.subtract(1, probF[i - 1]));
            local2 = mod.subtract(local2, mod.subtract(1, probF[i]));
            local2 = mod.plus(local2, probBoth);

            int local = mod.plus(local1, local2);
            part1 = mod.plus(part1, local);
        }
        part1 = mod.mul(part1, 2);

        int part2 = 0;
        for(int i = 1; i <= n; i++){
            part2 = mod.plus(part2, probF[i]);
        }

        int exp  = mod.plus(part1, part2);
        out.println(exp);
    }

    public int intersect(int l1, int r1, int l2, int r2) {
        int l = Math.max(l1, l2);
        int r = Math.min(r1, r2);
        return Math.max(r - l + 1, 0);
    }

    public int intersect(int l1, int r1, int l2, int r2, int l3, int r3) {
        int l = Math.max(l1, Math.max(l2, l3));
        int r = Math.min(r1, Math.min(r2, r3));
        return Math.max(r - l + 1, 0);
    }
}
