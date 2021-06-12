package on2021_05.on2021_05_31_Codeforces___Deltix_Round__Spring_2021__open_for_everyone__rated__Div__1___Div__2_.E__Crypto_Lights;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;

public class ECryptoLights {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e6, mod);
    Combination comb = new Combination(fact);

    public long prob(int n, int k) {
        return comb.invCombination(n, k);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[] way = new long[n + 1];
        long[] prob = new long[n + 1];

        for (int i = 0; i < n && i * k <= n + k - 1; i++) {
            int size = n + k - 1;
            int remain = size - k * i;
            way[i] = comb.combination(remain + i, remain);
            prob[i] = way[i] * prob(n, i) % mod;
        }

        long exp = 0;
        for(int i = 0; i < n; i++){
            long end = prob[i] - prob[i + 1];
            int cnt = i + 1;
            exp += cnt * end % mod;
        }

        exp = DigitUtils.mod(exp, mod);
        out.println(exp);
    }
}
