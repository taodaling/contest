package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.C__Pursuit;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class CPursuit {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);
        SequenceUtils.reverse(a);
        SequenceUtils.reverse(b);
        IntPredicate predicate = m -> {
            int round = (m + n);
            int k = round - round / 4;
            long sumA = 100L * Math.min(k, m);
            for (int i = Math.min(k, m), l = 0; i < k; i++, l++) {
                sumA += a[l];
            }
            long sumB = 0;
            for (int i = 0; i < n && i < k; i++) {
                sumB += b[i];
            }
            return sumA >= sumB;
        };
        int first = BinarySearch.firstTrue(predicate, 0, (int) 1e9);
        out.println(first);
    }
}
