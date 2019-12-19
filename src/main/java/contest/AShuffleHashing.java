package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class AShuffleHashing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] p = in.readString().toCharArray();
        char[] h = in.readString().toCharArray();

        for (int i = 0; i + p.length <= h.length; i++) {
            if(SequenceUtils.equal(summary(p, 0, p.length - 1),
                     0, 'z' - 'a', summary(h, i, i + p.length - 1),
                    0, 'z' - 'a'))
            {
                out.println("YES");
                return;
            }
        }

        out.println("NO");
    }

    public int[] summary(char[] s, int l, int r) {
        int[] cnt = new int['z' - 'a' + 1];
        for (int i = l; i <= r; i++) {
            cnt[s[i] - 'a']++;
        }
        return cnt;
    }
}
