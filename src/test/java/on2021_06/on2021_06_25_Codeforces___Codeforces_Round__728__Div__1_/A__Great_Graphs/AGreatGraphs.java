package on2021_06.on2021_06_25_Codeforces___Codeforces_Round__728__Div__1_.A__Great_Graphs;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;

import java.util.Arrays;

public class AGreatGraphs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] d = in.ri(n);
        Randomized.shuffle(d);
        Arrays.sort(d);
        long ans = d[n - 1];
        LongPreSum lps = new LongPreSum(i -> d[i], n);
        for (int i = 0; i < n; i++) {
            ans += (long) d[i] * (n - i - 1) - lps.post(i + 1);
        }
        out.println(ans);
    }
}
