package template;

/**
 * Created by dalt on 2018/5/25.
 */
public class SuffixAutomaton {
    static final int MIN_CHARACTER = 'a';
    static final int MAX_CHARACTER = 'z';
    static final int RANGE_SIZE = MAX_CHARACTER - MIN_CHARACTER + 1;
    Node root;
    Node buildLast;
    Node matchLast;
    int matchLength;

    public SuffixAutomaton() {
        buildLast = root = new Node();
        root.fail = null;
    }

    public void beginMatch() {
        matchLast = root;
        matchLength = 0;
    }

    public void match(char c) {
        int index = c - MIN_CHARACTER;
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

    public void build(char c) {
        int index = c - MIN_CHARACTER;
        Node now = new Node();
        now.maxlen = buildLast.maxlen + 1;

        Node p = visit(index, buildLast, null, now);
        if (p == null) {
            now.fail = root;
        } else {
            Node q = p.next[index];
            if (q.maxlen == p.maxlen + 1) {
                now.fail = q;
            } else {
                Node clone = q.clone();
                clone.maxlen = p.maxlen + 1;

                now.fail = q.fail = clone;
                visit(index, p, q, clone);
            }
        }

        buildLast = now;
    }

    public Node visit(int index, Node trace, Node target, Node replacement) {
        while (trace != null && trace.next[index] == target) {
            trace.next[index] = replacement;
            trace = trace.fail;
        }
        return trace;
    }

    public static class Node implements Cloneable {
        Node[] next = new Node[RANGE_SIZE];
        Node fail;
        int maxlen;

        @Override
        public Node clone() {
            try {
                Node res = (Node) super.clone();
                res.next = res.next.clone();
                return res;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}