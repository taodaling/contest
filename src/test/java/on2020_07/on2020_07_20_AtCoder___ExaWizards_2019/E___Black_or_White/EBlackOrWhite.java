package on2020_07.on2020_07_20_AtCoder___ExaWizards_2019.E___Black_or_White;



import template.datastructure.ModPreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Combination;
import template.math.Modular;
import template.math.Power;

public class EBlackOrWhite {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    CachedPow cp = new CachedPow(2, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int b = in.readInt();
        int w = in.readInt();

        int[] pb = calcProbs(b, w);
        int[] pw = calcProbs(w, b);

        ModPreSum pbPs = new ModPreSum(pb, mod);
        ModPreSum pwPs = new ModPreSum(pw, mod);
        for (int i = 1; i <= b + w; i++) {
            int ans = 0;
            ans = mod.plus(ans, mod.mul(pbPs.prefix(i - 1), 0));
            ans = mod.plus(ans, mod.mul(pwPs.prefix(i - 1), 1));
            int remain = 1;
            remain = mod.subtract(remain, pbPs.prefix(i - 1));
            remain = mod.subtract(remain, pwPs.prefix(i - 1));
            ans = mod.plus(ans, mod.mul(remain, cp.inverse(1)));

            out.println(ans);
        }
    }


    Combination comb = new Combination((int) 2e5, power);

    public int[] calcProbs(int b, int w) {
        int[] ans = new int[b + w + 1];
        ans[0] = 0;
        for (int i = 1; i < b + w; i++) {
            int prob = cp.inverse(i);
            prob = mod.mul(prob, comb.combination(i - 1, b - 1));
            ans[i] = prob;
        }
        ans[b + w] = 1;
        for (int i = 0; i < b + w; i++) {
            ans[b + w] = mod.subtract(ans[b + w], ans[i]);
        }
        return ans;
    }
}
