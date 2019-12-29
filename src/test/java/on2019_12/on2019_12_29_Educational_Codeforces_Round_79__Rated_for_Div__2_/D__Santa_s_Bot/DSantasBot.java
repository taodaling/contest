package on2019_12.on2019_12_29_Educational_Codeforces_Round_79__Rated_for_Div__2_.D__Santa_s_Bot;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Modular;
import template.math.Power;

public class DSantasBot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);
        InverseNumber inverseNumber = new InverseNumber(1000000, mod);
        int n = in.readInt();
        int limit = 1000000;
        int[] goods = new int[limit + 1];
        int[] reqs = new int[limit + 1];

        int probOfKid = inverseNumber.inverse(n);
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            int prob = inverseNumber.inverse(k);
            for (int j = 0; j < k; j++) {
                int t = in.readInt();
                goods[t] = mod.plus(goods[t], mod.mul(probOfKid, prob));
                reqs[t]++;
            }
        }

        int valid = 0;
        for (int i = 1; i <= limit; i++) {
            int prob = mod.mul(reqs[i], probOfKid);
            valid = mod.plus(valid, mod.mul(goods[i], prob));
        }

        out.println(valid);
    }
}
