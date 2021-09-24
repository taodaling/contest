package on2021_05.on2021_05_19_DMOJ.DMOPC__20_Contest_5_P6___Top_Row0;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;
import template.utils.Debug;
import template.utils.SegmentUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DMOPC20Contest5P6TopRow {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        int L = 0;
        int R = n - 1;
        int q = in.ri();
        LazyPersistentSegTreeBasedOnArray ps = new LazyPersistentSegTreeBasedOnArray(L, R, ((int)1.5e6));
        LCTNode.L = L;
        LCTNode.R = R;
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
        for (int i = 0; i < n; i++) {
            sa.build(s[i]);
        }
        for (SuffixAutomaton.SANode node : sa.all) {
            node.lct = new LCTNode();
            node.lct.sa = node;
            node.lct.pushUp();
        }

        for (SuffixAutomaton.SANode node : sa.all) {
            if (node.fail == null) {
                continue;
            }
            LCTNode a = node.lct;
            LCTNode b = node.fail.lct;
            LCTNode.join(a, b);
        }
        LCTNode.makeRoot(sa.root.lct);

        int[] history = new int[n + 1];
        sa.beginMatch();

        LCTNode.persistentSegment = ps;
        for (int i = 0; i < n; i++) {
            sa.match(s[i]);
            LCTNode tail = sa.matchLast.lct;
            LCTNode.access(tail, i);
            history[i + 1] = ps.last;
        }

        Sum sum = new Sum();
        long lastAns = 0;
        for (int i = 0; i < q; i++) {
            int l = (int) ((lastAns ^ in.rl()) - 1);
            int r = (int) ((lastAns ^ in.rl()) - 1);
            debug.debug("l", l);
            debug.debug("r", r);
            sum.init();
            ps.query(history[r + 1], l, r, 0, 0, sum);
            lastAns = sum.x;
            debug.debug("lastans", lastAns);
            out.println(lastAns);
        }
    }

    Debug debug = new Debug(false);
}

class Sum {
    public long x;

    void add(long d) {
        x += d;
    }

    void init() {
        x = 0;
    }
}

class LazyPersistentSegTreeBasedOnArray {
    int[] left;
    int[] right;
    int allocator = 1;
    long[] a;
    long[] b;
    long[] sum;

    public void clear() {
        while (allocator > 1) {
            allocator--;
            left[allocator] = 0;
            right[allocator] = 0;
        }
        last = 0;
    }

    private void duplicate() {
        int cap = left.length * 2;
        left = Arrays.copyOf(left, cap);
        right = Arrays.copyOf(right, cap);
        a = Arrays.copyOf(a, cap);
        b = Arrays.copyOf(b, cap);
        sum = Arrays.copyOf(sum, cap);
    }

    private int alloc() {
        if (allocator >= left.length) {
            duplicate();
        }
        return allocator++;
    }

    private int clone(int root) {
        int node = alloc();
        left[node] = left[root];
        right[node] = right[root];
        a[node] = a[root];
        b[node] = b[root];
        sum[node] = sum[root];
        return node;
    }

    int begin;
    int end;

    public LazyPersistentSegTreeBasedOnArray(int begin, int end, int opNum) {
        this.begin = begin;
        this.end = end;
        int len = end - begin + 1;
        int height = Log2.ceilLog(len) + 1;
        int expSize = height * opNum * 2 + 1;
        left = new int[expSize];
        right = new int[expSize];
        a = new long[expSize];
        b = new long[expSize];
        sum = new long[expSize];
    }

    public void query(int L, int R, long ma, long mb, Sum s) {
        query(last, L, R, ma, mb, s);
    }

    public void query(int version, int L, int R, long ma, long mb, Sum s) {
        query0(clone(version), L, R, begin, end, s, ma, mb);
    }

