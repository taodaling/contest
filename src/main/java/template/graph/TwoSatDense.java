package template.graph;

import template.datastructure.BitSet;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;

public class TwoSatDense extends TwoSat {
    private boolean[][] adj;
    private int[] set;
    private int[] dfn;
    private int[] low;
    private boolean[] instk;
    private int[] stk;
    private boolean[] value;
    private int tail;
    private int modVersion;
    private int solveVersion = -1;
    private int testVersion = -1;
    private boolean possible;
    private BitSet[] floyd;
    BitSet tmp;

    public BitSet[] getRelyClosure() {
        prepareTest();
        return floyd;
    }

    private void prepareTest() {
        if (testVersion != modVersion) {
            testVersion = modVersion;
            for (int i = 0; i < floyd.length; i++) {
                for (int j = 0; j < floyd.length; j++) {
                    if (adj[i][j]) {
                        floyd[i].set(j);
                    }
                }
            }
            for (int k = 0; k < floyd.length; k++) {
                for (int i = 0; i < floyd.length; i++) {
                    if (floyd[i].get(k)) {
                        floyd[i].or(floyd[k]);
                    }
                }
            }
        }
    }

    /**
     * 判断将所有variables同时设为true，是否可能有解
     *
     * @param variables
     * @return
     */
    public boolean test(IntegerArrayList variables) {
        prepareTest();

        tmp.fill(false);
        for (int i = 0; i < variables.size(); i++) {
            int v = variables.get(i);
            tmp.or(floyd[v]);
        }
        for (int i = 0; i < variables.size(); i++) {
            if (tmp.get(negate(variables.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public boolean mustTrue(int i) {
        prepareTest();
        return floyd[negate(i)].get(i);
    }

    public boolean mustFalse(int i) {
        prepareTest();
        return floyd[i].get(negate(i));
    }

    public boolean onlyOneChoice(int i){
        prepareTest();
        return floyd[i].get(negate(i)) || floyd[negate(i)].get(i);
    }

    void push(int x) {
        stk[tail++] = x;
    }

    int pop() {
        return stk[--tail];
    }

    public TwoSatDense(int n) {
        adj = new boolean[n * 2][n * 2];
        set = new int[n * 2];
        dfn = new int[n * 2];
        low = new int[n * 2];
        stk = new int[n * 2];
        instk = new boolean[n * 2];
        value = new boolean[n * 2];
        floyd = new BitSet[n * 2];
        for (int i = 0; i < n * 2; i++) {
            floyd[i] = new BitSet(n * 2);
            floyd[i].set(i);
        }
        tmp = new BitSet(2 * n);
    }

    @Override
    protected void addRely(int a, int b) {
        adj[a][b] = true;
        modVersion++;
    }

    int order = 0;

    public boolean[] solve() {
        if (solveVersion == modVersion) {
            return possible ? value : null;
        }
        solveVersion = modVersion;
        order = 0;
        tail = 0;
        Arrays.fill(dfn, -1);
        Arrays.fill(instk, false);
        for (int i = 0; i < adj.length; i++) {
            tarjan(i);
        }
        possible = true;
        for (int i = 0; i < adj.length; i++) {
            if (set[i] == set[negate(i)]) {
                possible = false;
                return null;
            }
        }
        Arrays.fill(value, false);
        Arrays.fill(instk, false);
        for (int i = 0; i < adj.length; i++) {
            assign(i);
            value[i] = value[set[i]];
        }
        return value;
    }

    void assign(int root) {
        if (instk[root]) {
            return;
        }
        instk[root] = true;
        for (int i = 0; i < adj.length; i++) {
            if (!adj[root][i]) {
                continue;
            }
            assign(i);
        }
        if (set[root] == root) {
            assert instk[set[negate(root)]] == value[set[negate(root)]];
            value[root] = !value[set[negate(root)]];
        }
    }

    void tarjan(int root) {
        if (dfn[root] != -1) {
            return;
        }
        dfn[root] = low[root] = ++order;
        instk[root] = true;
        push(root);
        for (int i = 0; i < adj.length; i++) {
            if (!adj[root][i]) {
                continue;
            }
            tarjan(i);
            if (instk[i]) {
                low[root] = Math.min(low[root], low[i]);
            }
        }
        if (dfn[root] == low[root]) {
            while (true) {
                int node = pop();
                set[node] = root;
                instk[node] = false;
                if (node == root) {
                    break;
                }
            }
        }
    }
}
