package on2021_02.on2021_02_01_Codeforces___Codeforces_Round__313__Div__1_.B__Equivalent_Strings1;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BEquivalentStrings {
    char[] a, b;

    public void minimal(char[] s, int l, int r) {
        if ((r - l + 1) % 2 == 1) {
            return;
        }
        int m = (l + r) / 2;
        minimal(s, l, m);
        minimal(s, m + 1, r);
        if (CompareUtils.compareArray(s, l, m, s, m + 1, r) > 0) {
            for (int i = l; i <= m; i++) {
                SequenceUtils.swap(s, i, i + m + 1 - l);
            }
        }
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        a = in.rs().toCharArray();
        b = in.rs().toCharArray();
        minimal(a, 0, a.length - 1);
        minimal(b, 0, b.length - 1);
        debug.debugArray("a", a);
        debug.debugArray("b", b);
        if (CompareUtils.compareArray(a, 0, a.length - 1, b, 0, b.length - 1) == 0) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }
}
