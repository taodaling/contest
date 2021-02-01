package on2021_02.on2021_02_01_Codeforces___Codeforces_Round__313__Div__1_.B__Equivalent_Strings0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.IntRangeHash;
import template.rand.MultiSetHasher;
import template.rand.SparseMultiSetHasher;

public class BEquivalentStrings {
    char[] a, b;
    MultiSetHasher hasher = new SparseMultiSetHasher((int) 1e6);

    long hash(char[] s, int l, int r) {
        if ((r - l + 1) % 2 != 0) {
            long h1 = 0;
            long h2 = 0;
            long x1 = 31;
            long x2 = 13;
            int mod = (int) 1e9 + 7;
            for (int i = l; i <= r; i++) {
                h1 = (h1 * x1 + s[i]) % mod;
                h2 = (h2 * x2 + s[i]) % mod;
            }
            return hasher.hash(DigitUtils.asLong(h1, h2));
        }
        int m = (r + l) / 2;
        long sum = hasher.merge(hash(s, l, m), hash(s, m + 1, r));
        return hasher.hash(sum);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        a = in.rs().toCharArray();
        b = in.rs().toCharArray();
        long h1 = hash(a, 0, a.length - 1);
        long h3 = hash(b, 0, b.length - 1);
        out.println(h1 == h3 ? "YES" : "NO");
    }
}
