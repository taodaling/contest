package on2020_10.on2020_10_20_Library_Checker.Dynamic_Graph_Vertex_Add_Component_Sum;



import template.algo.*;
import template.algo.CommutativeUndoOperation;
import template.algo.UndoOperation;
import template.io.FastInput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicGraphVertexAddComponentSum {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int q = in.readInt();

        stack = new UndoStack(q * 20);
        dsu = new RevokeDSU(n);
        dsu.init();
        for (int i = 0; i < n; i++) {
            dsu.size[i] = in.readInt();
        }
        Segment segment = new Segment(0, q);

        LongHashMap map = new LongHashMap(q, false);
        queries = new Query[q];

        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 0) {
                int u = in.readInt();
                int v = in.readInt();
                map.put(DigitUtils.asLong(u, v), i);
            } else if (t == 1) {
                int u = in.readInt();
                int v = in.readInt();
                int l = (int) map.get(DigitUtils.asLong(u, v));
                map.remove(DigitUtils.asLong(u, v));
                segment.update(l, i, 0, q - 1, dsu.merge(u, v));
            } else if (t == 2) {
                int v = in.readInt();
                int x = in.readInt();
                segment.update(i, q - 1, 0, q - 1, dsu.modify(v, x));
            } else if (t == 3) {
                Query query = new Query();
                query.v = in.readInt();
                queries[i] = query;
            }
        }

        for (LongEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            long key = iterator.getEntryKey();
            int l = (int) iterator.getEntryValue();
            int v = DigitUtils.lowBit(key);
            int u = DigitUtils.highBit(key);
            segment.update(l, q - 1, 0, q - 1, dsu.merge(u, v));
        }

        dfs(segment, 0, q - 1);
        for (int i = 0; i < q; i++) {
            if (queries[i] == null) {
                continue;
            }
            out.println(queries[i].ans);
        }
    }

    public void dfs(Segment root, int l, int r) {
        for (UndoOperation op : root.ops) {
            stack.push(op);
        }
        if (l == r) {
            if (queries[l] != null) {
                queries[l].ans = dsu.size[dsu.find(queries[l].v)];
            }
        } else {
            int m = DigitUtils.floorAverage(l, r);
            dfs(root.left, l, m);
            dfs(root.right, m + 1, r);
        }
        for (UndoOperation op : root.ops) {
            stack.pop();
        }
    }

    RevokeDSU dsu;
    UndoStack stack;
    Query[] queries;
}

class Query {
    int v;
    long ans;
}

class RevokeDSU {
    int[] rank;
    int[] p;
    long[] size;


    public RevokeDSU(int n) {
        rank = new int[n];
        p = new int[n];
        size = new long[n];
    }

    public void init() {
        Arrays.fill(rank, 1);
        Arrays.fill(p, -1);
        Arrays.fill(size, 0);
    }

    public int find(int x) {
        while (p[x] != -1) {
            x = p[x];
        }
        return x;
    }

    public CommutativeUndoOperation modify(int x, int mod) {
        return new CommutativeUndoOperation() {
            @Override
            public void apply() {
                int y = x;
                while (y != -1) {
                    size[y] += mod;
                    y = p[y];
                }
            }

            @Override
            public void undo() {
                int y = x;
                while (y != -1) {
                    size[y] -= mod;
                    y = p[y];
                }
            }
        };
    }

    public int size(int x) {
        return rank[find(x)];
    }

    public CommutativeUndoOperation merge(int a, int b) {
        return new CommutativeUndoOperation() {
            int x, y;


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
                size[x] += size[y];
            }


            public void undo() {
                int cur = y;
                while (p[cur] != -1) {
                    cur = p[cur];
                    rank[cur] -= rank[y];
                    size[cur] -= size[y];
                }
                p[y] = -1;
            }
        };
    }
}

class Segment implements Cloneable {
    Segment left;
    Segment right;
    List<UndoOperation> ops = new ArrayList<>();

    private void modify(UndoOperation op) {
        ops.add(op);
    }

    public void pushUp() {
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, UndoOperation op) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(op);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, op);
        right.update(ll, rr, m + 1, r, op);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append("val").append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}

