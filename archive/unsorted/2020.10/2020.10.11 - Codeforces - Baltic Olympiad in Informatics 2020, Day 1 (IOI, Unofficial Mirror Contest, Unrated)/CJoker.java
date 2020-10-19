package contest;

import template.algo.OperationQueue;
import template.io.FastInput;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class CJoker {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();

        int[][] edges = new int[2][m + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j < 2; j++) {
                edges[j][i] = in.readInt() - 1;
            }
        }
        OperationQueue oq = new OperationQueue(m);
        XorDSU dsu = new XorDSU(n);
        int cur = 1;
        for (int i = 1; i <= m; i++) {
            int a = edges[0][i];
            int b = edges[1][i];
            while (dsu.find(a) == dsu.find(b) && dsu.delta(a, b) == 0) {
                oq.remove();
                cur++;
            }
            dsu.merge(a, b, oq);
        }

        int[] right = new int[m + 1];
        right[0] = cur;
        for (int i = 1; i <= m; i++) {
            int a = edges[0][i];
            int b = edges[1][i];
            while (cur <= i) {
                cur++;
                oq.remove();
            }
            while (cur <= m && dsu.find(a) == dsu.find(b) && dsu.delta(a, b) == 0) {
                oq.remove();
                cur++;
            }
            if (cur == m + 1 && dsu.find(a) == dsu.find(b) && dsu.delta(a, b) == 0) {
                cur++;
            }
            dsu.merge(a, b, oq);
            right[i] = cur;
        }

        for (int i = 0; i < q; i++) {
            int l = in.readInt();
            int r = in.readInt();
            if (right[l - 1] - 1 > r) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }

        debug.debug("right", right);
    }
}

class XorDSU {
    int[] p;
    int[] rank;
    int[] xor;

    public XorDSU(int n) {
        p = new int[n];
        rank = new int[n];
        xor = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = -1;
            rank[i] = 1;
        }
    }

    public int find(int x) {
        return p[x] == -1 ? x : find(p[x]);
    }

    public int delta(int a, int b) {
        return deltaToRoot(a) ^ deltaToRoot(b);
    }

    public int deltaToRoot(int a) {
        int ans = 0;
        while (p[a] != -1) {
            ans ^= xor[a];
            a = p[a];
        }
        return ans;
    }

    public void merge(int a, int b, OperationQueue queue) {
        queue.add(new OperationQueue.CommutativeOperation() {
            int x;
            int y;

            @Override
            public void apply() {
                x = find(a);
                y = find(b);
                if (x == y) {
                    return;
                }
                if (rank[x] < rank[y]) {
                    int tmp = x;
                    x = y;
                    y = tmp;
                }
                xor[y] = deltaToRoot(a) ^ deltaToRoot(b) ^ 1;
                p[y] = x;
                rank[x] += rank[y];
            }

            @Override
            public void undo() {
                if (x == y) {
                    return;
                }
                int cur = y;
                while (p[cur] != -1) {
                    cur = p[cur];
                    rank[cur] -= rank[y];
                }
                p[y] = -1;
                xor[y] = 0;
            }
        });
    }
}