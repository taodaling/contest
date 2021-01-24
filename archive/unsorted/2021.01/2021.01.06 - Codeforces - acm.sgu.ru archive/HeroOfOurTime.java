package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.NatureBigInt;
import template.utils.Debug;



public class HeroOfOurTime {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();

        NatureBigInt bn = NatureBigInt.valueOf(n);
        NatureBigInt[] powN = new NatureBigInt[n + 1];
        powN[0] = NatureBigInt.valueOf(1);
        for (int i = 1; i <= n; i++) {
            powN[i] = powN[i - 1].mul(bn);
        }
        NatureBigInt[] fact = new NatureBigInt[n + 1];
        fact[0] = NatureBigInt.valueOf(1);
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1].mul(NatureBigInt.valueOf(i));
        }
        NatureBigInt[] invFact = new NatureBigInt[n + 2];
        invFact[n + 1] = NatureBigInt.valueOf(1);
        for (int i = n; i >= 1; i--) {
            invFact[i] = invFact[i + 1].mul(NatureBigInt.valueOf(i));
        }
        NatureBigInt sum = NatureBigInt.valueOf(0);
        debug.elapse("prepare");
        for (int i = 3; i < n; i++) {
            int m = n - i + 1;
            NatureBigInt contrib = powN[m - 2].mul(invFact[n - i + 1]);
            sum = sum.add(contrib);
        }
        debug.elapse("sum");
        sum = sum.add(fact[n - 1]);
        sum = sum.div(2);
        sum.toString(out.getCache());
    }

}
