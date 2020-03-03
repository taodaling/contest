package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongList;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Random;

public class FKuroniAndThePunishment {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        int ans = 0;
        long g = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
            g = GCDs.gcd(a[i], g);
            if (a[i] % 2 != 0) {
                ans++;
            }
        }
        if (g > 1) {
            out.println(0);
            return;
        }
        long end = System.currentTimeMillis() + 2000;
        LongList factors = new LongList(100);
        RandomWrapper wrapper = new RandomWrapper();
        while (System.currentTimeMillis() < end) {
            int x = wrapper.nextInt(0, n - 1);
            int y;
            while ((y = wrapper.nextInt(0, n - 1)) == x) ;
            factors.clear();
            if (a[x] == 1 || a[y] == 1) {
                continue;
            }
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Factorization.factorizeNumberPrime(GCDs.gcd(a[x] + i, a[y] + j), factors);
                }
            }
            factors.unique();
            for (int i = 0; i < factors.size(); i++) {
                long f = factors.get(i);
                long local = 0;
                for (long v : a) {
                    long remainder = v % f;
                    long contrib = f - remainder;
                    if (v > remainder) {
                        contrib = Math.min(remainder, contrib);
                    }
                    local += contrib;
                }
                if (local < ans) {
                    debug.debug("f", f);
                    debug.debug("local", local);
                    ans = (int) local;
                }
            }
        }

        out.println(ans);
    }
}
