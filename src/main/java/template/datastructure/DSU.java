package template.datastructure;

public class DSU {
    protected int[] p;
    protected int[] rank;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
    }

    public void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        preAccess(a);
        return p[a] = find(p[a]);
    }

    /**
     * before setting p[a] = p[p[a]]
     */
    protected void preAccess(int a) {

    }


    /**
     * before setting p[b] = a
     */
    protected void preMerge(int a, int b) {

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

        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }

        preMerge(a, b);
        p[b] = a;
    }
}
