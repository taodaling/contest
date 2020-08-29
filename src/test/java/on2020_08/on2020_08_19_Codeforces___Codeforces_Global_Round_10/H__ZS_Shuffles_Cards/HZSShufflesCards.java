package on2020_08.on2020_08_19_Codeforces___Codeforces_Global_Round_10.H__ZS_Shuffles_Cards;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.math.Modular;
import template.math.Power;

public class HZSShufflesCards {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Modular mod = new Modular(998244353);
        Power pow = new Power(mod);

        int exp1 = pow.inverse(m + 1);
        exp1 = mod.mul(exp1, n);
        exp1 = mod.plus(exp1, 1);

        InverseNumber inv = new ModPrimeInverseNumber(n, mod);
        int exp2 = 0;
        for (int i = 1; i <= n; i++) {
            exp2 = mod.plus(exp2, inv.inverse(i));
        }
        exp2 = mod.mul(exp2, m);
        exp2 = mod.plus(exp2, 1);

        int ans = mod.mul(exp1, exp2);
        out.println(ans);
    }
}
