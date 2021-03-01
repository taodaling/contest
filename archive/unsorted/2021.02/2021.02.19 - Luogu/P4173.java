package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.string.MultiMatch;

public class P4173 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = in.ri();
        char[] A = new char[m];
        char[] B = new char[n];
        in.rs(A);
        in.rs(B);
        int[][] pattern = new int[1][m];
        int[] source = new int[n];
        for (int i = 0; i < m; i++) {
            pattern[0][i] = A[i] == '*' ? 0 : A[i] - 'a' + 1;
        }
        for (int i = 0; i < n; i++) {
            source[i] = B[i] == '*' ? 0 : B[i] - 'a' + 1;
        }
        boolean[] match = new MultiMatch().match(pattern, source, true);
        int cnt = 0;
        for (boolean x : match) {
            if (x) {
                cnt++;
            }
        }
        out.println(cnt);
        for (int i = 0; i < n; i++) {
            if (match[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
