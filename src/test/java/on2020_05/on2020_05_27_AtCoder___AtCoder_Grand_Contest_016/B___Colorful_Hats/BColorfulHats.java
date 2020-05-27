package on2020_05.on2020_05_27_AtCoder___AtCoder_Grand_Contest_016.B___Colorful_Hats;



import template.io.FastInput;
import template.io.FastOutput;

public class BColorfulHats {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        int low = n;
        int high = 0;
        int[] cnt = new int[n + 1];
        for (int i = 0; i < n; i++) {
            low = Math.min(low, a[i]);
            high = Math.max(high, a[i]);
            cnt[a[i]]++;
        }

        String NO = "No";
        String YES = "Yes";
        if (high - low > 1) {
            out.println(NO);
            return;
        }

        if (low == high) {
            //distinct
            if (low == n - 1) {
                out.println(YES);
                return;
            }
            //duplicate
            if (low * 2 <= n) {
                out.println(YES);
                return;
            }
            out.println(NO);
            return;
        }

        int unique = cnt[low];
        if (high - unique >= 1 && (high - unique) * 2 <= cnt[high]) {
            out.println(YES);
            return;
        }
        out.println(NO);
    }
}
