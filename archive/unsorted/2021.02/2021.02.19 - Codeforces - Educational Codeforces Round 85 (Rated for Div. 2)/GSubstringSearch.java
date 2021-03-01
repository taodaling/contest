package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MultiMatch;
import template.utils.PrimitiveBuffers;

public class GSubstringSearch {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] perm = new int[26];
        for (int i = 0; i < 26; i++) {
            perm[i] = in.ri() - 1;
        }
        char[] s = new char[(int) 2e5];
        char[] t = new char[(int) 2e5];
        int n = in.rs(s);
        int m = in.rs(t);
        int[][] pattern = new int[2][n];
        for (int i = 0; i < n; i++) {
            pattern[0][i] = s[i] - 'a';
            pattern[1][i] = perm[s[i] - 'a'];
        }
        int[] source = new int[m];
        for (int i = 0; i < m; i++) {
            source[i] = t[i] - 'a';
        }
        MultiMatch mm = new MultiMatch();
        boolean[] match = mm.match(pattern, source, false);
        for (int i = 0; i + n <= m; i++) {
            out.append(match[i] ? 1 : 0);
        }
    }
}
