package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class CStringInvasion {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 2e5];
        int n = in.rs(s);
        SequenceUtils.reverse(s, 0, n - 1);
        int lastMerge = -1;
        long ans = 0;
        //debug.debug("s", new String(s, 0, n));
        for (int i = 2; i < n; i++) {
            long contrib = 0;
            if (s[i] == s[i - 1] && s[i - 2] != s[i]) {
                for (int k = i - 2; k > lastMerge; k--) {
                    if (s[k] != s[i]) {
                        contrib++;
                    }
                }
                if (lastMerge >= 0 && s[lastMerge] != s[i]) {
                    contrib += lastMerge + 1;
                }
                lastMerge = i;
            }
            ans += contrib;
            // debug.debug("contrib", contrib);
        }
        out.println(ans);
    }
}
