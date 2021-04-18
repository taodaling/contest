package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Reversort;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

public class Reversort {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        long ans = 0;
        for (int i = 0; i < n - 1; i++) {
            int j = i;
            for (int k = i; k < n; k++) {
                if (a[k] < a[j]) {
                    j = k;
                }
            }
            ans += j - i + 1;
            debug.debug("i", i);
            debug.debug("j", j);
            SequenceUtils.reverse(a, i, j);
        }
        out.printf("Case #%d: %d", testNumber, ans).println();
    }
}
