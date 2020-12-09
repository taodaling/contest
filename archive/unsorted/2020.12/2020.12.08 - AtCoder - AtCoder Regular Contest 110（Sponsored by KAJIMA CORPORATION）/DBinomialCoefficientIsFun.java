package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigCombination;
import template.math.IntRecursiveCombination;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;

import java.util.Arrays;

public class DBinomialCoefficientIsFun {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int mod = (int) 1e9 + 7;
        int[] a = new int[n];
        in.populate(a);
        int sumOfA = Arrays.stream(a).sum();
        int top = m + n;
        int bot = sumOfA + n;
        InverseNumber inv = new ModPrimeInverseNumber((int) 5e6, mod);
        IntRecursiveCombination comb = new IntRecursiveCombination(inv, mod);
        int ans = comb.combination(top, bot);
        out.println(ans);
    }
}
