package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class PigeonholePrinciple {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[5 * n];
        in.readString(s, 0);
        int[] state = new int[n];
        for (int i = 0; i < n; i++) {
            int l = i * 5;
            int r = l + 4;
            state[i] = s[l] == '<' ? 0 : 1;
        }
        int[][][] nicePattern = new int[2][2][n];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) {
                nicePattern[i][0][j] = j < n / 2 ? 0 : 1;
                nicePattern[i][1][j] = j % 2 == 0 ? 0 : 1;
                nicePattern[i][0][j] ^= i;
                nicePattern[i][1][j] ^= i;
            }
        }

        int ans = (int) 1e9;
        for (int[][] r2 : nicePattern) {
            for (int[] r1 : r2) {
                int sum = 0;
                for (int i = 0; i < n; i++) {
                    sum += r1[i] ^ state[i];
                }
                ans = Math.min(ans, sum);
            }
        }
        out.println(ans);
    }
}
