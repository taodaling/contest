package on2021_01.on2021_01_11_CSES___CSES_Problem_Set.Xor_Pyramid;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.IntRecursiveCombination;
import template.math.Lucas;

public class XorPyramid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        int mod = 2;
        Combination comb = new Combination(n, mod);
        Lucas lucas = new Lucas(comb, mod);

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (lucas.combination(n - 1, i) == 1) {
                ans ^= a[i];
            }
        }
        out.println(ans);
    }
}
