package on2019_11.on2019_11_23_Hello_2019.F___Alex_and_a_TV_Show0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;

import java.util.BitSet;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int limit = 7000;
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(limit,
                true, false, false);
        BitSet[] mu = new BitSet[limit + 1];
        BitSet[] divisors = new BitSet[limit + 1];
        for(int i = 1; i <= limit; i++){
            mu[i] = new BitSet(limit + 1);
            divisors[i] = new BitSet(limit + 1);
        }
        for (int i = 1; i <= limit; i++) {
            for (int j = i; j <= limit; j += i) {
                mu[i].set(j, sieve.mobius[j / i] != 0);
                divisors[j].set(i, true);
            }
        }

        BitSet[] sets = new BitSet[n + 1];
        for (int i = 0; i <= n; i++) {
            sets[i] = new BitSet(limit + 1);
        }

        for (int i = 1; i <= q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int x = in.readInt();
                int v = in.readInt();
                sets[x].clear();
                sets[x].or(divisors[v]);
            } else if (t == 2) {
                int x = in.readInt();
                int y = in.readInt();
                int z = in.readInt();
                if (x == y && x == z) {
                    sets[x].xor(sets[x]);
                } else if (x == y) {
                    sets[x].xor(sets[z]);
                } else if (x == z) {
                    sets[x].xor(sets[y]);
                } else {
                    sets[x].clear();
                    sets[x].or(sets[y]);
                    sets[x].xor(sets[z]);
                }
            } else if (t == 3) {
                int x = in.readInt();
                int y = in.readInt();
                int z = in.readInt();
                if (x == y && x == z) {
                    sets[x].and(sets[x]);
                } else if (x == y) {
                    sets[x].and(sets[z]);
                } else if (x == z) {
                    sets[x].and(sets[y]);
                } else {
                    sets[x].clear();
                    sets[x].or(sets[y]);
                    sets[x].and(sets[z]);
                }
            } else {
                int x = in.readInt();
                int v = in.readInt();
                sets[0].clear();
                sets[0].or(sets[x]);
                sets[0].and(mu[v]);
                out.append(sets[0].cardinality() % 2);
            }
        }
    }
}
