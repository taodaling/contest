package on2021_08.on2021_08_13_CS_Academy___Virtual_Beta_Round__7.Subarray_Removal;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class SubarrayRemoval {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        if (Arrays.stream(a).max().orElse(-1) < 0) {
            out.println(Arrays.stream(a).max().orElse(-1));
            return;
        }
        long[] pref = prefix(a);
        SequenceUtils.reverse(a);
        long[] suf = prefix(a);
        SequenceUtils.reverse(suf);
        long best = 0;
        for (int i = 0; i < n; i++) {
            long left = i == 0 ? 0 : pref[i - 1];
            long right = i == n - 1 ? 0 : suf[i + 1];
            best = Math.max(best, left + right);
        }
        out.println(best);
    }


    long[] prefix(int[] a) {
        long[] ans = new long[a.length];
        for (int i = 0; i < a.length; i++) {
            long last = i == 0 ? 0 : ans[i - 1];
            ans[i] = Math.max(0, last + a[i]);
        }
        for (int i = 1; i < a.length; i++) {
            ans[i] = Math.max(ans[i], ans[i - 1]);
        }
        return ans;
    }
}
