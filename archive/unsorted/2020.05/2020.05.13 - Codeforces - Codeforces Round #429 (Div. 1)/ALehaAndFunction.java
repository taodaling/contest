package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.CompareUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ALehaAndFunction {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int[] a = new int[m];
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
        }
        int[] indices = new int[m];
        for (int i = 0; i < m; i++) {
            indices[i] = i;
        }
        CompareUtils.quickSort(indices, (x, y) -> Integer.compare(b[x], b[y]), 0, m);
        Randomized.shuffle(a);
        Arrays.sort(a);
        SequenceUtils.reverse(a);

        debug.debug("indices", indices);
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            ans[indices[i]] = a[i];
        }
        for (int i = 0; i < m; i++) {
            out.append(ans[i]).append(' ');
        }
    }
}
