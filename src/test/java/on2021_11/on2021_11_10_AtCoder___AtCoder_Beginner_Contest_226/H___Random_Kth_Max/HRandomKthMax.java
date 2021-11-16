package on2021_11.on2021_11_10_AtCoder___AtCoder_Beginner_Contest_226.H___Random_Kth_Max;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.utils.SequenceUtils;

public class HRandomKthMax {
    int mod = 998244353;
    InverseNumber inv = new ModPrimeInverseNumber((int) 1e3, mod);

    public long expect(int n, int k) {
        return (long) k * inv.inverse(n + 1) % mod;
    }

    public long intersect(int l, int r, int L, int R) {
        return Math.max(0, Math.min(r, R) - Math.max(l, L));
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = n + 1 - in.ri();
        int[] l = new int[n];
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            l[i] = in.ri();
            r[i] = in.ri();
        }
        long[][] prev = new long[n + 1][n + 1];
        long[][] next = new long[n + 1][n + 1];
        int M = 100;
        long ans = 0;
        for (int i = 0; i < M; i++) {
            int begin = i;
            int end = i + 1;
            SequenceUtils.deepFill(prev, 0L);
            prev[0][0] = 1;
            for (int j = 0; j < n; j++) {
                SequenceUtils.deepFill(next, 0L);
                for (int t = 0; t <= n; t++) {
                    for (int z = 0; z <= n; z++) {
                        if (prev[t][z] == 0) {
                            continue;
                        }
                        long left = intersect(l[j], r[j], 0, begin) * inv.inverse(r[j] - l[j]) % mod;
                        long mid = intersect(l[j], r[j], begin, end) * inv.inverse(r[j] - l[j]) % mod;
                        long right = intersect(l[j], r[j], end, M) * inv.inverse(r[j] - l[j]) % mod;
                        next[t + 1][z] += left * prev[t][z] % mod;
                        next[t][z + 1] += mid * prev[t][z] % mod;
                        next[t][z] += right * prev[t][z] % mod;
                    }
                }
                for (int t = 0; t <= n; t++) {
                    for (int z = 0; z <= n; z++) {
                        next[t][z] %= mod;
                    }
                }
                long[][] tmp = prev;
                prev = next;
                next = tmp;
            }
            for (int t = 0; t <= n; t++) {
                for (int z = 0; z <= n; z++) {
                    if (prev[t][z] == 0) {
                        continue;
                    }
                    if (t >= k || t + z < k) {
                        continue;
                    }
                    int kth = k - t;
                    long expect = expect(z, kth) + i;
                    ans += expect * prev[t][z] % mod;
                }
            }
        }

        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
