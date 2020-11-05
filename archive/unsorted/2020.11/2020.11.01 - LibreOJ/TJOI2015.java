package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.string.SuffixArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TJOI2015 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
        for (int i = 0; i < n; i++) {
            sa.build(s[i]);
        }
        int t = in.readInt();
        int k = in.readInt();

        sa.calcRight(i -> s[i], n);
        prepare(sa.root);
        SuffixAutomaton.SANode cur = sa.root;
        while (k > 0) {
            for (int i = 0; i < cur.next.length; i++) {
                SuffixAutomaton.SANode node = cur.next[i];
                if (node == null) {
                    continue;
                }
                if (node.way[t] < k) {
                    k -= node.way[t];
                } else {
                    k -= t == 0 ? 1 : node.right;
                    out.append((char) (i + 'a'));
                    cur = node;
                    break;
                }
            }
        }
    }



    public void prepare(SuffixAutomaton.SANode root) {
        if(root.way != null){
            return;
        }
        root.way = new long[2];
        root.way[0] = 1;
        root.way[1] = root.right;
        for (SuffixAutomaton.SANode node : root.next) {
            if (node == null) {
                continue;
            }
            prepare(node);
            root.way[0] += node.way[0];
            root.way[1] += node.way[1];
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
    public List<SANode> all = new ArrayList<>();
    public boolean sorted = true;

    public long realTimeDistinctSubstr = -1;

    public void enableDistinctSubstr() {
        realTimeDistinctSubstr = 0;
    }

    public SuffixAutomaton(int minCharacter, int maxCharacter) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
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
        public SANode fail;
        public int maxlen;
        public int right;
        public int indeg;
        public long[] way;

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