package template.datastructure;

public class DeltaDSU {
    int[] p;
    int[] rank;
    long[] delta;

    public DeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new long[n];
        reset();
    }

    public void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            delta[i] = 0;
        }
    }

    public int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        delta[a] += delta[p[a]];
        return p[a] = find(p[a]);
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public long delta(int a, int b) {
        if(find(a) != find(b)){
            throw new IllegalArgumentException();
        }
        return delta[a] - delta[b];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, long d) {
        find(a);
        find(b);
        d = d - delta[a] + delta[b];
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] < rank[b]) {
            int tmp = a;
            a = b;
            b = tmp;
            d = -d;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        p[b] = a;
        delta[b] = -d;
    }
}
