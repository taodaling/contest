package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BBearAndCompressing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();

        transform = new int[6][6];
        SequenceUtils.deepFill(transform, -1);
        for (int i = 0; i < q; i++) {
            transform[in.rc() - 'a'][in.rc() - 'a'] = in.rc() - 'a';
        }

        int ans = enumerate(new int[n], 0);
        out.println(ans);
    }

    int[][] transform;

    public int enumerate(int[] s, int i) {
        if (i == s.length) {
            int last = s[0];
            for (int j = 1; j < s.length && last != -1; j++) {
                last = transform[last][s[j]];
            }
            return last == 0 ? 1 : 0;
        }
        int ans = 0;
        for (int j = 0; j < 6; j++) {
            s[i] = j;
            ans += enumerate(s, i + 1);
        }
        return ans;
    }

}
