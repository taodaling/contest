package on2019_12.on2019_12_05_Educational_Codeforces_Round_61__Rated_for_Div__2_.G___Greedy_Subsequences;



import template.datastructure.*;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        MultiWayIntStack stack = new MultiWayIntStack(n, n);
        IntDeque deque = new IntDequeImpl(n);
        for (int i = n - 1; i >= 0; i--) {
            while (!deque.isEmpty() && a[deque.peekFirst()] <= a[i]) {
                deque.removeFirst();
            }
            if (deque.isEmpty()) {
            } else {
                stack.addLast(deque.peekFirst(), i);
            }
            deque.addFirst(i);
        }
        DSU[] dsus = new DSU[n];
        for (int i = 0; i < n; i++) {
            dsus[i] = new DSU();
            dsus[i].belong = i;
        }
        IntLeftistTree[] trees = new IntLeftistTree[n];
        for (int i = 0; i < n; i++) {
            trees[i] = new IntLeftistTree(1, i);
        }
        TreeSet<IntLeftistTree> set = new TreeSet<>((x, y) ->
                x.key == y.key ? x.index - y.index : x.key - y.key);
        for (int i = 0; i < n; i++) {
            IntLeftistTree tree = IntLeftistTree.NIL;
            for (IntIterator iterator = stack.iterator(i); iterator.hasNext(); ) {
                int next = iterator.next();
                set.remove(trees[next]);
                tree = IntLeftistTree.merge(tree, trees[next]);
                DSU.merge(dsus[i], dsus[next]);
            }
            tree.modify(1);
            tree = IntLeftistTree.merge(tree, trees[i]);
            set.add(tree);
            trees[i] = tree;
            dsus[i].find().belong = i;

            if (i < k - 1) {
                continue;
            }

            while (set.last().index <= i - k) {
                IntLeftistTree last = set.pollLast();
                last = IntLeftistTree.pop(last);
                trees[dsus[last.index].find().belong] = last;
                if (!last.isEmpty()) {
                    set.add(last);
                }
            }

            out.println(set.last().peek());
        }
    }
}

class DSU {
    DSU p = this;
    int rank;
    int belong;

    public DSU find() {
        return p.p == p ? p : (p = p.find());
    }

    public static void merge(DSU a, DSU b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }
}

class IntLeftistTree {
    public static final IntLeftistTree NIL = new IntLeftistTree(0, 0);

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.dist = -1;
    }

    IntLeftistTree left = NIL;
    IntLeftistTree right = NIL;
    int dist;
    int key;
    int dirty;
    int index;

    public void modify(int x) {
        if (this == NIL) {
            return;
        }
        key += x;
        dirty += x;
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public IntLeftistTree(int key, int index) {
        this.key = key;
        this.index = index;
    }

    public static IntLeftistTree merge(IntLeftistTree a, IntLeftistTree b) {
        if (a == NIL) {
            return b;
        } else if (b == NIL) {
            return a;
        }

        if (a.key < b.key) {
            IntLeftistTree tmp = a;
            a = b;
            b = tmp;
        }
        a.pushDown();
        a.right = merge(a.right, b);
        if (a.left.dist < a.right.dist) {
            IntLeftistTree tmp = a.left;
            a.left = a.right;
            a.right = tmp;
        }
        a.dist = a.right.dist + 1;
        return a;
    }

    public boolean isEmpty() {
        return this == NIL;
    }

    public int peek() {
        return key;
    }

    public static IntLeftistTree pop(IntLeftistTree root) {
        root.pushDown();
        IntLeftistTree ans = merge(root.left, root.right);
        root.left = root.right = NIL;
        return ans;
    }

    private void toStringDfs(StringBuilder builder) {
        if (this == NIL) {
            return;
        }
        builder.append(key).append(' ');
        left.toStringDfs(builder);
        right.toStringDfs(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        toStringDfs(builder);
        return builder.toString();
    }
}