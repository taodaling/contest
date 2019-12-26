package template.graph;

import java.util.function.IntBinaryOperator;

public class TreePath {
    private int[] vertex = new int[2];
    private int dist;

    public int getA() {
        return vertex[0];
    }

    public int getB() {
        return vertex[1];
    }

    public int getDist() {
        return dist;
    }

    public void initAsSingleVertex(int x) {
        vertex[0] = vertex[1] = x;
        dist = 0;
    }

    public void copyOf(TreePath x) {
        vertex[0] = x.vertex[0];
        vertex[1] = x.vertex[1];
        dist = x.dist;
    }

    /**
     * check whether node v included in the path or not
     */
    public boolean include(int v, LcaOnTree lca) {
        int lca1 = lca.lca(vertex[0], vertex[1]);
        int lca2 = lca.lca(vertex[0], v);
        int lca3 = lca.lca(vertex[1], v);
        return lca1 == v || ((lca2 == v) != (lca3 == v));
    }

    /**
     * check whether path has common vertex with current path or not
     */
    public boolean intersect(TreePath path, LcaOnTree lca) {
        int lca1 = lca.lca(vertex[0], vertex[1]);
        int lca2 = lca.lca(path.vertex[0], path.vertex[1]);
        return (lca.lca(lca1, path.vertex[0]) == lca1 || lca.lca(lca1, path.vertex[1]) == lca1) &&
                (lca.lca(lca2, vertex[0]) == lca2 || lca.lca(lca2, vertex[1]) == lca2);
    }

    public void mergeAsLongestPath(TreePath a, TreePath b, IntBinaryOperator distFunc) {
        copyOf(a);
        mergeAsLongestPath(b, distFunc);
    }

    public void mergeAsLongestPath(TreePath x, IntBinaryOperator distFunc) {
        int a = vertex[0];
        int b = vertex[1];
        int dist = this.dist;

        if (x.dist > dist) {
            a = x.vertex[0];
            b = x.vertex[1];
            dist = x.dist;
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int d = distFunc.applyAsInt(vertex[i], x.vertex[j]);
                if (d > dist) {
                    a = vertex[i];
                    b = x.vertex[j];
                    dist = d;
                }
            }
        }

        this.vertex[0] = a;
        this.vertex[1] = b;
        this.dist = dist;
    }

    @Override
    public String toString() {
        return String.format("%d...%d(%d)", vertex[0], vertex[1], dist);
    }
}
