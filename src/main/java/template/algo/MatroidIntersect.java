package template.algo;

import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * O(r) invoke computeAdj and extend. O(r^2n)
 */
public class MatroidIntersect {
    protected IntegerDequeImpl dq;
    protected int[] dists;
    protected boolean[] added;
    protected boolean[][] adj1;
    protected boolean[][] adj2;
    protected int n;
    protected boolean[] x1;
    protected boolean[] x2;
    protected static int distInf = (int) 1e9;
    protected int[] pre;
    protected Consumer<boolean[]> callback;
    protected static Consumer<boolean[]> nilCallback = x -> {
    };

    public void setCallback(Consumer<boolean[]> callback) {
        if (callback == null) {
            callback = nilCallback;
        }
        this.callback = callback;
    }


    public MatroidIntersect(int n) {
        this.n = n;
        dq = new IntegerDequeImpl(n);
        dists = new int[n];
        added = new boolean[n];
        adj1 = new boolean[n][];
        adj2 = new boolean[n][];
        x1 = new boolean[n];
        x2 = new boolean[n];
        pre = new int[n];
        setCallback(nilCallback);
    }


    protected boolean adj(int i, int j) {
        if (added[i]) {
            return adj1[i][j];
        } else {
            return adj2[j][i];
        }
    }

    protected boolean expand(MatroidIndependentSet a, MatroidIndependentSet b, int round) {
        Arrays.fill(x1, false);
        Arrays.fill(x2, false);
        a.prepare(added);
        b.prepare(added);
        a.extend(added, x1);
        b.extend(added, x2);
        for (int i = 0; i < n; i++) {
            if (x1[i] && x2[i]) {
                pre[i] = -1;
                xorPath(i);
                return true;
            }
        }

        for (int i = 0; i < n; i++) {
            if (added[i]) {
                Arrays.fill(adj1[i], false);
                Arrays.fill(adj2[i], false);
            }
        }

        a.computeAdj(added, adj1);
        b.computeAdj(added, adj2);
        Arrays.fill(dists, distInf);
        Arrays.fill(pre, -1);
        dq.clear();
        for (int i = 0; i < n; i++) {
            if (added[i]) {
                continue;
            }
            //right
            if (x1[i]) {
                dists[i] = 0;
                dq.addLast(i);
            }
        }

        int tail = -1;
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            if (x2[head]) {
                tail = head;
                break;
            }
            for (int j = 0; j < n; j++) {
                if (added[head] != added[j] && adj(head, j) && dists[j] > dists[head] + 1) {
                    dists[j] = dists[head] + 1;
                    dq.addLast(j);
                    pre[j] = head;
                }
            }
        }

        if (tail == -1) {
            return false;
        }

        xorPath(tail);
        return true;
    }

    protected void xorPath(int tail) {
        boolean[] last1 = new boolean[n];
        boolean[] last2 = new boolean[n];
        for (boolean add = true; tail != -1; tail = pre[tail], add = !add) {
            assert added[tail] != add;
            added[tail] = add;
            if (add) {
                adj1[tail] = last1;
                adj2[tail] = last2;
            } else {
                last1 = adj1[tail];
                last2 = adj2[tail];
                adj1[tail] = null;
                adj2[tail] = null;
            }
        }
    }

    /**
     * Find a basis with largest possible size
     *
     * @param a
     * @param b
     * @return
     */
    public boolean[] intersect(MatroidIndependentSet a, MatroidIndependentSet b) {
        Arrays.fill(added, false);
        int round = 0;
        callback.accept(added);
        while (expand(a, b, round)) {
            round++;
            callback.accept(added);
        }
        return added;
    }

}
