package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.math.BigInteger;

public class Isomorphism {
    int pMod;
    int mod;
    int[][] gcd;
    int[] size = new int[100];
    int[] cnt = new int[100];
    int n;
    CachedPow pow2;
    InverseNumber inv;
    Factorial fact;
    int split(int n) {
        int ans = 0;
        n--;
        ans += DigitUtils.ceilDiv(n, 2);
        return ans;
    }

    long dfs(int k, int t, int cap) {
        if (t == 0) {
            if (cap != 0) {
                return 0;
            }
            long expSum = 0;
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < i; j++) {
                    expSum += (long) gcd[size[i]][size[j]] * cnt[i] * cnt[j] ;
                }
                expSum += (long) size[i] * cnt[i] * (cnt[i] - 1) / 2;
                expSum += (long)split(size[i]) * cnt[i];
            }
            long each = pow2.pow(expSum);
            long way = fact.fact(n);
            for (int i = 0; i < k; i++) {
                way = way * fact.invFact(cnt[i]) % mod;
                for(int j = 0; j < cnt[i]; j++){
                    way = way * inv.inverse(size[i]) % mod;
                }
            }
            return each * way % mod;
        }
        long ans = dfs(k, t - 1, cap);
        size[k] = t;
        for (int i = 1; i * t <= cap; i++) {
            cnt[k] = i;
            ans += dfs(k + 1, t - 1, cap - i * t);
        }
        return ans % mod;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int k = in.ri();
        mod = in.ri();
        pMod = in.ri();
        pow2 = new CachedPow(k, mod);
        inv = new ModPrimeInverseNumber(100, mod);
        fact = new Factorial(100, mod);
        gcd = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                gcd[i][j] = GCDs.gcd(i, j);
            }
        }
        long ans = dfs(0, n, n);
        ans = ans * fact.invFact(n) % mod;
        out.println(ans);
    }
}
