package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.IntArrayIntSequenceAdapter;
import template.string.SuffixArrayDC3;
import template.utils.SequenceUtils;

public class P4051JSOI2007 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 1e5 * 2];
        int n = in.rs(s);
        System.arraycopy(s, 0, s, n, n);
        int[] sa = SuffixArrayDC3.suffixArray(new IntArrayIntSequenceAdapter(s, 0, 2 * n - 1));
        for (int x : sa) {
            if (x < n) {
                out.append((char)s[x + n - 1]);
            }
        }
    }
}
