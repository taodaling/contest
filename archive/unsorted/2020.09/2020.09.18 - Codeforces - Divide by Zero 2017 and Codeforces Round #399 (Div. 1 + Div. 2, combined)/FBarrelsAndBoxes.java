package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;

public class FBarrelsAndBoxes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        try {
            solve0(testNumber, in, out);
        } catch (IllegalArgumentException e) {
            out.println(0);
        }
    }

    public void solve0(int testNumber, FastInput in, FastOutput out) {
        int f = in.readInt();
        int w = in.readInt();
        int h = in.readInt();

        int threshold = f + w;

        long total = 0;
        long satisfy = 0;

        //food first
        for (int i = 1; i <= threshold; i++) {
            int forFood = DigitUtils.ceilDiv(i, 2);
            int forWine = i - forFood;
            //alloc
            total += way(forFood, f, 1) * way(forWine, w, 1) % modVal;
            satisfy += way(forFood, f, 1) * way(forWine, w, h + 1) % modVal;
        }

        for (int i = 1; i <= threshold; i++) {
            int forWine = DigitUtils.ceilDiv(i, 2);
            int forFood = i - forWine;

            //alloc
            total += way(forFood, f, 1) * way(forWine, w, 1) % modVal;
            satisfy += way(forFood, f, 1) * way(forWine, w, h + 1) % modVal;
        }

        total %= modVal;
        satisfy %= modVal;

        Power power = new Power(mod);
        long ans = satisfy * power.inverse((int) total) % modVal;
        out.println(ans);
    }


    Modular mod = new Modular(1e9 + 7);
    int modVal = mod.getMod();
    Combination comb = new Combination(1000000, new Power(mod));

    //while xi >= k and x1 + ... + xn = m
    public long way(int n, int m, int k) {
        if (n == 0) {
            return (long) m * k == 0 ? 1 : 0;
        }
        if ((long) k * n > m) {
            return 0;
        }
        m -= n * k;
        return comb.combination(m + n - 1, m);
    }
}
