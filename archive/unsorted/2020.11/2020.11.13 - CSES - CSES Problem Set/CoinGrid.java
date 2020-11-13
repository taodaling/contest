package contest;

import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;

public class CoinGrid {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        DinicBipartiteMatch dbm = new DinicBipartiteMatch(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (in.readChar() == 'o') {
                    dbm.addEdge(i, j);
                }
            }
        }
        dbm.solve();
        boolean[][] cover = dbm.minVertexCover();
        int ans = 0;
        for (boolean[] a : cover) {
            for (boolean x : a) {
                ans += x ? 1 : 0;
            }
        }
        out.println(ans);
        for (int i = 0; i < n; i++) {
            if (cover[0][i]) {
                out.append(1).append(' ').append(i + 1).println();
            }
        }
        for (int i = 0; i < n; i++) {
            if (cover[1][i]) {
                out.append(2).append(' ').append(i + 1).println();
            }
        }
    }
}
