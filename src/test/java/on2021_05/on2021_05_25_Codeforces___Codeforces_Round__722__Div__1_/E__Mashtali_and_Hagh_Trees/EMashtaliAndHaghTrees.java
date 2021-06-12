package on2021_05.on2021_05_25_Codeforces___Codeforces_Round__722__Div__1_.E__Mashtali_and_Hagh_Trees;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.PermutationUtils;
import template.math.Power;

import java.util.stream.IntStream;

public class EMashtaliAndHaghTrees {
    int mod = 998244353;
    int inv2 = (mod + 1) / 2;

    public long solve0(int n, boolean req) {
        if (n == 0) {
            return (req ? 0 : 1) + 1;
        }
        long way = solve0(n - 1, false);
        long ans = ((req ? 0 : 1) + (way * way - way) % mod * inv2 % mod + way) % mod;
        return ans;
    }

    DSU dsu = new DSU(3);

    private long color(int[] p, long way) {
        dsu.init(p.length);
        for (int i = 0; i < p.length; i++) {
            dsu.merge(i, p[i]);
        }
        long ans = 1;
        for (int i = 0; i < p.length; i++) {
            if (dsu.find(i) == i) {
                ans = ans * way % mod;
            }
        }
        return ans;
    }

    Power power = new Power(mod);

    public long color(int n, long way) {
        long ans = 0;
        int[] p = IntStream.range(0, n).toArray();
        int g = 0;
        do {
            g++;
            ans += color(p, way);
        } while (PermutationUtils.nextPermutation(p));
        return ans % mod * power.inverse(g) % mod;
    }

    public long solve(int n) {
        if (n <= 0) {
            return 1;
        }
        long way = solve0(n - 1, true);
        long ans = color(3, way) * 2 + solve0(n, true) * 2 - (n + 1);
        return ans % mod;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long res = solve(n) - solve(n - 1);
        res = DigitUtils.mod(res, mod);
        out.println(res);
    }
}
