package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P2056ZJOI2007 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        seq = new IntegerArrayList(2 * n);
        open = new boolean[n * 2];
        close = new boolean[n * 2];
        dfs(nodes[0], null);
        L = 0;
        R = seq.size() - 1;
        st = new SegTree<>(L, R, SumImpl::new, UpdateImpl::new, i -> {
            SumImpl s = new SumImpl();
            s.b = seq.get(i);
            upd.enableClose = close[i];
            upd.enableOpen = open[i];
            s.update(upd);
            return s;
        });
        lights = new boolean[n];
        Arrays.fill(lights, true);
        lightCnt = n;
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            debug.debug("st", st);
            char c = in.rc();
            if (c == 'C') {
                int x = in.ri() - 1;
                change(x);
            } else {
                if (lightCnt == 0) {
                    out.println(-1);
                } else {
                    out.println(st.sum.abc);
                }
            }
        }
    }

    IntegerArrayList seq;
    boolean[] open;
    boolean[] close;

    void dfs(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        root.open = seq.size();
        seq.add(root.depth);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            seq.add(root.depth);
        }
        root.close = seq.size() - 1;
        open[root.open] = close[root.close] = true;
    }

    boolean[] lights;
    int lightCnt = 0;
    UpdateImpl upd = new UpdateImpl();
    int L;
    int R;
    SegTree<SumImpl, UpdateImpl> st;
    Node[] nodes;

    public void change(int i) {
        if (lights[i]) {
            lightCnt--;
            upd.clear();
            st.update(nodes[i].open, nodes[i].open, L, R, upd);
            if (nodes[i].open != nodes[i].close) {
                st.update(nodes[i].close, nodes[i].close, L, R, upd);
            }
        }
        lights[i] = !lights[i];
        if (lights[i]) {
            lightCnt++;
            upd.clear();
            upd.enableOpen = true;
            if (nodes[i].open == nodes[i].close) {
                upd.enableClose = true;
            }
            st.update(nodes[i].open, nodes[i].open, L, R, upd);
            if (nodes[i].open != nodes[i].close) {
                upd.clear();
                upd.enableClose = true;
                st.update(nodes[i].close, nodes[i].close, L, R, upd);
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int depth;
    int open;
    int close;
    int id;
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    boolean enableOpen;
    boolean enableClose;

    @Override
    public void update(UpdateImpl update) {
    }

    @Override
    public void clear() {
        enableOpen = enableClose = false;
    }

    @Override
    public boolean ofBoolean() {
        return false;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    static int inf = (int) 1e8;
    int a;
    int b;
    int c;
    int ab;
    int bc;
    int abc;

    @Override
    public void add(SumImpl sum) {
        abc = Math.max(abc, sum.abc);
        abc = Math.max(abc, a + sum.bc);
        abc = Math.max(abc, ab + sum.c);
        ab = Math.max(ab, sum.ab);
        ab = Math.max(ab, a - 2 * sum.b);
        bc = Math.max(bc, sum.bc);
        bc = Math.max(bc, -2 * b + sum.c);
        a = Math.max(a, sum.a);
        b = Math.min(b, sum.b);
        c = Math.max(c, sum.c);
    }

    @Override
    public void update(UpdateImpl update) {
        a = -inf;
        c = -inf;
        ab = -inf;
        bc = -inf;
        abc = -inf;
        if (update.enableOpen && update.enableClose) {
            a = c = b;
            ab = bc = -b;
            abc = 0;
        } else if (update.enableOpen) {
            a = b;
            ab = a - b * 2;
        } else if (update.enableClose) {
            c = b;
            bc = -2 * b + c;
        }
    }

    @Override
    public void copy(SumImpl sum) {
        a = sum.a;
        b = sum.b;
        c = sum.c;
        ab = sum.ab;
        bc = sum.bc;
        abc = sum.abc;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + "," + c + ")";
    }
}

