package contest;

import template.algo.OperationQueue;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.io.PrintWriter;
import java.util.Arrays;

public class HVirus {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();
        int k = in.readInt();
        IntegerDeque dq = new IntegerDequeImpl(q);
        DSU dsu = new DSU(n);
        OperationQueue queue = new OperationQueue(q);
        int day = 0;
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int x = in.readInt() - 1;
                int y = in.readInt() - 1;
                dq.addLast(day);
                dsu.merge(x, y, queue);
            } else if (t == 2) {
                int x = in.readInt() - 1;
                out.println(dsu.size(x));
            } else {
                day++;
                while (!dq.isEmpty() && day - dq.peekFirst() >= k) {
                    dq.removeFirst();
                    queue.remove();
                }
            }
        }
    }
}

class DSU {
    int[] rank;
    int[] p;

    public DSU(int n) {
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

    public void merge(int a, int b, OperationQueue queue) {
        queue.add(new OperationQueue.CommutativeOperation() {
            int x, y;

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
                p[y] = x;
                rank[x] += rank[y];
            }

            @Override
            public void undo() {
                int cur = y;
                while (p[cur] != -1) {
                    cur = p[cur];
                    rank[cur] -= rank[y];
                }
                p[y] = -1;
            }
        });
    }
}