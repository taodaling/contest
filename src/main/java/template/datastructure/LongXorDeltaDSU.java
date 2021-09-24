package template.datastructure;

public class LongXorDeltaDSU {
    int[] p;
    int[] size;
    long[] delta;
    boolean valid;

    public boolean valid(){
        return valid;
    }

    public LongXorDeltaDSU(int n) {
        p = new int[n];
        size = new int[n];
        delta = new long[n];
        init();
    }

    public void init() {
        init(p.length);
    }

    public void init(int n) {
        for (int i = 0; i < n; i++) {
            p[i] = i;
            size[i] = 0;
            delta[i] = 0;
        }
        valid = true;
    }

    public int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        delta[a] ^= delta[p[a]];
        return p[a] = p[p[a]];
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public long delta(int a, int b) {
        find(a);
        find(b);
        return delta[a] ^ delta[b];
    }

    public long deltaRoot(int a) {
        find(a);
        return delta[a];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, long d) {
        find(a);
        find(b);
        d = d ^ delta[a] ^ delta[b];
        a = find(a);
        b = find(b);
        if (a == b) {
            valid = valid && d == 0;
            return;
        }
        if (size[a] < size[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        size[a] += size[b];
        p[b] = a;
        delta[b] = d;
    }
}
