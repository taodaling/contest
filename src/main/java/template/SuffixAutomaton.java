package template;

/**
 * Created by dalt on 2018/5/25.
 */
public class SuffixAutomaton {
    final int minCharacter;
    final int maxCharacter;
    final int alphabet;
    Node root;
    Node buildLast;
    Node matchLast;
    int matchLength;

    public int lengthMatch(){
        return matchLength;
    }

    public SuffixAutomaton(int minCharacter, int maxCharacter) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        alphabet = maxCharacter - minCharacter + 1;
        buildLast = root = new Node(alphabet);
        root.fail = null;
    }

    public void beginMatch() {
        matchLast = root;
        matchLength = 0;
    }

    public void match(char c) {
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

    public void build(char c) {
        int index = c - minCharacter;
        Node now = new Node(alphabet);
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
        Node[] next;
        Node fail;
        int maxlen;

        public Node(int alphabet){
            next = new Node[alphabet];
        }

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