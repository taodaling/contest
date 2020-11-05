package template.string;

import template.primitve.generated.datastructure.IntToIntegerFunction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.TreeMap;

public class SparseSuffixAutomaton {
    public SANode root;
    public SANode buildLast;
    public SANode matchLast;
    public int matchLength;
    public List<SANode> all = new ArrayList<>();
    public boolean sorted = true;
    public long distinctSubstr = -1;

    public void enableDistinctSubstr() {
        distinctSubstr = 0;
    }

    public SparseSuffixAutomaton() {
        buildLast = root = newNode();
        root.fail = null;
    }

    private SANode newNode() {
        SANode ans = new SANode();
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
        int index = c;
        if (matchLast.next.containsKey(index)) {
            matchLast = matchLast.next.get(index);
            matchLength = matchLength + 1;
            return;
        }
        while (matchLast != null && !matchLast.next.containsKey(index)) {
            matchLast = matchLast.fail;
        }
        if (matchLast == null) {
            matchLast = root;
            matchLength = 0;
        } else {
            matchLength = matchLast.maxlen + 1;
            matchLast = matchLast.next.get(index);
        }
    }

    public void build(int c) {
        sorted = false;
        int index = c;
        SANode now = newNode();
        now.maxlen = buildLast.maxlen + 1;

        SANode p = visit(index, buildLast, null, now);
        if (p == null) {
            now.fail = root;
        } else {
            SANode q = p.next.get(index);
            if (q.maxlen == p.maxlen + 1) {
                now.fail = q;
            } else {
                SANode clone = cloneNode(q);
                clone.maxlen = p.maxlen + 1;
                now.fail = q.fail = clone;
                if (distinctSubstr != -1) {
                    distinctSubstr -= q.maxlen - clone.fail.maxlen;
                    distinctSubstr += q.maxlen - q.fail.maxlen;
                    distinctSubstr += clone.maxlen - clone.fail.maxlen;
                }
                visit(index, p, q, clone);
            }
        }
        if (distinctSubstr != -1) {
            distinctSubstr += now.maxlen - now.fail.maxlen;
        }
        buildLast = now;
    }

    public SANode visit(int index, SANode trace, SANode target, SANode replacement) {
        while (trace != null && trace.next.get(index) == target) {
            trace.next.put(index, replacement);
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
        public TreeMap<Integer, SANode> next = new TreeMap<>();
        public SANode fail;
        public int maxlen;
        public int right;
        public int indeg;

        public int minLength() {
            return fail == null ? 0 : fail.maxlen + 1;
        }

        public SANode() {
        }

        @Override
        public SANode clone() {
            try {
                SANode res = (SANode) super.clone();
                res.next = new TreeMap<>(res.next);
                return res;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}