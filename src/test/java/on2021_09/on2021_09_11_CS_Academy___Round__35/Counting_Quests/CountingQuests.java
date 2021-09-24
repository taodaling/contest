package on2021_09.on2021_09_11_CS_Academy___Round__35.Counting_Quests;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.FastPow2;
import template.utils.Debug;

public class CountingQuests {
    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = in.ri();
        Combination comb = new Combination((int) 1e5, mod);
        FastPow2 fp = new FastPow2(2, mod);
        long ans = 0;
        for (int conn = 0; conn < n && conn < 2; conn++) {
            for (int i = 0; i < n; i++) {
                long choice = choose2(n - 1 - i);
                if (conn == 0) {
                    choice += (n - 1 - i) * 2 + 1;
                }else{
                    choice += 1;
                }
                long contrib = fp.pow(choice);
                contrib = contrib * comb.combination(n - 1, i) % mod;
                if ((i + conn) % 2 == 1) {
                    contrib = -contrib;
                }
                ans += contrib;
                debug.debug("i", i);
                debug.debug("contrib", contrib);
            }
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
