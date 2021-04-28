package on2021_04.on2021_04_21_Codeforces___Codeforces_Round__201__Div__1_.C__Number_Transformation_II;



import template.datastructure.MonotoneOrderBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerComparator;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Comparator;

public class CNumberTransformationII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = in.ri(n);
        int[] unique = SortUtils.unique(x, 0, n - 1, IntegerComparator.NATURE_ORDER);
        int a = in.ri();
        int b = in.ri();
        int[] max = new int[a - b + 1];
        Arrays.fill(max, 2);
        for (int u : unique) {
            int start = DigitUtils.ceilDiv(b, u) * u;
            for (int i = start - b; i < max.length; i += u) {
                max[i] = Math.max(max[i], u);
            }
        }
        MonotoneOrderBeta<Integer, Integer> mo = new MonotoneOrderBeta<Integer, Integer>(Comparator.naturalOrder(), Comparator.naturalOrder(),
                false, false);
        int[] dist = new int[a - b + 1];
        dist[dist.length - 1] = 0;
        mo.add(dist.length - 1, 0);
        for (int i = max.length - 2; i >= 0; i--) {
            dist[i] = mo.floor(i + max[i] - 1) + 1;
            mo.add(i, dist[i]);
        }
        int ans = dist[0];
        out.println(ans);
    }
}
