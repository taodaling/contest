package on2021_07.on2021_07_16_DMOJ___DMOPC__20_Contest_1.P5___Victor_Takes_Over_Canada;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class P5VictorTakesOverCanada {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int m = in.ri();

        open = new int[n];
        close = new int[n];
        color = new int[n + 1];
        Arrays.fill(color, -1);
        reg = new int[n + 1];
        IntegerArrayList us = new IntegerArrayList(n - 1);
        IntegerArrayList vs = new IntegerArrayList(n - 1);
        int[] assign = new int[n];
        occur = IntStream.range(0, k).mapToObj(x -> new TreeSet<>()).toArray(i -> new TreeSet[i]);
        bit = new IntegerBIT(n);
        p = new int[n];
        for (int i = 0; i < n; i++) {
            assign[i] = in.ri() - 1;
        }
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            us.add(a);
            vs.add(b);
        }
        g = Graph.createUndirectedGraph(n, n - 1, us.getData(), vs.getData());
        debug.elapse("init");
        dfs(0, -1);
        debug.elapse("dfs");
        debug.elapse("rmq");
        ParentOnTree pot = new ParentOnTreeByGivenArray(p);
        DepthOnTree dot = new DepthOnTreeByParent(n, pot);

        bl = new CompressedBinaryLift(n, dot, pot);
        for (int i = 0; i < n; i++) {
            color[open[i]] = assign[i];
        }
        for (int i = 1; i <= n; i++) {
            if (color[i] == -1) {
                continue;
            }
            occur[color[i]].add(i);
        }
        for (int i = 0; i < k; i++) {
            int last = -1;
            for (int pos : occur[i]) {
                bit.update(pos, 1);
                if (last != -1) {
                    bit.update(lca(last, pos), -1);
                }
                last = pos;
            }
        }
        debug.elapse("init color");
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int pos = open[in.ri() - 1];
            if (t == 1) {
                int c = in.ri() - 1;
                delete(pos);
                color[pos] = c;
                add(pos);
            } else {
                int l = open[reg[pos]];
                int r = close[reg[pos]];
                int sum = bit.query(l, r);
                out.println(sum);
            }
        }
        debug.elapse("solve");
    }

    CompressedBinaryLift bl;
    int[] color;
    int[] open;
    int[] close;
    int[] reg;
    int[] p;
    int order = 1;
    int[][] g;
    TreeSet<Integer>[] occur;
    IntegerBIT bit;
    Debug debug = new Debug(true);

    public int lca(int p1, int p2) {
        int index = bl.lca(reg[p1], reg[p2]);
        return open[index];
    }

    public void delete(int pos) {
        int c = color[pos];
        Integer floor = occur[c].floor(pos - 1);
        Integer ceil = occur[c].ceiling(pos + 1);
        if (floor != null) {
            bit.update(lca(floor, pos), 1);
        }
        if (ceil != null) {
            bit.update(lca(pos, ceil), 1);
        }
        if (floor != null && ceil != null) {
            bit.update(lca(floor, ceil), -1);
        }
        bit.update(pos, -1);
        occur[c].remove(pos);
    }

    public void add(int pos) {
        int c = color[pos];
        Integer floor = occur[c].floor(pos - 1);
        Integer ceil = occur[c].ceiling(pos + 1);
        if (floor != null) {
            bit.update(lca(floor, pos), -1);
        }
        if (ceil != null) {
            bit.update(lca(pos, ceil), -1);
        }
        if (floor != null && ceil != null) {
            bit.update(lca(floor, ceil), 1);
        }
        bit.update(pos, 1);
        occur[c].add(pos);
    }

    public void dfs(int root, int p) {
        this.p[root] = p;
        close[root] = open[root] = order;
        reg[order] = root;
        order++;
        for (int node : g[root]) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
        close[root] = order - 1;
    }
}


