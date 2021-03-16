package template.datastructure;

import java.util.Arrays;

public class DSU {
    protected int[] p;
    public int[] size;
    protected int n;

    public DSU(int n) {
        p = new int[n];
        size = new int[n];
    }

    public void init() {
        init(p.length);
    }

    public void init(int n) {
        this.n = n;
        for (int i = 0; i < n; i++) {
            p[i] = i;
            size[i] = 1;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        preAccess(a);
        return p[a] = p[p[a]];
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
        size[a] += size[b];
    }

    public final void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }

        if (size[a] < size[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        preMerge(a, b);
        p[b] = a;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(p, n));
    }
}
