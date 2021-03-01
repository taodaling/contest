package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerWeightDirectedEdge;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.primitve.generated.graph.IntegerWeightUndirectedEdge;

import java.util.List;

public class P2986USACO10MARGreatCowGatheringG {
    int[] C;
    List<IntegerWeightUndirectedEdge>[] g;
    long[] subtreeChildCnt;
    long[] subtreeChildSum;

    long[] wholeTreeDist;

    void dfs1(int root, int p) {
        subtreeChildCnt[root] = C[root];
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs1(e.to, root);
            subtreeChildCnt[root] += subtreeChildCnt[e.to];
            subtreeChildSum[root] += subtreeChildSum[e.to] + subtreeChildCnt[e.to] * e.weight;
        }
    }

    void dfs2(int root, int p, long total, long topSum) {
        wholeTreeDist[root] = topSum + subtreeChildSum[root];
        int m = g[root].size();
        long[] pre = new long[m];
        for (int i = 0; i < m; i++) {
            IntegerWeightUndirectedEdge e = g[root].get(i);
            if (e.to == p) {
                continue;
            }
            pre[i] = subtreeChildSum[e.to] + subtreeChildCnt[e.to] * e.weight;
        }
        long[] post = pre.clone();
        for (int i = 1; i < m; i++) {
            pre[i] += pre[i - 1];
        }
        for (int i = m - 2; i >= 0; i--) {
            post[i] += post[i + 1];
        }
        for (int i = 0; i < m; i++) {
            IntegerWeightUndirectedEdge e = g[root].get(i);
            if (e.to == p) {
                continue;
            }
            long cost = topSum;
            if (i > 0) {
                cost += pre[i - 1];
            }
            if (i + 1 < m) {
                cost += post[i + 1];
            }
            cost += (total - subtreeChildCnt[e.to]) * e.weight;
            dfs2(e.to, root, total, cost);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        g = Graph.createGraph(n);
        C = in.ri(n);
        subtreeChildCnt = new long[n];
        subtreeChildSum = new long[n];
        wholeTreeDist = new long[n];
        for (int i = 0; i < n - 1; i++) {
            IntegerWeightGraph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1, in.ri());
        }
        dfs1(0, -1);
        dfs2(0, -1, subtreeChildCnt[0], 0);
        long min = Long.MAX_VALUE;
        for(long x : wholeTreeDist){
            min = Math.min(min, x);
        }
        out.println(min);
    }
}
