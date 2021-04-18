package template.graph;


import template.utils.SortUtils;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * implementation of Landau's Theorem
 */
public class TournamentBuilder {
    /**
     * O(n^2)
     * res[i][j] = true means there is an edge i => j
     * @param deg
     * @return
     */
    public static Optional<boolean[][]> build(int[] deg) {
        int n = deg.length;
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(deg[i], deg[j]), 0, n);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += deg[indices[i]];
            if (sum < (i + 1) * i / 2) {
                return Optional.empty();
            }
        }
        if (sum != n * (n - 1) / 2) {
            return Optional.empty();
        }
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = deg[indices[i]];
        }
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    adj[i][j] = true;
                    b[i]++;
                }
            }
        }
        int i = 0;
        int j = 0;
        while (true) {
            while (i < n && a[i] == b[i]) {
                i++;
            }
            if (i == n) {
                break;
            }
            j = Math.max(j, i + 1);
            while (a[j] >= b[j]) {
                j++;
            }
            int t = 0;
            while(a[i] > b[i] && a[j] < b[j]) {
                while (!(adj[j][t] && adj[t][i])) {
                    t++;
                }
                b[j]--;
                b[i]++;
                adj[j][t] = !adj[j][t];
                adj[t][j] = !adj[t][j];

                adj[t][i] = !adj[t][i];
                adj[i][t] = !adj[i][t];
            }
        }

        boolean[][] res = new boolean[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                res[indices[r]][indices[c]] = adj[r][c];
            }
        }

        assert check(res, deg);
        return Optional.of(res);
    }

    static boolean check(boolean[][] mat, int[] deg) {
        int n = deg.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (mat[i][j] == mat[j][i]) {
                    return false;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            int d = 0;
            for (int j = 0; j < n; j++) {
                if (mat[i][j]) {
                    d++;
                }
            }
            if (d != deg[i]) {
                return false;
            }
        }
        return true;
    }
}
