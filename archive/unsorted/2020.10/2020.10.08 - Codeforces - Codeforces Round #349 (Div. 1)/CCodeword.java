package contest;

import template.io.FastInput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Power;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CCodeword {

    int mod = (int) (1e9 + 7);
    Power pow = new Power(mod);
    Combination comb = new Combination((int) 1e5, mod);

    int C = 'z' - 'a' + 1;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        Map<Integer, int[]> cache = new HashMap<>();
        int m = in.readInt();
        char[] buf = new char[(int) 1e5];
        int s = in.readString(buf, 0);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                s = in.readString(buf, 0);

            } else {
                int n = in.readInt();
                if (!cache.containsKey(s)) {
                    cache.put(s, compute(s));
                }
                int[] ans = cache.get(s);
                out.println(ans[n]);
            }
        }
    }

    int len = (int) 1e5;

    public int[] compute(int s) {
        int[] ans = new int[len + 1];
        long top = 1;
        long bot = pow.inversePower(C - 1, s);
        long sum = 0;
        long x = DigitUtils.modsub(1, pow.inverse(C), mod);
        long prod = 1;
        for (int i = 1; i <= len; i++) {
            top = top * C % mod;
            prod = prod * x % mod;
            sum += comb.combination(i - 1, s - 1) * prod;
            sum %= mod;
            ans[i] = (int) (top * bot % mod * sum % mod);
        }
        return ans;
    }
}
