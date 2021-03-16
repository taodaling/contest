package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.Arrays;

public class TwoSatBeta extends TwoSat {
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

    public boolean[] solve() {
        Arrays.fill(values, false);
        Arrays.fill(dfns, 0);
        deque.clear();
        dfn = 0;

        for (int i = 0; i < sets.length; i++) {
            tarjan(i);
        }
        for (int i = 0; i < n; i++) {
            if (sets[elementId(i)] == sets[negateElementId(i)]) {
                return null;
            }
        }

        Arrays.fill(dfns, 0);
        for (int i = 0; i < sets.length; i++) {
            assign(i);
            values[i] = values[sets[i]];
        }
        return values;
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

    protected void addRely(int a, int b) {
        edges.addLast(a, b);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            ans.append(values[elementId(i)]).append(',');
        }
        if (ans.length() > 0) {
            ans.setLength(ans.length() - 1);
        }
        ans.append('\n');
        IntegerArrayList list = new IntegerArrayList();
        for (int i = 0; i < n; i++) {
            list.clear();
            list.addAll(edges.iterator(elementId(i)));
            ans.append(i).append(": ");
            for (int x : list.toArray()) {
                int v = x / 2;
                if (x % 2 == 1) {
                    ans.append("~");
                }
                ans.append(v).append(",");
            }
            if (ans.charAt(ans.length() - 1) == ',') {
                ans.setLength(ans.length() - 1);
            }
            ans.append("\n");
            list.clear();
            list.addAll(edges.iterator(negateElementId(i)));
            ans.append("~").append(i).append(": ");
            for (int x : list.toArray()) {
                int v = x / 2;
                if (x % 2 == 1) {
                    ans.append("~");
                }
                ans.append(v).append(",");
            }
            if (ans.charAt(ans.length() - 1) == ',') {
                ans.setLength(ans.length() - 1);
            }
            ans.append("\n");
        }
        return ans.toString();
    }
}
