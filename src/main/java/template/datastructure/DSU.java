package template.datastructure;

public class DSU {
    int[] p;
    int[] rank;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        reset();
    }

    public void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int a) {
        return p[a] == p[p[a]] ? p[a] : (p[a] = find(p[a]));
    }

    public void merge(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        if (rank[a] > rank[b]) {
            p[b] = a;
        } else {
            p[a] = b;
        }
    }
}
