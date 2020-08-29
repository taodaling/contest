package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAnimalsAndPuzzle {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] square = new int[n + 1][m + 1];
        int[][] mat = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                mat[i][j] = in.readInt();
            }
        }
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                square[i][j] = mat[i][j];
                if (i > 0 && j > 0 && mat[i][j] == 1) {
                    square[i][j] = Math.min(Math.min(square[i - 1][j - 1], square[i - 1][j]), square[i][j - 1]) + 1;
                }
            }
        }

        int block = DigitUtils.ceilDiv(Math.min(n, m), 100);
        int[][][] partial = new int[block][n + 1][m + 1];

        MultiWayStack<int[]> stack = new MultiWayStack<>(Math.min(n, m) + 1, n * m);
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                stack.addLast(square[i][j], new int[]{i, j});
                for (int k = 0; k < block; k++) {
                    int l = k * 100;
                    int r = l + 100 - 1;
                    if (l <= square[i][j]) {
                        partial[k][i][j] = 1;
                    }
                }
            }
        }
        for (int[][] p : partial) {
            ps(p, p);
        }

        int t = in.readInt();
        Query[] qs = new Query[t];
        for (int i = 0; i < t; i++) {
            qs[i] = new Query();
            qs[i].b = in.readInt();
            qs[i].l = in.readInt();
            qs[i].t = in.readInt();
            qs[i].r = in.readInt();
        }

        for (Query q : qs) {
            for (int i = block - 1; i > 0; i--) {
                if (contain(q, partial[i], i * 100)) {
                    q.level = i;
                    break;
                }
            }
        }

        Query[] sorted = qs.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a.level, b.level));
        int[][] build = new int[n + 1][m + 1];
        int[][] ps = new int[n + 1][m + 1];
        SequenceUtils.reverse(sorted);
        for (int i = Math.min(n, m), indexHead = 0, qsHead = 0; i >= 1; i--) {
            //debug.debug("i", i);
            for(int[] e : stack.getStack(i)){
                build[e[0]][e[1]] = 1;
            }

            ps(build, ps);
            while (qsHead < sorted.length && sorted[qsHead].minLevel() > i) {
                qsHead++;
            }
            for (int j = qsHead; j < sorted.length && sorted[j].maxLevel() >= i; j++) {
                if (contain(sorted[j], ps, i)) {
                    sorted[j].ans = Math.max(sorted[j].ans, i);
                }
            }
        }

        for (Query q : qs) {
            out.println(q.ans);
        }
    }

    public boolean contain(Query q, int[][] ps, int level) {
        return region(ps, q.b + level - 1, q.t, q.l + level - 1, q.r) > 0;
    }

    public int region(int[][] ps, int b, int t, int l, int r) {
        if (b > t || l > r) {
            return 0;
        }
        return ps[t][r] - ps[b - 1][r] - ps[t][l - 1] + ps[b - 1][l - 1];
    }

    public void ps(int[][] mat, int[][] ps) {
        int n = mat.length;
        int m = mat[0].length;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                ps[i][j] = ps[i][j - 1] + mat[i][j];
            }
            for (int j = 1; j < m; j++) {
                ps[i][j] += ps[i - 1][j];
            }
        }
    }
}

class Query {
    int l;
    int r;
    int b;
    int t;
    int level = 0;
    int ans;

    public int minLevel() {
        return level * 100;
    }

    public int maxLevel() {
        return (level + 1) * 100 - 1;
    }
}