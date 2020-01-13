package template.datastructure;

public class DeltaDSU {
    int[] p;
    int[] rank;
    int[] delta;

    public DeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new int[n];
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
        if(p[a] == p[p[a]]){
            return p[a];
        }
        find(p[a]);
        delta[a] += delta[p[a]];
        return p[a] = find(p[a]);
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public int delta(int a, int b){
       find(a);
       find(b);
       return delta[a] - delta[b];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, int d) {
        find(a);
        find(b);
        d = d - delta[a] + delta[b];
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
            delta[b] = -d;
        } else {
            p[a] = b;
            delta[a] = d;
        }
    }
}
