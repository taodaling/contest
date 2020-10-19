package template.algo;

import java.util.Arrays;

public class RevokeDSU {
    int[] rank;
    int[] p;

    public RevokeDSU(int n) {
        rank = new int[n];
        p = new int[n];
        Arrays.fill(rank, 1);
        Arrays.fill(p, -1);
    }

    public int find(int x) {
        while (p[x] != -1) {
            x = p[x];
        }
        return x;
    }

    public int size(int x) {
        return rank[find(x)];
    }

    public OperationQueue.CommutativeOperation merge(int a, int b, OperationQueue queue) {
        return new OperationQueue.CommutativeOperation() {
            int x, y;


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
                p[y] = x;
                rank[x] += rank[y];
            }


            public void undo() {
                int cur = y;
                while (p[cur] != -1) {
                    cur = p[cur];
                    rank[cur] -= rank[y];
                }
                p[y] = -1;
            }
        };
    }
}
