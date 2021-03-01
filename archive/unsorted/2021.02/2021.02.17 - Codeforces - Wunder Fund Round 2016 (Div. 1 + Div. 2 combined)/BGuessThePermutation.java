package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BGuessThePermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] mat = new int[n][];
        for (int i = 0; i < n; i++) {
            mat[i] = in.ri(n);
        }
        boolean[] handled = new boolean[n];
        int[] perm = new int[n];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                if (handled[j]) {
                    continue;
                }
                int max = 0;
                for (int k = 0; k < n; k++) {
                    if (handled[k]) {
                        continue;
                    }
                    max = Math.max(max, mat[j][k]);
                }
                if (max == i) {
                    handled[j] = true;
                    perm[j] = i;
                    break;
                }
            }
        }

        for (int x : perm) {
            out.append(x == 0 ? n : x).append(' ');
        }
    }
}
