package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.Random;

public class BDivideAndSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int mod = 998244353;
        int n = in.readInt();
        Combination comb = new Combination(n * 2, mod);
        int[] a = new int[2 * n];
        in.populate(a);
        Randomized.shuffle(a);
        Arrays.sort(a);
        long ans = 0;
        for(int i = 0; i < n; i++){
            ans -= a[i];
        }
        for(int i = n; i < 2 * n; i++){
            ans += a[i];
        }
        ans = DigitUtils.mod(ans, mod);
        ans = ans * comb.combination(2 * n, n) % mod;
        out.println(ans);
    }
}
