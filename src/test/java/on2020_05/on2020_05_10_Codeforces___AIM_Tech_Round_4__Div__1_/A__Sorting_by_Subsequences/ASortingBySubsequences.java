package on2020_05.on2020_05_10_Codeforces___AIM_Tech_Round_4__Div__1_.A__Sorting_by_Subsequences;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;

import java.util.List;

public class ASortingBySubsequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        IntegerDiscreteMap dm = new IntegerDiscreteMap(a.clone(), 0, a.length);
        for (int i = 0; i < n; i++) {
            a[i] = dm.rankOf(a[i]);
        }

        PermutationUtils.PowerPermutation pp = new PermutationUtils.PowerPermutation(a);
        List<IntegerList> list = pp.extractCircles();
        out.println(list.size());
        for (IntegerList c : list) {
            out.append(c.size()).append(' ');
            for (int i = 0; i < c.size(); i++) {
                out.append(c.get(i) + 1).append(' ');
            }
            out.println();
        }
    }
}
