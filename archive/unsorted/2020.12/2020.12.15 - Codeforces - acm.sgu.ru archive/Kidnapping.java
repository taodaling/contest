package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class Kidnapping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] edges = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                edges[i][j] = in.ri();
            }
        }
        boolean[] prev = new boolean[n];
        boolean[] next = new boolean[n];
        prev[0] = true;
        int k = in.ri();
        for (int i = 0; i < k; i++) {
            Arrays.fill(next, false);
            int r = in.ri();
            for (int j = 0; j < n; j++) {
                if (!prev[j]) {
                    continue;
                }
                for (int t = 0; t < n; t++) {
                    if (edges[j][t] == r) {
                        next[t] = true;
                    }
                }
            }
            boolean[] tmp = prev;
            prev = next;
            next = tmp;
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (prev[i]) {
                sum++;
            }
        }
        out.println(sum);
        for (int i = 0; i < n; i++) {
            if (prev[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
