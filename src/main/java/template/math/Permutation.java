package template.math;

import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * permutation start from 0
 * The permutation item is index, p(i) means which the value of p_i (which value the i-th slot contain)
 */
public class Permutation {
    int[] g;
    int[] idx;
    int[] l;
    int[] r;
    int n;

    public List<IntegerArrayList> extractCircles() {
        return extractCircles(1);
    }

    public List<IntegerArrayList> extractCircles(int threshold) {
        List<IntegerArrayList> ans = new ArrayList<>(n);
        for (int i = 0; i < n; i = r[i] + 1) {
            int size = r[i] - l[i] + 1;
            if (size < threshold) {
                continue;
            }
            IntegerArrayList list = new IntegerArrayList(r[i] - l[i] + 1);
            for (int j = l[i]; j <= r[i]; j++) {
                list.add(g[j]);
            }
            ans.add(list);
        }
        return ans;
    }

    public int countCircles() {
        int ans = 0;
        for (int i = 0; i < n; i = r[i] + 1) {
            ans++;
        }
        return ans;
    }

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
            p[i] = b.apply(a.apply(i, ap), bp);
        }
        return new Permutation(p, n);
    }

    /**
     * return this^p(x)
     */
    public int apply(int x, int p) {
        int i = idx[x];
        int dist = DigitUtils.mod((i - l[i]) + p, r[i] - l[i] + 1);
        return g[dist + l[i]];
    }

    /**
     * return this^p(x)
     */
    public int apply(int x, long p) {
        int i = idx[x];
        int dist = DigitUtils.mod((i - l[i]) + p, r[i] - l[i] + 1);
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