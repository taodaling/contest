package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum2D;
import template.utils.SortUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class P2741USACO44FrameUp {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[][] mat = new char[n][];
        for (int i = 0; i < n; i++) {
            mat[i] = in.rc(m);
        }
        Grid[] gs = new Grid[128];
        for (char[] row : mat) {
            for (char c : row) {
                if (c == '.') {
                    continue;
                }
                if (gs[c] == null) {
                    gs[c] = new Grid(mat, c);
                }
            }
        }
        List<Grid> cand = new ArrayList<>();
        for (Grid g : gs) {
            if (g != null) {
                cand.add(g);
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!cand.isEmpty()) {
            Grid remove = null;
            for (Grid g : cand) {
                if (g.ready) {
                    remove = g;
                    break;
                }
            }
            assert remove != null;
            cand.remove(remove);
            debug.debug("remove", remove.mainCh);
            ans.append(remove.mainCh);
            for (Grid g : cand) {
                for (int[] pos : remove.pos) {
                    g.active(pos[0], pos[1]);
                }
                g.recalc();
            }
        }
        out.println(ans.reverse().toString());
    }


}

class Grid {
    int[][] mat;
    int l;
    int r;
    int b;
    int t;
    char mainCh;
    List<int[]> pos = new ArrayList<>();
    IntegerPreSum2D ps;
    boolean ready;

    public void active(int i, int j) {
        mat[i][j] = 1;
    }

    public void recalc() {
        if (ready) {
            return;
        }
        int n = mat.length;
        int m = mat[0].length;
        int cnt = pos.size();
        ps.init((i, j) -> mat[i][j], n, m);

        for (b = 0; b < n; b++) {
            for (t = b + 2; t < n; t++) {
                for (l = 0; l < m; l++) {
                    for (r = l + 2; r < m; r++) {
                        int size = (t - b) * 2 + (r - l) * 2;
                        if (size < cnt) {
                            continue;
                        }
                        int lack = size - cnt;
                        int expect = cnt * 10000 + lack;
                        if (ps.rect(b, t, l, r) - ps.rect(b + 1, t - 1, l + 1, r - 1) == expect) {
                            ready = true;
                            return;
                        }
                    }
                }
            }
        }
    }

    public Grid(char[][] mat, char mainCh) {
        this.mainCh = mainCh;
        int n = mat.length;
        int m = mat[0].length;

        this.mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == mainCh) {
                    pos.add(new int[]{i, j});
                    this.mat[i][j] = 10000;
                }
            }
        }
        ps = new IntegerPreSum2D(n, m);
        recalc();
    }
}