    private void query0(int root, int L, int R, int l, int r, Sum s, long ma, long mb) {
        if (SegmentUtils.leave(L, R, l, r)) {
            return;
        }
        if (SegmentUtils.enter(L, R, l, r)) {
            s.add(sum[root] + ma * IntMath.sumOfInterval(l, r) + mb * (r - l + 1));
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
//        pushDown(root, l, r);
        ma += a[root];
        mb += b[root];
        query0(left[root], L, R, l, m, s, ma, mb);
        query0(right[root], L, R, m + 1, r, s, ma, mb);
        return;
    }

    private void modify(int root, int l, int r, long ma, long mb) {
        a[root] += ma;
        b[root] += mb;
        sum[root] += ma * IntMath.sumOfInterval(l, r) + mb * (r - l + 1);
    }

    private void pushUp(int root, int l, int r) {
        sum[root] = sum[left[root]] + sum[right[root]];
    }

    private void pushDown(int root, int l, int r) {
        left[root] = clone(left[root]);
        right[root] = clone(right[root]);
        if (a[root] != 0 || b[root] != 0) {
            int m = DigitUtils.floorAverage(l, r);
            modify(left[root], l, m, a[root], b[root]);
            modify(right[root], m + 1, r, a[root], b[root]);
            a[root] = b[root] = 0;
        }
    }

    int last = 0;

    public int update(int L, int R, long ma, long mb) {
        return update(last, L, R, ma, mb);
    }

    public int update(int version, int L, int R, long ma, long mb) {
        last = clone(version);
        update0(last, L, R, begin, end, ma, mb);
        return last;
    }

    private void update0(int root, int L, int R, int l, int r, long ma, long mb) {
        if (SegmentUtils.leave(L, R, l, r)) {
            return;
        }
        if (SegmentUtils.enter(L, R, l, r)) {
            modify(root, l, r, ma, mb);
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        pushDown(root, l, r);
        update0(left[root], L, R, l, m, ma, mb);
        update0(right[root], L, R, m + 1, r, ma, mb);
        pushUp(root, l, r);
        return;
    }
}

class SuffixAutomaton {
    final int minCharacter;
    final int maxCharacter;
    final int alphabet;
    public SuffixAutomaton.SANode root;
    public SuffixAutomaton.SANode buildLast;
    public SuffixAutomaton.SANode matchLast;
    public int matchLength;
    public List<SANode> all;
    public boolean sorted = true;
    public long realTimeDistinctSubstr = -1;

    public SuffixAutomaton(int minCharacter, int maxCharacter) {
        this(minCharacter, maxCharacter, 0);
    }

    public SuffixAutomaton(int minCharacter, int maxCharacter, int cap) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        all = new ArrayList<>(cap * 2 + 1);
        alphabet = maxCharacter - minCharacter + 1;
        buildLast = root = newNode();
        root.fail = null;
    }

    private SuffixAutomaton.SANode newNode() {
        SuffixAutomaton.SANode ans = new SuffixAutomaton.SANode(alphabet);
        all.add(ans);
        return ans;
    }

    private SuffixAutomaton.SANode cloneNode(SuffixAutomaton.SANode x) {
        SuffixAutomaton.SANode ans = x.clone();
        all.add(ans);
        return ans;
    }

    public void beginMatch() {
        matchLast = root;
        matchLength = 0;
    }

    public void match(int c) {
        int index = c - minCharacter;
        if (matchLast.next[index] != null) {
            matchLast = matchLast.next[index];
            matchLength = matchLength + 1;
        } else {
            while (matchLast != null && matchLast.next[index] == null) {
                matchLast = matchLast.fail;
            }
            if (matchLast == null) {
                matchLast = root;
                matchLength = 0;
            } else {
                matchLength = matchLast.maxlen + 1;
                matchLast = matchLast.next[index];
            }
        }
    }

    public void build(int c) {
        sorted = false;
        int index = c - minCharacter;
        SuffixAutomaton.SANode now = newNode();
        now.maxlen = buildLast.maxlen + 1;

        SuffixAutomaton.SANode p = visit(index, buildLast, null, now);
        if (p == null) {
            now.fail = root;
        } else {
            SuffixAutomaton.SANode q = p.next[index];
            if (q.maxlen == p.maxlen + 1) {
                now.fail = q;
            } else {
                SuffixAutomaton.SANode clone = cloneNode(q);
                clone.maxlen = p.maxlen + 1;
                now.fail = q.fail = clone;
                if (realTimeDistinctSubstr != -1) {
                    realTimeDistinctSubstr -= q.maxlen - clone.fail.maxlen;
                    realTimeDistinctSubstr += q.maxlen - q.fail.maxlen;
                    realTimeDistinctSubstr += clone.maxlen - clone.fail.maxlen;
                }
                visit(index, p, q, clone);
            }
        }
        if (realTimeDistinctSubstr != -1) {
            realTimeDistinctSubstr += now.maxlen - now.fail.maxlen;
        }
        buildLast = now;
    }

    public SuffixAutomaton.SANode visit(int index, SuffixAutomaton.SANode trace, SuffixAutomaton.SANode target, SuffixAutomaton.SANode replacement) {
        while (trace != null && trace.next[index] == target) {
            trace.next[index] = replacement;
            trace = trace.fail;
        }
        return trace;
    }

    class SANode implements Cloneable {
        public SuffixAutomaton.SANode[] next;
        public SuffixAutomaton.SANode fail;
        public int maxlen;
        LCTNode lct;

        public SANode(int alphabet) {
            next = new SuffixAutomaton.SANode[alphabet];
        }

        public int minLength() {
            return fail == null ? 1 : fail.maxlen + 1;
        }

        public SuffixAutomaton.SANode clone() {
            try {
                SuffixAutomaton.SANode res = (SuffixAutomaton.SANode) super.clone();
                res.next = res.next.clone();
                return res;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

    }

}

class LCTNode {
    public static final LCTNode NIL = new LCTNode();
    static LazyPersistentSegTreeBasedOnArray persistentSegment;
    static int L;
    static int R;
    public LCTNode left = NIL;
    public LCTNode right = NIL;
    public LCTNode father = NIL;
    public LCTNode treeFather = NIL;
    public boolean reverse;
    public int id;
    public int color = -1;
    SuffixAutomaton.SANode sa;
    int l;
    int r;

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
        NIL.l = (int) 1e9;
        NIL.r = -1;
    }

    static void consider(LCTNode node, int mod) {
        int i = node.color;
        int l = i - node.r + 1;
        int r = i - node.l + 1;
        persistentSegment.update(l, r, -1 * mod, (long) (1 + i + 1) * mod);
    }

    public static void access(LCTNode x, int cur) {
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            x.right.father = NIL;
            x.right.treeFather = x;
            x.setRight(NIL);
            x.pushUp();
            if (x.color != -1) {
                consider(x, -1);
            }
            x.setRight(last);
            x.pushUp();
            last = x;
            x = x.treeFather;
        }
        last.color = cur;
        if (last.color != -1) {
            consider(last, 1);
        }
    }

    public static void makeRoot(LCTNode x) {
        access(x, -1);
        splay(x);
        x.reverse();
    }

    public static void join(LCTNode y, LCTNode x) {
        makeRoot(x);
        makeRoot(y);
        x.treeFather = y;
        y.pushUp();
    }

    public static void splay(LCTNode x) {
        if (x == NIL) {
            return;
        }
        LCTNode y, z;
        while ((y = x.father) != NIL) {
            if ((z = y.father) == NIL) {
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    zig(x);
                } else {
                    zag(x);
                }
            } else {
                z.pushDown();
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    if (y == z.left) {
                        zig(y);
                        zig(x);
                    } else {
                        zig(x);
                        zag(x);
                    }
                } else {
                    if (y == z.left) {
                        zag(x);
                        zig(x);
                    } else {
                        zag(y);
                        zag(x);
                    }
                }
            }
        }

        x.pushDown();
        x.pushUp();
    }

    public static void zig(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.right;

        y.setLeft(b);
        x.setRight(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static void zag(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.left;

        y.setRight(b);
        x.setLeft(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public String toString() {
        return "" + id;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (reverse) {
            reverse = false;

            LCTNode tmpNode = left;
            left = right;
            right = tmpNode;

            left.reverse();
            right.reverse();
        }

        left.treeFather = treeFather;
        right.treeFather = treeFather;
        left.color = color;
        right.color = color;
    }

    public void reverse() {
        reverse = !reverse;
    }

    public void setLeft(LCTNode x) {
        left = x;
        x.father = this;
    }

    public void setRight(LCTNode x) {
        right = x;
        x.father = this;
    }

    public void changeChild(LCTNode y, LCTNode x) {
        if (left == y) {
            setLeft(x);
        } else {
            setRight(x);
        }
    }

    public void pushUp() {
        if (this == NIL) {
            return;
        }
        l = sa.minLength();
        r = sa.maxlen;
        l = Math.min(l, left.l);
        l = Math.min(l, right.l);
        r = Math.max(r, left.r);
        r = Math.max(r, right.r);
    }

}
