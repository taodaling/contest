package on2020_10.on2020_10_23_AtCoder___AtCoder_Beginner_Contest_163.F___path_pass_i;



import template.io.FastInput;
import template.math.DigitUtils;
import template.utils.Buffer;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FPathPassI {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        n = in.readInt();
        Node[] nodes = new Node[n];

        int[] classify = new int[n + 1];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].c = in.readInt();
            nodes[i].id = i;
            classify[nodes[i].c]++;
        }
        for (int i = 0; i < n - 1; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }
        ans = new long[n + 1];
        Wrapper seg = dfs(nodes[0], null);
        debug.debug("ans", ans);
        for (int i = 1; i <= n; i++) {
            long cnt = seg.get(i, 1, n);
            ans[i] += cnt * (n - cnt);
            ans[i] += (long) classify[i] * (n - 1);
            ans[i] /= 2;
            ans[i] += classify[i];
            out.println(ans[i]);
        }

    }

    public Wrapper dfs(Node root, Node p) {
        Wrapper now = new Wrapper();
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            Wrapper seg = dfs(node, root);
            long cnt = seg.get(root.c, 1, n);
            ans[root.c] += cnt * (n - cnt);
            now.merge(seg, 1, n);
        }
        now.mod++;
        now.set0(root.c, 1, n);
        return now;
    }

    int n;
    long[] ans;
}

class Wrapper {
    MergeAbleCountSegment seg = new MergeAbleCountSegment();
    int mod;

    public int get(int i, int l, int r) {
        return seg.query(i, i, l, r) + mod;
    }

    public void set0(int i, int l, int r) {
        seg.update(i, l, r, -get(i, l, r));
    }

    public void merge(Wrapper x, int l, int r) {
        seg = seg.merge(l, r, x.seg);
        mod += x.mod;
    }
}

class Node {
    int c;
    List<Node> adj = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}


class MergeAbleCountSegment implements Cloneable {
    public static final MergeAbleCountSegment NIL = new MergeAbleCountSegment();
    private static Buffer<MergeAbleCountSegment> allocator = new Buffer<MergeAbleCountSegment>(MergeAbleCountSegment::new, x -> {
        x.left = x.right = null;
        x.cnt = 0;
    });

    public static MergeAbleCountSegment alloc() {
        return new MergeAbleCountSegment();
    }

    public static void destroy(MergeAbleCountSegment segment) {
        //allocator.addLast(segment);
    }

    static {
        NIL.left = NIL;
        NIL.right = NIL;
    }

    private MergeAbleCountSegment left;
    private MergeAbleCountSegment right;
    private int cnt;

    public void pushUp() {
        cnt = left.cnt + right.cnt;
    }

    public void pushDown() {
    }

    public MergeAbleCountSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int x, int l, int r, int mod) {
        if (l == r) {
            cnt += mod;
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (x <= m) {
            if (left == NIL) {
                left = alloc();
            }
            left.update(x, l, m, mod);
        } else {
            if (right == NIL) {
                right = alloc();
            }
            right.update(x, m + 1, r, mod);
        }
        pushUp();
    }

    public int kth(int l, int r, int k) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (left.cnt >= k) {
            return left.kth(l, m, k);
        } else {
            return right.kth(m + 1, r, k - left.cnt);
        }
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return cnt;
        }
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    /**
     * split this by kth element, and kth element belong to the left part.
     * Return the k-th element as result
     */
    public MergeAbleCountSegment splitByKth(int k, int l, int r) {
        MergeAbleCountSegment ret = alloc();
        if (l == r) {
            ret.cnt = k;
            cnt -= k;
            return ret;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (k >= left.cnt) {
            k -= left.cnt;
            ret.left = left;
            left = NIL;
        } else {
            ret.left = left.splitByKth(k, l, m);
            k = 0;
        }
        if (k > 0) {
            if (k >= right.cnt) {
                ret.right = right;
                right = NIL;
            } else {
                ret.right = right.splitByKth(k, l, m);
            }
        }

        ret.pushUp();
        this.pushUp();
        return ret;
    }

    public MergeAbleCountSegment merge(int l, int r, MergeAbleCountSegment segment) {
        if (this == NIL) {
            return segment;
        } else if (segment == NIL) {
            return this;
        }
        if (l == r) {
            cnt += segment.cnt;
            destroy(segment);
            return this;
        }
        int m = DigitUtils.floorAverage(l, r);
        left = left.merge(l, m, segment.left);
        right = right.merge(m + 1, r, segment.right);
        destroy(segment);
        pushUp();
        return this;
    }

    public void toString(int l, int r, StringBuilder builder) {
        if (this == NIL) {
            return;
        }
        if (l == r) {
            builder.append(l).append(':').append(cnt).append(',');
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        toString(l, m, builder);
        toString(m + 1, r, builder);
    }

    public String toString(int l, int r) {
        StringBuilder builder = new StringBuilder("{");
        toString(l, r, builder);
        if (builder.length() > 1) {
            builder.setLength(builder.length() - 1);
        }
        builder.append('}');
        return builder.toString();
    }
}