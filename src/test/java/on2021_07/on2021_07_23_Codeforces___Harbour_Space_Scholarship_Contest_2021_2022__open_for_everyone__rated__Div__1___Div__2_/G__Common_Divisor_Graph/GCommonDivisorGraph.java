package on2021_07.on2021_07_23_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.G__Common_Divisor_Graph;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.primitve.generated.datastructure.LongHashSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GCommonDivisorGraph {
    public long id(int a, int b) {
        if (a < b) {
            return DigitUtils.asLong(a, b);
        } else {
            return DigitUtils.asLong(b, a);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        int L = (int) 1e6;
        IntegerMultiWayStack primes = Factorization.factorizeRangePrime((int) 1e6 + 10);
        DSU dsu = new DSU(n);
        dsu.init();
        Set<Integer>[] sets = new Set[n];
        for (int i = 0; i < n; i++) {
            sets[i] = new HashSet<>();
        }

        LongHashSet em = new LongHashSet(7 * 6 / 2 * n, false);
        IntegerArrayList list = new IntegerArrayList(10);
        int[] reg = new int[L + 10];
        Arrays.fill(reg, -1);
        for (int i = 0; i < n; i++) {
            int x = a[i];

            for (IntegerIterator iterator = primes.iterator(x); iterator.hasNext(); ) {
                int p = iterator.next();
                if (reg[p] == -1) {
                    reg[p] = i;
                }
                dsu.merge(reg[p], i);
            }

        }
        for (int i = 0; i < n; i++) {
            int x = a[i];
            int root = dsu.find(i);
            list.clear();
            for (IntegerIterator iterator = primes.iterator(x + 1); iterator.hasNext(); ) {
                int p = iterator.next();
                if (reg[p] == -1) {
                    continue;
                }
                list.add(dsu.find(reg[p]));
                sets[root].add(dsu.find(reg[p]));
            }
            list.unique();
            int[] data = list.getData();
            int size = list.size();
            for (int j = 0; j < size; j++) {
                for (int t = j + 1; t < size; t++) {
                    em.add(id(data[j], data[t]));
                }
            }
        }
        for (int i = 0; i < q; i++) {
            int s = in.ri() - 1;
            int t = in.ri() - 1;
            s = dsu.find(s);
            t = dsu.find(t);
            if (s == t) {
                out.println(0);
            } else {
                if (sets[s].contains(t) || sets[t].contains(s) || em.contain(id(s, t))) {
                    out.println(1);
                } else {
                    out.println(2);
                }
            }
        }
    }
}

