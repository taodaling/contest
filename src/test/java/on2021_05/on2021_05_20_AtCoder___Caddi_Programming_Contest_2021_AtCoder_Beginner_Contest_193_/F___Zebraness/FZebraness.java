package on2021_05.on2021_05_20_AtCoder___Caddi_Programming_Contest_2021_AtCoder_Beginner_Contest_193_.F___Zebraness;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.utils.Debug;
import template.utils.GridUtils;

import java.util.List;

public class FZebraness {
    int n;

    int idOf(int i, int j) {
        return i * n + j;
    }

    int idOfSrc() {
        return n * n;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        char[][] mat = new char[n][n];
        int inf = (int) 1e8;
        List<IntegerFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.rc();
                if ((i + j) % 2 == 1 && mat[i][j] != '?') {
                    mat[i][j] = mat[i][j] == 'B' ? 'W' : 'B';
                }
            }
        }

        debug.debugMatrix("mat", mat);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 'B') {
                    IntegerFlow.addFlowEdge(g, idOfSrc(), idOf(i, j), inf);
                }
                if (mat[i][j] == 'W') {
                    IntegerFlow.addFlowEdge(g, idOf(i, j), idOfDst(), inf);
                }
                for (int[] d : GridUtils.DIRS) {
                    int ni = d[0] + i;
                    int nj = d[1] + j;
                    if (ni < 0 || ni >= n || nj < 0 || nj >= n) {
                        continue;
                    }
                    IntegerFlow.addFlowEdge(g, idOf(i, j), idOf(ni, nj), 1);
                }
            }
        }

//        debug.debug("g", IntegerFlow.flowToString(g));
        IntegerDinic dinic = new IntegerDinic();
        int minCut = dinic.apply(g, idOfSrc(), idOfDst(), inf);
        int total = (n - 1) * n * 2;
        int ans = total - minCut;
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
