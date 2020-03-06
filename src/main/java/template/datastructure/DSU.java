package template.datastructure;

public class DSU {
    protected int[] p;
    protected int[] rank;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        int ans = find(p[a]);
        refreshParent(a, p[a], ans);
        return ans;
    }

    protected void refreshParent(int a, int old, int refresh) {
        p[a] = refresh;
    }

    /**
     * link a into subtree of b
     */
    protected void link(int a, int b) {
        p[a] = b;
    }

    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        if (rank[a] > rank[b]) {
            link(b, a);
        } else {
            link(a, b);
        }
    }
}
