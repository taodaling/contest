package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DLinearCongruentialGenerator {
    public int log(int x, int y) {
        int ans = 0;
        while (y != 0 && y % x == 0) {
            ans++;
            y /= x;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int limit = (int) 2e6;
        IntegerMultiWayStack primeFactors = Factorization.factorizeRangePrime(limit);
        int[] cnts = new int[limit + 1];
        int[] times = new int[limit + 1];
        int[] xs = new int[n];

        in.populate(xs);
        Randomized.shuffle(xs);
        Arrays.sort(xs);
        SequenceUtils.reverse(xs);
        boolean[] retain = new boolean[n];
        for (int i = 0; i < n; i++) {
            int x = xs[i];
            if (cnts[x] == 0) {
                retain[i] = true;
                cnts[x] = 1;
                times[x] = 1;
                continue;
            }

            for (IntegerIterator iterator = primeFactors.iterator(x - 1); iterator.hasNext(); ) {
                int p = iterator.next();
                int log = log(p, x - 1);
                if (cnts[p] < log) {
                    cnts[p] = log;
                    times[p] = 0;
                }
                if (cnts[p] == log) {
                    times[p]++;
                }
            }
        }

        Modular mod = new Modular(1e9 + 7);
        int prod = 1;
        for (int i = 1; i <= limit; i++) {
            for (int j = 0; j < cnts[i]; j++) {
                prod = mod.mul(prod, i);
            }
        }

        for (int i = 0; i < n; i++) {
            int x = xs[i];
            boolean unique = false;
            if (retain[i]) {
                if (cnts[x] == 1 && times[x] == 1) {
                    unique = true;
                }
            } else {
                for (IntegerIterator iterator = primeFactors.iterator(x - 1); iterator.hasNext(); ) {
                    int p = iterator.next();
                    int log = log(p, x - 1);
                    if (cnts[p] == log && times[p] == 1) {
                        unique = true;
                    }
                }
            }
            if (!unique) {
                prod = mod.plus(prod, 1);
                break;
            }
        }

        out.println(prod);
    }
}
