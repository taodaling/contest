package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BStringModification {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = in.readString().toCharArray();

        char[] best = s.clone();
        int need = 1;
        char[] reorder = new char[s.length];
        for (int i = 2; i <= n; i++) {
            apply(s, i, reorder);
            if (SortUtils.compareArray(best, 0, n - 1, reorder, 0, n - 1) > 0) {
                System.arraycopy(reorder, 0, best, 0, n);
                need = i;
            }
        }

        out.println(new String(best));
        out.println(need);
    }

    public void apply(char[] s, int k, char[] ans) {
        int tail = s.length - (k - 1);
        System.arraycopy(s, 0, ans, 0, s.length);
        SequenceUtils.reverse(ans, 0, ans.length - 1);
        SequenceUtils.reverse(ans, 0, tail - 1);
        if (tail % 2 == 0) {
            SequenceUtils.reverse(ans, tail, ans.length - 1);
        }
    }
}
