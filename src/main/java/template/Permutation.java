package template;

public class Permutation {
    int[] g;
    int[] idx;
    int[] l;
    int[] r;
    int n;

    public Permutation(int[] p) {
        this(p, p.length);
    }

    public Permutation(int[] p, int len) {
        n = len;
        boolean[] visit = new boolean[n];
        g = new int[n];
        l = new int[n];
        r = new int[n];
        idx = new int[n];
        int wpos = 0;
        for (int i = 0; i < n; i++) {
            int val = p[i];
            if (visit[val]) {
                continue;
            }
            visit[val] = true;
            g[wpos] = val;
            l[wpos] = wpos;
            idx[val] = wpos;
            wpos++;
            while (true) {
                int x = p[g[wpos - 1]];
                if (visit[x]) {
                    break;
                }
                visit[x] = true;
                g[wpos] = x;
                l[wpos] = l[wpos - 1];
                idx[x] = wpos;
                wpos++;
            }
            for (int j = l[wpos - 1]; j < wpos; j++) {
                r[j] = wpos - 1;
            }
        }
    }

    /**
     * return a^ap * b^bp
     */
    public static Permutation mul(Permutation a, int ap, Permutation b, int bp) {
        int n = a.n;
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = a.apply(b.apply(i, bp), ap);
        }
        return new Permutation(p, n);
    }

    /**
     * return this^p(x)
     */
    public int apply(int x, int p) {
        int i = idx[x];
        int dist = (i - l[i]) + p;
        int len = r[i] - l[i] + 1;
        dist %= len;
        if (dist < 0) {
            dist += len;
        }
        return g[dist + l[i]];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(apply(i, 1)).append(' ');
        }
        return builder.toString();
    }
}