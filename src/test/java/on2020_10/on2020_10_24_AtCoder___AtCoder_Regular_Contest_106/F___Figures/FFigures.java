package on2020_10.on2020_10_24_AtCoder___AtCoder_Regular_Contest_106.F___Figures;



import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class FFigures {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] d = new int[n];
        int mod = 998244353;
        in.populate(d);
        long prod = 1;
        for (int i = 0; i < n; i++) {
            prod = prod * d[i] % mod;
            d[i]--;
        }
        long sum = Arrays.stream(d).mapToLong(Long::valueOf).sum() % mod;
        for (int i = 0; i < n - 2; i++) {
            prod = prod * sum % mod;
            sum = DigitUtils.modsub((int)sum, 1, mod);
        }
        out.println(prod);
    }
}
