package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntegerFunction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class RepeatingSubstring {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z', n);
        for (int i = 0; i < n; i++) {
            sa.build(s[i]);
        }
        sa.calcRight(i -> s[i], n);
        SuffixAutomaton.SANode best = null;
        for (SuffixAutomaton.SANode node : sa.all) {
            if (node.right <= 1 || node.maxlen == 0) {
                continue;
            }
            if (best == null || best.maxlen < node.maxlen) {
                best = node;
            }
        }
        if (best == null) {
            out.println(-1);
            return;
        }
        for (int i = best.occur - best.maxlen + 1; i <= best.occur; i++) {
            out.append(s[i]);
        }
    }
}


class SuffixAutomaton {
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

    public SuffixAutomaton(int minCharacter, int maxCharacter) {
        this(minCharacter, maxCharacter, 0);
    }

    public SuffixAutomaton(int minCharacter, int maxCharacter, int cap) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        all = new ArrayList<>(cap + 1);
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
            return;
        }
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
        for (SANode node : all) {
            node.right = 0;
            node.occur = (int) 1e9;
        }
        for (int i = 0; i < n; i++) {
            match(func.apply(i));
            matchLast.right++;
            matchLast.occur = i;
        }
        for (SANode node : all) {
            if (node.fail != null) {
                node.fail.right += node.right;
                node.fail.occur = Math.min(node.fail.occur, node.occur);
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
        public int occur;

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