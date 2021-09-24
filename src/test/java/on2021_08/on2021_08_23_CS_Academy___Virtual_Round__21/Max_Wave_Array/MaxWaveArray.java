package on2021_08.on2021_08_23_CS_Academy___Virtual_Round__21.Max_Wave_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.TreeSet;

public class MaxWaveArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        TreeSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            set.add(in.ri());
        }
        IntegerArrayList seq = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            boolean max = i % 2 == 0;
            if (max) {
                int pref = set.pollLast();
                seq.add(pref);
                continue;
            }
            if (set.size() == 1) {
                seq.add(set.pollLast());
                continue;
            }
            int v = set.floor(set.last() - 1);
            set.remove(v);
            seq.add(v);
        }
        for(int x : seq.toArray()){
            out.append(x).append(' ');
        }
    }
}
