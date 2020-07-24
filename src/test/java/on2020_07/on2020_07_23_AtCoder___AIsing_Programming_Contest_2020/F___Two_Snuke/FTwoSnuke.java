package on2020_07.on2020_07_23_AtCoder___AIsing_Programming_Contest_2020.F___Two_Snuke;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRecursiveCombination;
import template.math.Lucas;
import template.math.Modular;
import template.math.Power;

public class FTwoSnuke {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    IntRecursiveCombination comb = new IntRecursiveCombination(power);
    Lucas lucas = new Lucas(comb, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = 11;
        int m = 5;
        int T = in.readInt() - 5;
        if (T < 0) {
            out.println(0);
            return;
        }
        int ans = 0;
        int lastA = -1;
        int lastB = -1;
        for (int i = 0; i <= k && T - i >= 0; i++) {
            if (lastA == -1) {
                lastA = 1;
            } else {
                lastA = mod.mul(lastA, mod.mul(k - i + 1, power.inverse(i)));
            }

            int ai = lastA;
//
//            if(ai != lucas.combination(k, i)){
//                throw new RuntimeException();
//            }
            int j = T - i;
            if (j % 2 == 1) {
                continue;
            }

            int top = m + k - 1 + j / 2;
            int bot = j / 2;
            if (lastB == -1) {
                lastB = lucas.combination(top, bot);
            } else {
                lastB = mod.mul(lastB, mod.plus(bot, 1));
                lastB = mod.mul(lastB, power.inverse(mod.plus(top, 1)));
            }


            int bi = lastB;
//            if(bi != lucas.combination(top, bot)){
//                throw new RuntimeException();
//            }
            int contrib = mod.mul(ai, bi);
            ans = mod.plus(ans, contrib);
        }
        out.println(ans);
    }
}
