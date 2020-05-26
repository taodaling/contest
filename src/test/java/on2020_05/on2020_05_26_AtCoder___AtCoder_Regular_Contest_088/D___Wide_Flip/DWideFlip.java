package on2020_05.on2020_05_26_AtCoder___AtCoder_Regular_Contest_088.D___Wide_Flip;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class DWideFlip {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 1e5 + 1];
        int n = in.readString(s, 0);

        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }

        s[n] = 0;
        int[] diff = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            if (i == 0) {
                diff[i] = s[i];
            } else {
                diff[i] = s[i] == s[i - 1] ? 0 : 1;
            }
        }

        int cnt = 0;
        for (int i = 0; i <= n; i++) {
            cnt += diff[i];
        }

        debug.debug("prefix", diff);
        if (cnt % 2 == 1) {
            out.println(1);
            return;
        }

        int ans = n;
        for (int i = 0; i <= n; i++) {
            if (diff[i] == 0) {
                continue;
            }
            int len = Math.max(i, n - i);
            ans = Math.min(ans, len);
        }

        out.println(ans);
    }
}
