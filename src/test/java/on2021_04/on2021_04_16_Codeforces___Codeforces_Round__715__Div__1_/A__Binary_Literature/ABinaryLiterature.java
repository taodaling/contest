package on2021_04.on2021_04_16_Codeforces___Codeforces_Round__715__Div__1_.A__Binary_Literature;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ABinaryLiterature {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[][] s = new char[3][n * 2];
        int[][] cnts = new int[3][2];
        for (int i = 0; i < 3; i++) {
            in.rs(s[i]);
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n * 2; j++) {
                cnts[i][s[i][j] - '0']++;
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 3; k++) {
                    if (cnts[j][i] >= n && cnts[k][i] >= n) {
                        char[] ans = build(s[j], s[k], i);
                        out.println(ans);
                        return;
                    }
                }
            }
        }
    }

    public char[] build(char[] a, char[] b, int t) {
        t += '0';
        int n = a.length / 2;
        char[] res = new char[3 * n];
        Arrays.fill(res, '0');
        int wpos = 0;
        int i = 0;
        int j = 0;

        while (i < a.length || j < b.length) {
            while (i < a.length && a[i] != t) {
                res[wpos++] = a[i];
                i++;
            }
            while (j < b.length && b[j] != t) {
                res[wpos++] = b[j];
                j++;
            }
            if (i < a.length || j < b.length) {
                res[wpos++] = (char) t;
                i++;
                j++;
            }
        }

        return res;
    }
}
