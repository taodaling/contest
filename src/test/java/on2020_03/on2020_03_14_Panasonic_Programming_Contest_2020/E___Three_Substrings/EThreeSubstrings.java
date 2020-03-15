package on2020_03.on2020_03_14_Panasonic_Programming_Contest_2020.E___Three_Substrings;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.Arrays;

public class EThreeSubstrings {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] a = in.readString().toCharArray();
        char[] b = in.readString().toCharArray();
        char[] c = in.readString().toCharArray();

        int ans = a.length + b.length + c.length;
        ans = Math.min(ans, solve(a, b, c));
        ans = Math.min(ans, solve(a, c, b));
        ans = Math.min(ans, solve(b, a, c));
        ans = Math.min(ans, solve(b, c, a));
        ans = Math.min(ans, solve(c, b, a));
        ans = Math.min(ans, solve(c, a, b));

        out.println(ans);
    }

    public boolean match(char[] a, int offset, char[] b) {
        for (int i = 0; i < b.length; i++) {
            if (a[offset + i] == '?' || b[i] == '?' || a[offset + i] == b[i]) {
                continue;
            }
            return false;
        }
        return true;
    }

    public int append(char[] s, char[] x) {
        for (int i = 0; ; i++) {
            if (!match(s, i, x)) {
                continue;
            }
            return i;
        }
    }

    public int solve(char[] a, char[] b, char[] c) {
        char[] builder = new char[a.length + b.length + c.length];
        Arrays.fill(builder, '?');
        int ans = builder.length;
        for (int j = 0; j <= a.length; j++) {
            Arrays.fill(builder, '?');
            System.arraycopy(a, 0, builder, 0, a.length);
            int aEnd = a.length;
            if (match(builder, j, b)) {
                int bEnd = j + b.length;
                for (int i = 0; i < b.length; i++) {
                    if (builder[i + j] == '?') {
                        builder[i + j] = b[i];
                    }
                }
                int cEnd = append(builder, c) + c.length;
                ans = Math.min(ans, Math.max(aEnd, Math.max(bEnd, cEnd)));
            }
        }
        debug.debug("ans", builder);
        return ans;
    }

}
