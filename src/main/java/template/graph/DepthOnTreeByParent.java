package template.graph;

import java.util.Arrays;

public class DepthOnTreeByParent implements DepthOnTree {
    int[] p;
    int[] depth;

    public DepthOnTreeByParent(int[] p) {
        this.p = p;
        depth = new int[p.length];
        Arrays.fill(depth, -1);
    }

    @Override
    public int depth(int node) {
        if (depth[node] == -1) {
            if (p[node] == -1) {
                return depth[node] = 0;
            }
            depth[node] = depth(p[node]) + 1;
        }
        return depth[node];
    }
}
