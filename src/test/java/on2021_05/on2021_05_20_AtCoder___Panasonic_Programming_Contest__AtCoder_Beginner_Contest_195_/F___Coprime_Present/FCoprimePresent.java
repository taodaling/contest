package on2021_05.on2021_05_20_AtCoder___Panasonic_Programming_Contest__AtCoder_Beginner_Contest_195_.F___Coprime_Present;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.math.BigInteger;
import java.util.Arrays;

public class FCoprimePresent {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        final int L = 72;
        long A = in.rl();
        long B = in.rl();
        EulerSieve sieve = new EulerSieve(L);
        IntegerArrayList primeList = new IntegerArrayList();
        for (int i = 1; i <= L; i++) {
            if (sieve.isPrime(i)) {
                primeList.add(i);
            }
        }
        int[] p = primeList.toArray();
        long[] prev = new long[1 << p.length];
        prev[0] = 1;
        long[] next = new long[1 << p.length];
        for (long i = A; i <= B; i++) {
            Arrays.fill(next, 0);
            int state = 0;
            for (int j = 0; j < p.length; j++) {
                if (i % p[j] == 0) {
                    state |= 1 << j;
                }
            }
            for (int j = 0; j < prev.length; j++) {
                if ((j & state) == 0) {
                    next[j | state] += prev[j];
                }
                next[j] += prev[j];
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = Arrays.stream(prev).sum();
        out.println(ans);
    }

    Debug debug = new Debug(true);
    int inf = (int) 1e9;

}
