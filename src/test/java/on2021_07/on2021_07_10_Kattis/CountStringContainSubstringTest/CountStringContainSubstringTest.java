package on2021_07.on2021_07_10_Kattis.CountStringContainSubstringTest;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.FastPow2;
import template.string.CountStringContainSubstring;
import template.string.IntFunctionIntSequenceAdapter;

public class CountStringContainSubstringTest {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int c = in.ri();
        int m = in.ri();
        char[] s = new char[m];
        in.rs(s);
        CountStringContainSubstring cs = new CountStringContainSubstring(n);
        FastPow2 pow = new FastPow2(c, mod);
        long ans = cs.solve(c, n, new IntFunctionIntSequenceAdapter(i -> s[i], 0, m - 1), mod, pow);
        out.println(ans);
    }
}
