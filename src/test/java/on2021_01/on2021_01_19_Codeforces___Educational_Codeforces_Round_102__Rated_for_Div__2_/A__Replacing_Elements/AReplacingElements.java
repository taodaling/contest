package on2021_01.on2021_01_19_Codeforces___Educational_Codeforces_Round_102__Rated_for_Div__2_.A__Replacing_Elements;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class AReplacingElements {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int d = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        for (int i = 0; i < n; i++) {
            if (a[i] <= d || a[0] + a[1] <= d) {
                continue;
            }
            out.println("NO");
            return;
        }
        out.println("YES");
    }
}
