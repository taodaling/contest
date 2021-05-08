package on2021_05.on2021_05_07_Codeforces___Codeforces_Round__189__Div__1_.A__Malek_Dance_Club;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow2;
import template.utils.SequenceUtils;

public class AMalekDanceClub {
    int mod = (int) 1e9 + 7;
    CachedPow2 p2 = new CachedPow2(2, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        SequenceUtils.reverse(s);
        long ans = sum(s.length, s);
        out.println(ans);
    }

    long sum(int k, char[] s) {
        if (k <= 0) {
            return 0;
        }
        long ans = 0;
        if (s[k - 1] == '1') {
            ans += p2.pow(2 * k - 2);
        }
        ans += 2 * sum(k - 1, s);
        return ans % mod;
    }
}
