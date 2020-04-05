package template.graph;

import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.Arrays;

public class TwoSatBeta {
    private IntegerMultiWayStack edges;
    private boolean[] values;
    private int[] sets;
    private int[] dfns;
    private int[] lows;
    private boolean[] instk;
    private IntegerDequeImpl deque;
    private int n;

    public TwoSatBeta(int n, int m) {
        values = new boolean[n * 2];
        sets = new int[n * 2];
        edges = new IntegerMultiWayStack(n * 2, m);
        dfns = new int[n * 2];
        lows = new int[n * 2];
        instk = new boolean[n * 2];
        deque = new IntegerDequeImpl(n * 2);
        this.n = n;
    }

    public void clear() {
        edges.clear();
    }

    public boolean valueOf(int x) {
        return values[sets[elementId(x)]];
    }

    public boolean solve(boolean fetchValue) {
        Arrays.fill(values, false);
        Arrays.fill(dfns, 0);
        deque.clear();
        dfn = 0;

        for (int i = 0; i < sets.length; i++) {
            tarjan(i);
        }
        for (int i = 0; i < n; i++) {
            if (sets[elementId(i)] == sets[negateElementId(i)]) {
                return false;
            }
        }

        if (!fetchValue) {
            return true;
        }

        Arrays.fill(dfns, 0);
        for (int i = 0; i < sets.length; i++) {
            assign(i);
        }
        return true;
    }

    private void assign(int root) {
        if (dfns[root] > 0) {
            return;
        }
        dfns[root] = 1;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            assign(node);
        }
        if (sets[root] == root) {
            values[root] = !values[sets[negate(root)]];
        }
    }

    private int dfn = 0;

    private void tarjan(int root) {
        if (dfns[root] > 0) {
            return;
        }
        lows[root] = dfns[root] = ++dfn;
        instk[root] = true;
        deque.addLast(root);
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            tarjan(node);
            if (instk[node] && lows[node] < lows[root]) {
                lows[root] = lows[node];
            }
        }
        if (lows[root] == dfns[root]) {
            int last;
            do {
                last = deque.removeLast();
                sets[last] = root;
                instk[last] = false;
            } while (last != root);
        }
    }

    public int elementId(int x) {
        return x << 1;
    }

    public int negateElementId(int x) {
        return (x << 1) | 1;
    }

    private int negate(int x) {
        return x ^ 1;
    }

    public void deduce(int a, int b) {
        edges.addLast(a, b);
        edges.addLast(negate(b), negate(a));
    }

    public void or(int a, int b) {
        deduce(negate(a), b);
    }

    public void isTrue(int a) {
        edges.addLast(negate(a), a);
    }

    public void isFalse(int a) {
        edges.addLast(a, negate(a));
    }

    public void same(int a, int b) {
        deduce(a, b);
        deduce(b, a);
    }

    public void xor(int a, int b) {
        same(a, negate(b));
    }

    public void atLeastOneIsFalse(int a, int b) {
        deduce(a, negate(b));
    }

    public void atLeastOneIsTrue(int a, int b) {
        or(a, b);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            ans.append(valueOf(i)).append(',');
        }
        if (ans.length() > 0) {
            ans.setLength(ans.length() - 1);
        }
        ans.append('\n');
        ans.append(edges);
        return ans.toString();
    }
}
