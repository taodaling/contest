package template.graph;

import java.util.Arrays;

public class DepthOnTreeByParent implements DepthOnTree {
    ParentOnTree pot;
    int[] depth;

    public DepthOnTreeByParent(int n, ParentOnTree pot) {
        this.pot = pot;
        depth = new int[n];
        Arrays.fill(depth, -1);
        for (int i = 0; i < n; i++) {
            consider(i);
        }
    }

    private int consider(int root) {
        if (root == -1) {
            return -1;
        }
        if (depth[root] == -1) {
            depth[root] = consider(pot.parent(root)) + 1;
        }
        return depth[root];
    }

    @Override
    public int depth(int node) {
        return depth[node];
    }
}
