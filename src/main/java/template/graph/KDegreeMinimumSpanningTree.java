package template.graph;

import template.datastructure.DSU;
import template.primitve.generated.graph.LongWeightUndirectedEdge;
import template.utils.SortUtils;

import java.util.List;

/**
 * 给定一个连通图，指定一个顶点s,要求它的度数必须为k，找满足条件的最小生成树
 */
public class KDegreeMinimumSpanningTree {
    List<LongWeightUndirectedEdge>[] tree;
    LongWeightUndirectedEdge[] toNode;
    int s;
    int n;

    /**
     * O(8E+V)
     */
    public KDegreeMinimumSpanningTree(List<LongWeightUndirectedEdge>[] g, int s) {
        n = g.length;
        this.s = s;
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < n; i++) {
            for (LongWeightUndirectedEdge e : g[i]) {
                dsu.merge(e.to, e.rev.to);
            }
        }
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) != dsu.find(0)) {
                return;
            }
        }
        containSpanningTree = true;
        toNode = new LongWeightUndirectedEdge[n];
        for (LongWeightUndirectedEdge e : g[s]) {
            if (e.to != s && (toNode[e.to] == null || toNode[e.to].weight > e.weight)) {
                toNode[e.to] = e;
            }
        }
        int estimate = 0;
        for (int i = 0; i < n; i++) {
            if (i == s) {
                continue;
            }
            for (LongWeightUndirectedEdge e : g[i]) {
                if (e.to > i && e.to != s) {
                    estimate++;
                }
            }
        }
        LongWeightUndirectedEdge[] all = new LongWeightUndirectedEdge[estimate];
        int allTail = 0;
        for (int i = 0; i < n; i++) {
            if (i == s) {
                continue;
            }
            for (LongWeightUndirectedEdge e : g[i]) {
                if (e.to > i && e.to != s) {
                    all[allTail++] = e;
                }
            }
        }
        assert allTail == all.length;
        long sign = 1L << 63;
//        Arrays.sort(all, Comparator.comparingLong(x -> x.weight));
        SortUtils.radixSortLongObject(all, 0, allTail - 1, x -> x.weight ^ sign);
        dsu.init();
        tree = Graph.createGraph(n);
        for (LongWeightUndirectedEdge e : all) {
            if (dsu.find(e.to) != dsu.find(e.rev.to)) {
                dsu.merge(e.to, e.rev.to);
                addEdge(e);
            }
        }
        for (int i = 0; i < n; i++) {
            if (i == s || dsu.find(i) != i) {
                continue;
            }
            LongWeightUndirectedEdge e = dfsForMinWeight(i, -1);
            assert e != null;
            addEdge(e);
        }
        calcedNext = true;
        largestEdgeOnPath = new LongWeightUndirectedEdge[n];
    }

    LongWeightUndirectedEdge dfsForMinWeight(int root, int p) {
        LongWeightUndirectedEdge best = toNode[root];
        for (LongWeightUndirectedEdge e : tree[root]) {
            if (e.to == p) {
                continue;
            }
            LongWeightUndirectedEdge cand = dfsForMinWeight(e.to, root);
            if (best == null || cand != null && cand.weight < best.weight) {
                best = cand;
            }
        }
        return best;
    }

    void addEdge(LongWeightUndirectedEdge e) {
        totalWeight += e.weight;
        tree[e.rev.to].add(e);
        tree[e.to].add(e.rev);
    }

    void removeEdge(LongWeightUndirectedEdge e) {
        totalWeight -= e.weight;
        boolean d1 = tree[e.rev.to].remove(e);
        boolean d2 = tree[e.to].remove(e.rev);
        assert d1 && d2;
    }

    boolean containSpanningTree;
    boolean calcedNext;

    public boolean getContainSpanningTree() {
        return containSpanningTree;
    }

    long totalWeight;

    public long getTotalWeight() {
        return totalWeight;
    }

    public List<LongWeightUndirectedEdge>[] getTree() {
        return tree;
    }

    LongWeightUndirectedEdge[] largestEdgeOnPath;
    private void dfsForLargestEdgeOnPath(int root, int p, LongWeightUndirectedEdge curBest){
        largestEdgeOnPath[root] = curBest;
        for(LongWeightUndirectedEdge e : tree[root]){
            if(e.to == p){
                continue;
            }
            LongWeightUndirectedEdge best = curBest;
            if(best == null || best.weight < e.weight){
                best = e;
            }
            dfsForLargestEdgeOnPath(e.to, root, best);
        }
    }

    private void calcNext() {
        if (calcedNext) {
            return;
        }
        calcedNext = true;
        if (tree == null) {
            return;
        }
        for(LongWeightUndirectedEdge e : tree[s]){
            dfsForLargestEdgeOnPath(e.to, s, null);
        }
        int bestIndex = -1;
        long bestOffer = Long.MAX_VALUE;
        for(int i = 0; i < tree.length; i++){
            if(largestEdgeOnPath[i] == null || toNode[i] == null){
                continue;
            }
            long offer = toNode[i].weight - largestEdgeOnPath[i].weight;
            if(offer < bestOffer){
                bestOffer = offer;
                bestIndex = i;
            }
        }
        if(bestIndex == -1){
            tree = null;
            return;
        }
        removeEdge(largestEdgeOnPath[bestIndex]);
        addEdge(toNode[bestIndex]);
    }

    /**
     * O(V) each round
     */
    public boolean next() {
        calcNext();
        calcedNext = false;
        return tree != null;
    }
}
