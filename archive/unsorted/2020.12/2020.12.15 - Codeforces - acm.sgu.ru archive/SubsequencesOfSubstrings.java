package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class SubsequencesOfSubstrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        char[] t = new char[100];
        int n = in.rs(s, 0);
        int m = in.rs(t, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        for (int i = 0; i < m; i++) {
            t[i] -= 'a';
        }
        int charset = 'z' - 'a' + 1;
        int[][] next = new int[n][charset];
        Arrays.fill(next[n - 1], n);
        for (int i = n - 2; i >= 0; i--) {
            System.arraycopy(next[i + 1], 0, next[i], 0, charset);
            next[i][s[i + 1]] = i + 1;
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int a = s[i] == t[0] ? i : next[i][t[0]];
            int b = 1;
            while (a < n && b < m) {
                a = next[a][t[b]];
                b++;
            }
            ans += n - a;
        }
        out.println(ans);
    }
}
