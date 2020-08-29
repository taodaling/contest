package on2020_08.on2020_08_24_Codeforces___Codeforces_Round__371__Div__1_.C__Sonya_and_Problem_Wihtout_a_Legend;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.LongDiscreteMap;

import java.util.Arrays;

public class CSonyaAndProblemWihtoutALegend {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        for (int i = 0; i < n; i++) {
            a[i] -= i;
        }
        IntegerDiscreteMap dm = new IntegerDiscreteMap(a.clone(), 0, n);
        int m = dm.maxRank() + 1;
        long[] last = new long[m];
        long[] next = new long[m];
        long inf = (long) 1e18;
        Arrays.fill(last, inf);
        last[0] = 0;
        for (int i = 0; i < n; i++) {
            long cost = inf;
            for (int j = 0; j < m; j++) {
                cost = Math.min(cost, last[j]);
                next[j] = cost + Math.abs(a[i] - dm.iThElement(j));
            }
            long[] tmp = last;
            last = next;
            next = tmp;
        }
        long ans = Arrays.stream(last).min().orElse(0);
        out.println(ans);
    }
}
