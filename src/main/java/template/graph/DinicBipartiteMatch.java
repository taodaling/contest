package template.graph;

import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;

import java.util.Arrays;
import java.util.List;

public class DinicBipartiteMatch {
    private List<IntegerFlowEdge>[] g;
    private IntegerDinic dinic;
    private int[] lMates;
    private int[] rMates;
    int l;
    int r;

    private int idOfL(int i) {
        return i;
    }

    private int idOfR(int i) {
        return i + l;
    }

    private int idOfSrc() {
        return l + r;
    }

    private int idOfDst() {
        return idOfSrc() + 1;
    }

    public DinicBipartiteMatch(int l, int r) {
        this.l = l;
        this.r = r;
        lMates = new int[l];
        rMates = new int[r];
        g = Graph.createGraph(idOfDst() + 1);
        dinic = new IntegerDinic();
        for (int i = 0; i < l; i++) {
            IntegerFlow.addFlowEdge(g, idOfSrc(), idOfL(i), 1);
        }
        for (int i = 0; i < r; i++) {
            IntegerFlow.addFlowEdge(g, idOfR(i), idOfDst(), 1);
        }
    }

    public void addEdge(int u, int v) {
        IntegerFlow.addFlowEdge(g, idOfL(u), idOfR(v), 1);
    }

    public int solve() {
        Arrays.fill(lMates, -1);
        Arrays.fill(rMates, -1);
        dinic.apply(g, idOfSrc(), idOfDst(), l);
        int ans = 0;
        for (int i = 0; i < l; i++) {
            for (IntegerFlowEdge e : g[idOfL(i)]) {
                if (e.real && e.flow == 1) {
                    ans++;
                    int to = e.to - idOfR(0);
                    lMates[i] = to;
                    rMates[to] = i;
                    break;
                }
            }
        }
        return ans;
    }

    /**
     * Find the mate of left i or -1 means nobody
     */
    public int mateOfLeft(int i) {
        return lMates[i];
    }

    /**
     * Find the mate of right i or -1 means nobody
     */
    public int mateOfRight(int i) {
        return rMates[i];
    }


    public boolean[][] minVertexCover() {
        boolean[] lVis = new boolean[l];
        boolean[] rVis = new boolean[r];
        for (int i = 0; i < r; i++) {
            if (rMates[i] == -1) {
                dfsRight(i, lVis, rVis);
            }
        }

        boolean[] left = new boolean[l];
        boolean[] right = new boolean[r];
        for (int i = 0; i < l; i++) {
            left[i] = lVis[i];
        }
        for (int i = 0; i < r; i++) {
            right[i] = !rVis[i];
        }

        return new boolean[][]{left, right};
    }

    private void dfsRight(int i, boolean[] lVis, boolean[] rVis) {
        if (rVis[i]) {
            return;
        }
        rVis[i] = true;
        for (IntegerFlowEdge e : g[idOfR(i)]) {
            if (e.real) {
                continue;
            }
            dfsLeft(e.to - idOfL(0), lVis, rVis);
        }
    }

    private void dfsLeft(int i, boolean[] lVis, boolean[] rVis) {
        if (lMates[i] == -1) {
            return;
        }
        if (lVis[i]) {
            return;
        }
        lVis[i] = true;
        dfsRight(lMates[i], lVis, rVis);
    }
}
