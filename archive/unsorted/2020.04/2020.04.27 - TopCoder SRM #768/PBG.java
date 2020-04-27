package contest;

import template.math.Modular;
import template.math.Power;
import template.math.PrimeCombination;

public class PBG {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    PrimeCombination combination = new PrimeCombination(10000, mod);
    int neg2 = power.inverseByFermat(2);

    public int findEV(int P, int B, int G) {
        int n = P + B + G;
        int sum = mod.valueOf((1 + n) * n / 2);
        if (G == 0) {
            return expect(P, B);
        }
        if (B == 0) {
            int EG = expect(G, P);
            int EP = mod.mul(mod.subtract(sum, mod.mul(G, EG)), power.inverseByFermat(P));
            return EP;
        }
        int EG = expect(G, B + P);
        int EG2 = expect(G + P, B);
        int EB = mod.mul(mod.subtract(sum, mod.mul(G + P, EG2)), power.inverseByFermat(B));
        int EP = mod.subtract(sum, mod.mul(G, EG));
        EP = mod.subtract(EP, mod.mul(B, EB));
        EP = mod.mul(EP, power.inverseByFermat(P));
        return EP;
    }

    public int expect(int P, int B) {
        int[][] dp = new int[P + B + 1][P + 1];
        dp[P + B][P] = 1;
        int exp = 0;
        for (int i = P + B; i >= 2; i--) {
            for (int j = P; j >= 1; j--) {
                if (dp[i][j] == 0) {
                    continue;
                }
                //P with P
                // A die
                int prob1 = mod.mul(neg2, mod.mul(combination.combination(j - 1, 1), combination.invCombination(i, 2)));

                //P with P and A survive
                int prob2 = mod.mul(combination.combination(j, 2), combination.invCombination(i, 2));
                prob2 = mod.subtract(prob2, prob1);

                //else
                int prob3 = mod.subtract(1, prob1);
                prob3 = mod.subtract(prob3, prob2);

                exp = mod.plus(exp, mod.mul(i, mod.mul(dp[i][j], prob1)));
                dp[i - 1][j - 1] = mod.plus(dp[i - 1][j - 1], mod.mul(prob2, dp[i][j]));
                dp[i - 1][j] = mod.plus(dp[i - 1][j], mod.mul(prob3, dp[i][j]));
            }
        }

        exp = mod.plus(exp, mod.mul(dp[1][1], 1));
        return exp;
    }
}
