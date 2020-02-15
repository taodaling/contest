package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerList;
import template.primitve.generated.LongObjectHashMap;
import template.utils.Buffer;

public class TaskC {
    XorDeltaDSU dsu;
    int total;
    int k;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        int[] status = new int[n];
        for (int i = 0; i < n; i++) {
            status[i] = in.readChar() - '0';
        }
        IntegerList[] appearance = new IntegerList[n];
        for (int i = 0; i < n; i++) {
            appearance[i] = new IntegerList(2);
        }

        for (int i = 0; i < k; i++) {
            int c = in.readInt();
            for (int j = 0; j < c; j++) {
                int x = in.readInt() - 1;
                appearance[x].add(i);
            }
        }

        dsu = new XorDeltaDSU(k + 1);
        for (int i = 0; i < k; i++) {
            dsu.req[0][i] = 1;
        }
        dsu.fixed[k] = k;

        for (int i = 0; i < n; i++) {
            if (status[i] == 0) {
                if (appearance[i].size() == 1) {
                    asTrue(appearance[i].first());
                } else {
                    merge(appearance[i].first(), appearance[i].tail(), 1);
                }
            } else {
                if (appearance[i].size() == 0) {

                } else if (appearance[i].size() == 1) {
                    asFalse(appearance[i].first());
                } else {
                    merge(appearance[i].first(), appearance[i].tail(), 0);
                }
            }
            out.println(total);
        }
    }

    public void merge(int i, int j, int d) {
        if (dsu.find(i) == dsu.find(j)) {
            return;
        }
        total -= dsu.getCost(i);
        total -= dsu.getCost(j);
        dsu.merge(i, j, d);
        total += dsu.getCost(i);
    }

    public void asFalse(int i) {
        merge(i, k, 1);
    }

    public void asTrue(int i) {
        merge(i, k, 0);
    }

}


class XorDeltaDSU {
    int[] p;
    int[] rank;
    int[] delta;
    int[] fixed;
    int[][] req;

    public XorDeltaDSU(int n) {
        p = new int[n];
        rank = new int[n];
        delta = new int[n];
        fixed = new int[n];
        req = new int[2][n];
        reset();
    }

    public void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            delta[i] = 0;
            fixed[i] = -1;
            req[0][i] = req[1][i] = 0;
        }
    }

    public int getCost(int i) {
        i = find(i);
        if (fixed[i] == -1) {
            return Math.min(req[0][i], req[1][i]);
        }
        int d = delta(fixed[i], i);
        return req[d][i];
    }

    public void setFixed(int i) {
        fixed[find(i)] = i;
    }

    public int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        find(p[a]);
        delta[a] ^= delta[p[a]];
        return p[a] = find(p[a]);
    }

    /**
     * return a - b, you should ensure a and b belong to same set
     */
    public int delta(int a, int b) {
        find(a);
        find(b);
        return delta[a] ^ delta[b];
    }

    /**
     * a - b = delta
     */
    public void merge(int a, int b, int d) {
        find(a);
        find(b);
        d = d ^ delta[a] ^ delta[b];
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
            delta[b] = d;
            if (fixed[a] == -1) {
                fixed[a] = fixed[b];
            }
            for (int i = 0; i < 2; i++) {
                req[i ^ d][a] += req[i][b];
            }
        } else {
            p[a] = b;
            delta[a] = d;
            if (fixed[b] == -1) {
                fixed[b] = fixed[a];
            }
            for (int i = 0; i < 2; i++) {
                req[i ^ d][b] += req[i][a];
            }
        }
    }
}
