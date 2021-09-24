package on2021_08.on2021_08_04_AtCoder___AtCoder_Beginner_Contest_210.F___Coprime_Solitaire;



import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.List;

public class FCoprimeSolitaire {
    int L = (int) 2e6;
    List<Integer>[] map = new List[L + 1];

    {
        for (int i = 0; i <= L; i++) {
            map[i] = new ArrayList<>();
        }
    }

    int S = (int)1.5e6;
    IntegerMultiWayStack primeFactors = Factorization.factorizeRangePrime(L);
    TwoSatBeta ts = new TwoSatBeta(S, S * 4);
    int alloc;

    public int next() {
        return alloc++;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            int a = in.ri();
            int b = in.ri();
            for (IntegerIterator it = primeFactors.iterator(a); it.hasNext(); ) {
                int p = it.next();
                map[p].add(i);
            }
            for (IntegerIterator it = primeFactors.iterator(b); it.hasNext(); ) {
                int p = it.next();
                map[p].add(i + n);
            }
        }

        alloc = n;
        for (List<Integer> ls : map) {
            if (ls.size() <= 1) {
                continue;
            }
            int m = ls.size();
            int[] prev = new int[m];
            int[] next = new int[m];
            for (int j = 0; j < m; j++) {
                prev[j] = next();
                int v = ls.get(j);
                int id = v < n ? ts.elementId(v) : ts.negateElementId(v - n);
                ts.deduce(ts.elementId(prev[j]), ts.negate(id));
                if (j > 0) {
                    ts.deduce(ts.elementId(prev[j]), ts.elementId(prev[j - 1]));
                }
            }
            for (int j = m - 1; j >= 0; j--) {
                next[j] = next();
                int v = ls.get(j);
                int id = v < n ? ts.elementId(v) : ts.negateElementId(v - n);
                ts.deduce(ts.elementId(next[j]), ts.negate(id));
                if (j + 1 < m) {
                    ts.deduce(ts.elementId(next[j]), ts.elementId(next[j + 1]));
                }
            }
            for (int j = 0; j < m; j++) {
                int v = ls.get(j);
                int id = v < n ? ts.elementId(v) : ts.negateElementId(v - n);
                if (j > 0) {
                    ts.deduce(id, ts.elementId(prev[j - 1]));
                }
                if (j + 1 < m) {
                    ts.deduce(id, ts.elementId(next[j + 1]));
                }
            }
        }

        boolean[] ans = ts.solve();
        if(ans == null){
            out.println("No");
        }else{
            out.println("Yes");
        }
    }
}
