package on2020_02.on2020_02_23_Codeforces_Round__454__Div__1__based_on_Technocup_2018_Elimination_Round_4_.D__Power_Tower;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.EulerSieve;
import template.math.GCDs;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.datastructure.IntegerHashMap;

public class DPowerTower {
    int limit = (int) 1e9;
    int sqrt = (int) Math.sqrt(limit);
    MultiplicativeFunctionSieve sieve =
            new MultiplicativeFunctionSieve(sqrt, false, true, false);
    IntegerHashMap map = new IntegerHashMap(sqrt, true);

    private int getFactor(int x) {
        for (int i = 2; i * i <= x; i++) {
            if (x % i == 0) {
                return i;
            }
        }
        return x;
    }

    public int euler(int x) {
        if (x <= sqrt) {
            return sieve.euler[x];
        } else {
            int ans = map.getOrDefault(x, -1);
            if (ans == -1) {
                int factor = getFactor(x);
                int y = x;
                int exp = 1;
                while (y % factor == 0) {
                    y /= factor;
                    exp *= factor;
                }
                ans = euler(y) * (exp - exp / factor);
                map.put(x, ans);
            }
            ;
            return ans;
        }
    }

    int[] w;
    int n;
    int[][] p;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = in.readInt();
        }
        p = new int[n][4];
        for (int i = 0; i < n; i++) {
            p[i][0] = w[i];
        }
        for (int j = 1; j < 4; j++) {
            for (int i = 0; i + j < n; i++) {
                p[i][j] = pow(w[i], p[i + 1][j - 1], limit);
            }
        }

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int l = in.readInt() - 1;
            int r = in.readInt() - 1;
            int ans = query(l, r, m);
            out.println(ans);
        }
    }


    public int pow(int x, int n, int limit) {
        if (n == 0) {
            return Math.min(1, limit);
        }
        int ans = pow(x, n / 2, limit);
        ans = (int) DigitUtils.mul(ans, ans, limit, limit);
        if (n % 2 == 1) {
            ans = (int) DigitUtils.mul(ans, x, limit, limit);
        }
        return ans;
    }

    public int modpow(int x, int n, int m) {
        if (n == 0) {
            return DigitUtils.mod(1, m);
        }
        int ans = modpow(x, n / 2, m);
        ans = DigitUtils.mod((long) ans * ans, m);
        if (n % 2 == 1) {
            ans = DigitUtils.mod((long) ans * x, m);
        }
        return ans;
    }

    public int test(int l, int r) {
        return p[l][r - l];
    }

    public int query(int l, int r, int m) {
        if (l == r) {
            return DigitUtils.mod(w[l], m);
        }
        if (w[l] == 1) {
            return DigitUtils.mod(1, m);
        }
        if (m == 1) {
            return 0;
        }
        int t = test(l + 1, Math.min(l + 4, r));
        if (t < 32) {
            return modpow(w[l], t, m);
        }
        return query(l, r, m, w[l], 0);
    }

    public int query(int l, int r, int b, int g, int sub) {
        g = GCDs.gcd(g, b);
        if (g == 1) {
            int expMod = euler(b);
            int ans = query(l + 1, r, expMod);
            ans = DigitUtils.mod(ans - sub, expMod);
            return modpow(w[l], ans, b);
        }
        int ans = query(l, r, b / g, g, sub + 1);
        ans = DigitUtils.mod((long) ans * (w[l] / g), b / g);
        return ans * g;
    }
}
