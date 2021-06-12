package on2021_05.on2021_05_23_AtCoder___AtCoder_Regular_Contest_120.F___Wine_Thief;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;

public class FWineThief {
    int mod = 998244353;
    InverseNumber inv = new ModPrimeInverseNumber((int) 5e5, mod);
    Combination comb = new Combination((int) 5e5, mod);
    long[] delta;

    public void update(int l, int r, long x) {
        delta[l] += x;
        if (r + 1 < delta.length) {
            delta[r + 1] -= x;
        }
    }

    public long f(int n, int k) {
        return comb.combination(n + 1 - k, k);
    }

    public long g(int n, int k) {
        return (f(n - 3, k - 1) + f(n - 1, k)) % mod;
    }

    public void dfs(int l, int r, int k) {
        if (k <= 0 || r - l + 1 < k) {
            return;
        }
        if (r - l + 1 == 1) {
            update(l, r, 1);
            return;
        }
        //circular
        update(l, r, g(r - l + 1, k) * k % mod * inv.inverse(r - l + 1) % mod);
        if (r - l + 1 >= 3) {
            long w = f(r - l + 1 - 4, k - 2);
            update(l, l, w);
            update(r, r, w);
            dfs(l + 2, r - 2, k - 2);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int d = in.ri();
        int[] a = in.ri(n);
        delta = new long[n];

        dfs(0, n - 1, k);
        for(int i = 0; i < n; i++){
            if(i > 0){
                delta[i] += delta[i - 1];
            }
            delta[i] %= mod;
        }
        long sum = 0;
        for(int i = 0; i < n; i++){
            sum += delta[i] * a[i] % mod;
        }
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
}
