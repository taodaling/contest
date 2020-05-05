package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FRoyalQuestions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int[][] princess = new int[m][3];
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                princess[i][j] = in.readInt();
            }
            princess[i][0]--;
            princess[i][1]--;
        }

        Arrays.sort(princess, (a, b) -> -Integer.compare(a[2], b[2]));
        long ans = 0;
        for (int[] p : princess) {
            if (dsu.find(p[0]) != dsu.find(p[1])) {
                if (dsu.circle[dsu.find(p[0])] && dsu.circle[dsu.find(p[1])]) {
                    continue;
                }
                dsu.merge(p[0], p[1]);
                ans += p[2];
            } else {
                if (dsu.circle[dsu.find(p[0])]) {
                    continue;
                }
                dsu.circle[dsu.find(p[0])] = true;
                ans += p[2];
            }
        }

        out.println(ans);
    }
}

class DSU {
    protected int[] p;
    protected int[] rank;
    boolean[] circle;

    public DSU(int n) {
        p = new int[n];
        rank = new int[n];
        circle = new boolean[n];
        reset();
    }

    public final void reset() {
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
            rank[i] = 0;
            circle[i] = false;
        }
    }

    public final int find(int a) {
        if (p[a] == p[p[a]]) {
            return p[a];
        }
        return p[a] = find(p[a]);
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

        p[b] = a;
        circle[a] = circle[a] || circle[b];
    }
}