package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.graph.UndirectedOneWeightMinCircle;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.List;

public class EEhabsREALNumberTheoryProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        EulerSieve sieve = new EulerSieve(1000000);
        IntegerMultiWayStack allPrime = Factorization.factorizeRangePrime(1000000);
        IntegerHashMap primeToIndex = new IntegerHashMap(sieve.getPrimeCount() + 1, false);
        for (int i = 0, end = sieve.getPrimeCount(); i < end; i++) {
            primeToIndex.put(sieve.get(i), i);
        }
        primeToIndex.put(1, sieve.getPrimeCount());
        List<UndirectedEdge>[] g = Graph.createUndirectedGraph(primeToIndex.size());

        IntegerList collect = new IntegerList(2);
        for (int i = 0; i < n; i++) {
            int x = a[i];
            collect.clear();
            for (IntegerIterator iterator = allPrime.iterator(x); iterator.hasNext(); ) {
                int p = iterator.next();
                int cnt = 0;
                while (x % p == 0) {
                    x /= p;
                    cnt ^= 1;
                }
                if (cnt == 1) {
                    collect.add(p);
                }
            }
            if (collect.size() == 0) {
                out.println(1);
                return;
            }
            if (collect.size() == 1) {
                collect.add(1);
            }
            //debug.debug("a", collect.get(0));
            //debug.debug("b", collect.get(1));
            Graph.addUndirectedEdge(g, primeToIndex.get(collect.get(0)), primeToIndex.get(collect.get(1)));
        }

        IntegerList circle = new IntegerList(n);
        UndirectedOneWeightMinCircle minCircle = new UndirectedOneWeightMinCircle(g);
        minCircle.optimize(sieve.getPrimeCount(), circle);
        for (int i = 0; sieve.get(i) < 1000; i++) {
            minCircle.optimize(i, circle);
        }

        if (circle.size() == 0) {
            out.println(-1);
            return;
        }
        out.println(circle.size());
    }
}
