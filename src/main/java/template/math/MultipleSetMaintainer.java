package template.math;

import template.primitve.generated.datastructure.LongHashSet;
import template.primitve.generated.datastructure.LongIterator;

import java.util.Arrays;

public class MultipleSetMaintainer {
    LongHashSet set;
    long g;
    long[] prev;
    long[] next;
    DivisorCollection col;

    public MultipleSetMaintainer(DivisorCollection divisor) {
        col = divisor;
        set = new LongHashSet(divisor.divisor.length + 1, false);
        g = divisor.factorization.g;
        prev = new long[divisor.divisor.length];
        next = new long[divisor.divisor.length];
        clear();
    }

    public void clear() {
        set.clear();
        size = 0;
    }

    /**
     * add kx % g for all k>=0
     */
    public void add(long x) {
        x = GCDs.gcd(x, g);
        set.add(x);
        size = -1;
    }

    long size;

    /**
     * based inclusion-exclusion
     * O(n^2\log_2g) and n is the number of factors of g
     */
    public long size() {
        if (size != -1) {
            return size;
        }
        Arrays.fill(prev, 0L);
        prev[(int) col.inverseMap.get(1)] = -1;
        int divisionNum = prev.length;
        for (LongIterator iterator = set.iterator(); iterator.hasNext(); ) {
            Arrays.fill(next, 0);
            long v = iterator.next();
            for (int j = 0; j < divisionNum; j++) {
                int to = (int) col.inverseMap.get(LCMs.lcm(v, col.divisor[j]));
                //add
                next[to] -= prev[j];
                next[j] += prev[j];
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        size = 0;
        for (int i = 0; i < divisionNum; i++) {
            size += prev[i] * (g / col.divisor[i]);
        }
        size += g;

        return size;
    }
}
