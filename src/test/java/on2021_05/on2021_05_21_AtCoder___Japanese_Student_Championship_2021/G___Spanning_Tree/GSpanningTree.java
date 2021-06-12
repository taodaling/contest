package on2021_05.on2021_05_21_AtCoder___Japanese_Student_Championship_2021.G___Spanning_Tree;



import template.datastructure.DSU;
import template.graph.MatrixTreeTheoremBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class GSpanningTree {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] mat = new int[n][n];
        DSU dsu = new DSU(n);
        dsu.init();
        boolean loop = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri();
                if (mat[i][j] == 1 && i > j) {
                    if (dsu.find(i) == dsu.find(j)) {
                        loop = true;
                    }
                    dsu.merge(i, j);
                }
            }
        }

        if (loop) {
            out.println(0);
            return;
        }

        int nextId = 0;
        int[] toId = new int[n];
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                toId[i] = nextId++;
            }
        }
        int m = nextId;
        MatrixTreeTheoremBeta mtt = new MatrixTreeTheoremBeta(m, mod, true);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j && mat[i][j] == -1) {
                    mtt.addEdge(toId[dsu.find(i)], toId[dsu.find(j)], 1);
                }
            }
        }

        int ans = mtt.countSpanningTree();
        out.println(ans);
    }
}
