package contest;


import template.datastructure.BitSet;
import template.graph.DinicBipartiteMatch;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.Debug;

import java.util.*;
import java.util.stream.IntStream;

public class EBirthday {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int threshold = (int) 1e7;
        ACAutomaton2 ac = new ACAutomaton2('a', 'b', (int)1e7);
        byte[] buf = new byte[threshold];
        byte[][] s = new byte[n][];
        boolean[] skip = new boolean[n];
        int[] terminals = new int[n];
        for (int i = 0; i < n; i++) {
            int len = in.rs(buf, 0);
            s[i] = Arrays.copyOf(buf, len);
        }
        debug.elapse("read");
        for (int i = 0; i < n; i++) {
            ac.prepareBuild();
            for (byte x : s[i]) {
                ac.build((char) x);
            }
            terminals[i] = ac.buildLast;
            if (ac.input[terminals[i]] == null) {
                ac.input[terminals[i]] = new BitSet(n);
            } else {
                skip[i] = true;
                continue;
            }
        }
        debug.elapse("build");
        ac.endBuild();

        debug.elapse("build fail");
        for (int i = 0; i < n; i++) {
            if (skip[i]) {
                continue;
            }
            ac.prepareMatch();
            for (byte x : s[i]) {
                ac.match(x);
                int last = ac.matchLast;
                if (ac.input[last] != null) {
                    ac.input[last].set(i);
                }
            }
        }

        debug.elapse("match");
        int[] order = IntStream.range(0, n).toArray();
        SortUtils.quickSort(order, (i, j) -> -(s[i].length - s[j].length), 0, order.length);
        for (int i : order) {
            if (ac.terminals[terminals[i]] != 0) {
                ac.input[ac.terminals[terminals[i]]].or(ac.input[terminals[i]]);
            }
        }
        DinicBipartiteMatch bm = new DinicBipartiteMatch(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && ac.input[terminals[i]].get(j)) {
                    bm.addEdge(j, i);
                }
            }
        }
        List<Integer> ans = new ArrayList<>();
        bm.solve();
        boolean[][] mis = bm.maxIndependentSet();
        for (int i = 0; i < n; i++) {
            if (skip[i]) {
                continue;
            }
            if (mis[0][i] && mis[1][i]) {
                ans.add(i);
            }
        }

        debug.elapse("bm");
        out.println(ans.size());
        for (int x : ans) {
            out.append(x + 1).append(' ');
        }

        debug.summary();
    }
}

class ACAutomaton2 {
    public int[][] next;
    public int[] fails;
    private int minChar;
    private int maxChar;
    private int allocIndicator;
    private int range;
    public int[] terminals;
    public BitSet[] input;


    public void init() {
        allocIndicator = 1;
    }

    private int alloc() {
        return allocIndicator++;
    }

    public ACAutomaton2(int minChar, int maxChar, int cap) {
        this.minChar = minChar;
        this.maxChar = maxChar;
        range = maxChar - minChar + 1;
        next = new int[range][cap + 1];
        fails = new int[cap + 1];
        input = new BitSet[cap + 1];
        terminals = new int[cap + 1];
        init();
    }

    public void prepareBuild() {
        buildLast = 0;
    }

    public void prepareMatch() {
        matchLast = 0;
    }

    public int buildLast;
    public int matchLast;

    public void build(int c) {
        c -= minChar;
        if (next[c][buildLast] == 0) {
            next[c][buildLast] = alloc();
        }
        buildLast = next[c][buildLast];
    }

    public void match(int c) {
        c -= minChar;
        matchLast = next[c][matchLast];
    }

    public int[] endBuild() {
        int[] dq = new int[allocIndicator];
        int dqTail = 1;
        int dqHead = 1;
        fails[0] = -1;
        for (int i = 0; i < range; i++) {
            if (next[i][0] != 0) {
                dq[dqTail++] = next[i][0];
                fails[next[i][0]] = 0;
            }
        }

        while (dqHead < dqTail) {
            int head = dq[dqHead++];
            terminals[head] = input[fails[head]] != null ? fails[head] : terminals[fails[head]];
            if(input[head] == null){
                input[head] = input[terminals[head]];
            }
            for (int i = 0; i < range; i++) {
                if (next[i][head] != 0) {
                    dq[dqTail++] = next[i][head];
                    int fail = visit(fails[head], i);
                    if (fail == -1) {
                        fail = 0;
                    } else {
                        fail = next[i][fail];
                    }
                    fails[next[i][head]] = fail;
                }
            }
        }

        assert dqTail == allocIndicator;
        return dq;
    }

    public int visit(int trace, int index) {
        while (trace != -1 && next[index][trace] == 0) {
            trace = fails[trace];
        }
        return trace;
    }
}

