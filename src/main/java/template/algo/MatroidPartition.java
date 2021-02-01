package template.algo;

import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.Debug;
import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.Arrays;

/**
 * O(r^2n) and O(r * maxPartition^2) invoke of computeAdj and extend
 */
public class MatroidPartition {
    public MatroidPartition(int n) {
        assert n >= 1;
        this.n = n;
        this.type = new int[n];
        this.sets = new boolean[n][n];
        this.extendables = new boolean[n][n];
        adj = new boolean[n + n][];
        dists = new int[n + n];
        prev = new int[n + n];
        for (int i = 0; i < n; i++) {
            adj[i] = new boolean[n];
            adj[i + n] = extendables[i];
        }
        dq = new IntegerDequeImpl(n);
    }

    int n;
    int[] type;
    boolean[][] sets;
    boolean[][] extendables;
    MatroidIndependentSet rule;
    int remain;
    boolean[][] adj;
    int size;
    int[] dists;
    int[] prev;
    static int inf = (int) 1e9;
    IntegerDequeImpl dq;
    int maxPartition;
    Callback callback = Callback.NIL;
//    Debug debug = new Debug(true);

    public void setCallback(Callback callback) {
        if (callback == null) {
            callback = Callback.NIL;
        }
        this.callback = callback;
    }

    public void occupy(int ele, int id) {
        if (type[ele] == -1) {
            remain--;
        } else {
            sets[type[ele]][ele] = false;
        }
        sets[id][ele] = true;
        type[ele] = id;
    }

    private boolean expand() {
        if (remain == 0) {
            return false;
        }
        //try find path
        for (int i = 0; i < n; i++) {
            if (type[i] == -1) {
                continue;
            }
            Arrays.fill(adj[i], false);
        }
        for (int i = 0; i < size; i++) {
            Arrays.fill(extendables[i], false);
            rule.prepare(sets[i]);
            rule.extend(sets[i], extendables[i]);
            rule.computeAdj(sets[i], adj);
        }
        Arrays.fill(dists, inf);
        Arrays.fill(prev, -1);
        dq.clear();
        for (int i = 0; i < size; i++) {
            dists[i + n] = 0;
            dq.addLast(i + n);
        }
        int tail = -1;
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            if (head < n && type[head] == -1) {
                tail = head;
                break;
            }
            for (int i = 0; i < n; i++) {
                if (adj[head][i] && dists[i] > 1 + dists[head]) {
                    dists[i] = 1 + dists[head];
                    prev[i] = head;
                    dq.addLast(i);
                }
            }
        }
        if (tail == -1) {
            if (size >= maxPartition || !newIndependentSet()) {
                return false;
            }
            return expand();
        }
        //replace
        while (tail < n) {
            int p = prev[tail];
            int id = p < n ? type[p] : p - n;
            occupy(tail, id);
            tail = p;
        }
        return true;
    }

    private boolean newIndependentSet() {
        if (!callback.newIndependentSet(sets, size)) {
            return false;
        }
        int id = size++;
        Arrays.fill(extendables[id], false);
        Arrays.fill(sets[id], false);
        boolean[] extendable = extendables[id];
        boolean[] newSet = sets[id];
        while (remain > 0) {
            Arrays.fill(extendable, false);
            rule.prepare(newSet);
            rule.extend(newSet, extendable);
            boolean find = false;
            for (int i = 0; i < n; i++) {
                if (type[i] == -1 && extendable[i]) {
                    occupy(i, id);
                    find = true;
                    break;
                }
            }
            if (!find) {
                break;
            }
        }
//        debug.debugArray("type", type);
        return true;
    }

    public Pair<int[], Integer> solve(MatroidIndependentSet set) {
        return solve(set, n);
    }

    public Pair<int[], Integer> solve(MatroidIndependentSet set, int maxPartition) {
        this.maxPartition = maxPartition;
        Arrays.fill(type, -1);
        size = 0;
        if (maxPartition == 0) {
            return new Pair<>(type, size);
        }
        this.rule = set;
        remain = n;
        SequenceUtils.deepFill(adj, false);
        if (!newIndependentSet()) {
            return new Pair<>(type, size);
        }
        while (expand()) {
//            debug.debugArray("type", type);
        }

        return new Pair<>(type, size);
    }



    public static interface Callback {
        static Callback NIL = (a, b) -> true;

        public boolean newIndependentSet(boolean[][] now, int size);
    }
}
