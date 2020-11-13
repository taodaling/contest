package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BArrayGCD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long a = in.readLong();
        long b = in.readLong();
        int[] x = new int[n];
        in.populate(x);

        IntegerArrayList list = new IntegerArrayList();
        for(int i = -1; i <= 1; i++) {
            list.addAll(Factorization.factorizeNumberPrime(x[0] + i));
            list.addAll(Factorization.factorizeNumberPrime(x[n - 1] + i));
        }

        long inf = (long) 1e18;
        long cost = inf;
        long[] costEach = new long[n];
        long[] prev = new long[3];
        long[] next = new long[3];
        Set<Integer> added = new HashSet<>();
        for (int factor : list.toArray()) {
            if (added.contains(factor)) {
                continue;
            }
            added.add(factor);
            Arrays.fill(costEach, inf);
            for (int i = 0; i < n; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((x[i] + j) % factor == 0) {
                        costEach[i] = Math.min(costEach[i], b * Math.abs(j));
                    }
                }
            }
            Arrays.fill(prev, 0);
            for (int i = 0; i < n; i++) {
                Arrays.fill(next, inf);
                next[0] = Math.min(next[0], prev[0] + costEach[i]);
                next[1] = Math.min(next[1], prev[0] + a);
                next[1] = Math.min(next[1], prev[1] + a);
                next[2] = Math.min(next[2], prev[2] + costEach[i]);
                next[2] = Math.min(next[2], prev[1] + costEach[i]);
                long[] tmp = prev;
                prev = next;
                next = tmp;
            }
            cost = Math.min(cost, Arrays.stream(prev).min().orElse(-1));
        }
        out.println(cost);
    }
}
