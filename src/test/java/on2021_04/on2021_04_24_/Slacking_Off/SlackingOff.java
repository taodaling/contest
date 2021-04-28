package on2021_04.on2021_04_24_.Slacking_Off;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class SlackingOff {
    int limit = (int) 1e6;
    int[][] next = new int[2][limit];
    int[] cnt = new int[limit];

    int alloc;

    int root() {
        return 0;
    }

    void reset() {
        alloc = 0;
        alloc();
    }

    int alloc() {
        next[0][alloc] = next[1][alloc] = cnt[alloc] = 0;
        return alloc++;
    }

    int transfer(int id, int i) {
        if (next[i][id] == 0) {
            next[i][id] = alloc();
        }
        return next[i][id];
    }

    void inc(int id, int x) {
        cnt[id] += x;
    }

    int size(int id) {
        return cnt[id];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.rc() == 'B' ? 0 : 1;
            }
        }
        if (n > m) {
            int[][] format = new int[m][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    format[j][i] = mat[i][j];
                }
            }
            int tmp = n;
            n = m;
            m = tmp;
            mat = format;
        }

        long ans = 0;
        int[] nodes = new int[m];
        boolean[] same = new boolean[m];
        for (int low = 0; low < n; low++) {
            long contrib = 0;
            reset();
            Arrays.fill(nodes, root());
            for (int high = low; high < n; high++) {
                for (int i = 0; i < m; i++) {
                    same[i] = mat[low][i] == mat[high][i];
                    nodes[i] = transfer(nodes[i], mat[high][i]);
                }
                if (high == low) {
                    continue;
                }
                for (int i = 0; i < m; i++) {
                    if (!same[i]) {
                        continue;
                    }
                    int l = i;
                    int r = i;
                    while (r + 1 < m && same[r + 1]) {
                        r++;
                    }
                    i = r;
                    for (int j = l; j <= r; j++) {
                        contrib += size(nodes[j]);
                        inc(nodes[j], 1);
                    }
                    for (int j = l; j <= r; j++) {
                        inc(nodes[j], -1);
                    }
                }
            }

            ans += contrib;
        }

        out.println(ans);
    }
}
