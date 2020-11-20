package template.graph;

import template.binary.Log2;
import template.utils.SequenceUtils;

import java.util.List;

public class KthAncestorOnTreeByLongLink {
    int[] depths;
    int[] longest;
    int[] longestChild;
    int[] top;
    int[] up;
    int[] down;
    int[] mem;
    int memIndicator;
    int[][] jump;
    List<? extends DirectedEdge>[] g;

    private int alloc(int n) {
        int ans = memIndicator;
        memIndicator += n;
        if(memIndicator > mem.length){
            throw new ArrayIndexOutOfBoundsException();
        }
        return ans;
    }

    public KthAncestorOnTreeByLongLink(List<? extends DirectedEdge>[] g, int root) {
        this.g = g;
        int n = g.length;
        depths = new int[n];
        longest = new int[n];
        top = new int[n];
        up = new int[n];
        down = new int[n];
        mem = new int[n * 2];
        longestChild = new int[n];
        jump = new int[Log2.floorLog(n) + 2][n];
        SequenceUtils.deepFill(jump, -1);
        dfs(root, -1);
        dfsForUpAndDown(root, -1, false);
    }

    public int kthAncestor(int root, int k) {
        if (k == 0) {
            return root;
        }
        int targetDepth = depths[root] - k;
        int log = Log2.floorLog(k);
        root = top[jump[log][root]];
        if (targetDepth <= depths[root]) {
            return mem[up[root] + depths[root] - targetDepth];
        } else {
            return mem[down[root] + targetDepth - depths[root]];
        }
    }

    public int getDepth(int root) {
        return depths[root];
    }

    private void dfs(int root, int p) {
        depths[root] = p == -1 ? 0 : (depths[p] + 1);
        jump[0][root] = p;
        for (int i = 0; jump[i][root] != -1; i++) {
            jump[i + 1][root] = jump[i][jump[i][root]];
        }
        longestChild[root] = -1;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
            if (longest[root] < longest[e.to] + 1) {
                longest[root] = longest[e.to] + 1;
                longestChild[root] = e.to;
            }
        }
    }

    private void upRecord(int root, int i, int until) {
        if (root == -1 || i == until) {
            return;
        }
        mem[i] = root;
        upRecord(jump[0][root], i + 1, until);
    }

    private void downRecord(int root, int i, int until) {
        if (root == -1 || i == until) {
            return;
        }
        mem[i] = root;
        downRecord(longestChild[root], i + 1, until);
    }

    private void dfsForUpAndDown(int root, int p, boolean connect) {
        if (connect) {
            top[root] = top[p];
        } else {
            top[root] = root;
            int len = longest[root];
            up[root] = alloc(len + 1);
            down[root] = alloc(len + 1);
            upRecord(root, up[root], up[root] + len + 1);
            downRecord(root, down[root], down[root] + len + 1);
        }

        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForUpAndDown(e.to, root, e.to == longestChild[root]);
        }
    }
}
