package on2021_08.on2021_08_04_AtCoder___AtCoder_Beginner_Contest_212.G___Power_Pair;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.LongFactorization;
import template.math.OrderedDivisorIterator;
import template.primitve.generated.datastructure.LongArrayList;
import template.utils.Debug;

import java.util.Arrays;

public class GPowerPair {
    int mod = 998244353;
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long p = in.rl();
        LongFactorization factorization = new LongFactorization(p - 1);
        OrderedDivisorIterator oi = new OrderedDivisorIterator(true);
        oi.init(factorization);
        LongArrayList list = new LongArrayList((int) 1e5);
        while (oi.hasNext()) {
            long next = oi.next();
            list.add(next);
        }

        long[] all = list.toArray();
        debug.debugArray("all", all);
        long[] f = new long[all.length];
        for (int i = 0; i < all.length; i++) {
            long k = all[i];
            long divisor = (p - 1) / GCDs.gcd(p - 1, k);
            f[i] = (p - 1) / divisor;
        }

        for (int i = 0; i < all.length; i++) {
            for (int j = 0; j < i; j++) {
                if (all[i] % all[j] == 0) {
                    f[i] -= f[j];
                }
            }
        }
        debug.debugArray("f", f);
        long ans = 1;
        for(int i = 0; i < all.length; i++){
            ans += all[i] % mod * (f[i] % mod) % mod;
        }
        ans %= mod;
        out.println(ans);
    }
}
