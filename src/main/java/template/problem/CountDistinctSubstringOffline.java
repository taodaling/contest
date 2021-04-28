package template.problem;

import template.datastructure.RangeAffineRangeSum;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.LongBITExt;
import template.string.IntSequence;
import template.utils.SortUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;

public class CountDistinctSubstringOffline {
    public static long[] solve(IntSequence s, int min, int max, int[][] qs) {
        int q = qs.length;
        long[] ans = new long[q];
        int[] indices = IntStream.range(0, q).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(qs[i][1], qs[j][1]),
                0, q);

        int n = s.length();
        int L = 0;
        int R = n - 1;
        LongBITExt st = new LongBITExt(n);
        LCTNode.L = L;
        LCTNode.R = R;
        SuffixAutomaton sa = new SuffixAutomaton(min, max);
        for (int i = 0; i < n; i++) {
            sa.build(s.get(i));
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

        LCTNode.st = st;
        sa.beginMatch();
        int nextProcess = 0;
        for (int i = 0; i < n; i++) {
            sa.match(s.get(i));
            LCTNode tail = sa.matchLast.lct;
            LCTNode.access(tail, i);
            while (nextProcess < q && qs[indices[nextProcess]][1] == i) {
                int[] head = qs[indices[nextProcess]];
                ans[indices[nextProcess]] = st.query(head[0] + 1, head[1] + 1);
                nextProcess++;
            }
        }
        return ans;
    }

    static class LCTNode {
        public static final LCTNode NIL = new LCTNode();

        static LongBITExt st;
        static int L;
        static int R;

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.treeFather = NIL;
            NIL.l = (int) 1e9;
            NIL.r = -1;
        }

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

        public void init() {
            left = right = father = treeFather = NIL;
            reverse = false;
            pushUp();
        }

        static void consider(LCTNode node, int mod) {
            int i = node.color;
            int l = i - node.r + 1;
            int r = i - node.l + 1;
            st.update(l + 1, r + 1, 1 * mod);
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

        public static void cut(LCTNode y, LCTNode x) {
            makeRoot(y);
            access(x, -1);
            splay(y);
            y.right.treeFather = NIL;
            y.right.father = NIL;
            y.setRight(NIL);
            y.pushUp();
        }

        public static void join(LCTNode y, LCTNode x) {
            makeRoot(x);
            makeRoot(y);
            x.treeFather = y;
            y.pushUp();
        }

        public static void findRoute(LCTNode x, LCTNode y) {
            makeRoot(y);
            access(x, -1);
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

        public static LCTNode findRoot(LCTNode x) {
            splay(x);
            x.pushDown();
            while (x.left != NIL) {
                x = x.left;
                x.pushDown();
            }
            splay(x);
            return x;
        }

        @Override
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

    static class SuffixAutomaton {
        final int minCharacter;
        final int maxCharacter;
        final int alphabet;
        public SANode root;
        public SANode buildLast;
        public SANode matchLast;
        public int matchLength;
        public List<SANode> all;
        public boolean sorted = true;

        public long realTimeDistinctSubstr = -1;

        public void enableDistinctSubstr() {
            realTimeDistinctSubstr = 0;
        }

        public void prepareBuild() {
            buildLast = root;
        }

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

        private SANode newNode() {
            SANode ans = new SANode(alphabet);
            all.add(ans);
            return ans;
        }

        private SANode cloneNode(SANode x) {
            SANode ans = x.clone();
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
            SANode now = newNode();
            now.maxlen = buildLast.maxlen + 1;

            SANode p = visit(index, buildLast, null, now);
            if (p == null) {
                now.fail = root;
            } else {
                SANode q = p.next[index];
                if (q.maxlen == p.maxlen + 1) {
                    now.fail = q;
                } else {
                    SANode clone = cloneNode(q);
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

        public SANode visit(int index, SANode trace, SANode target, SANode replacement) {
            while (trace != null && trace.next[index] == target) {
                trace.next[index] = replacement;
                trace = trace.fail;
            }
            return trace;
        }

        public void topoSort() {
            if (sorted) {
                return;
            }
            sorted = true;
            Deque<SANode> dq = new ArrayDeque<>(all.size());
            for (SANode node : all) {
                if (node.fail != null) {
                    node.fail.indeg++;
                }
            }
            for (SANode node : all) {
                if (node.indeg == 0) {
                    dq.addLast(node);
                }
            }
            all.clear();
            while (!dq.isEmpty()) {
                SANode head = dq.removeFirst();
                all.add(head);
                if (head.fail != null) {
                    head.fail.indeg--;
                    if (head.fail.indeg == 0) {
                        dq.addLast(head.fail);
                    }
                }
            }
        }

        public void calcRight(IntToIntegerFunction func, int n) {
            topoSort();
            beginMatch();
            for (int i = 0; i < n; i++) {
                match(func.apply(i));
                matchLast.right++;
            }
            for (SANode node : all) {
                if (node.fail != null) {
                    node.fail.right += node.right;
                }
            }
        }


        public static class SANode implements Cloneable {
            public SANode[] next;
            /**
             * right最小的一个顶点，且满足fail.right是right的真超集
             */
            public SANode fail;
            /**
             * 对于每个right集合中的元素r，以及minLength()<=i<=maxlen, S[r-i+1,r]都会转移到这个状态
             */
            public int maxlen;
            /**
             * right表示这个子串在S中出现的右端点位置数目
             */
            public int right;
            public int indeg;
            LCTNode lct;

            public SANode(int alphabet) {
                next = new SANode[alphabet];
            }

            public int minLength() {
                return fail == null ? 0 : fail.maxlen + 1;
            }

            @Override
            public SANode clone() {
                try {
                    SANode res = (SANode) super.clone();
                    res.next = res.next.clone();
                    return res;
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
