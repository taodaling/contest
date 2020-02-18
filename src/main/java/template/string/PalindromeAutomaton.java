package template.string;

/**
 * Created by dalt on 2018/5/25.
 */
public class PalindromeAutomaton {
    final int minCharacter;
    final int maxCharacter;
    int range;

    Node odd;
    Node even;

    char[] data;
    int size;
    Node buildLast;

    private Node newNode() {
        return new Node(range);
    }

    public PalindromeAutomaton(int minCharacter, int maxCharacter, int cap) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        range = maxCharacter - minCharacter + 1;

        data = new char[cap];
        size = 0;

        odd = newNode();
        odd.len = -1;

        even = newNode();
        even.fail = odd;
        even.len = 0;

        buildLast = odd;
    }

    public void build(char c) {
        data[size++] = c;

        int index = c - minCharacter;

        Node trace = buildLast;
        while (size - 2 - trace.len < 0) {
            trace = trace.fail;
        }

        while (data[size - trace.len - 2] != c) {
            trace = trace.fail;
        }

        if (trace.next[index] != null) {
            buildLast = trace.next[index];
            return;
        }

        Node now = newNode();
        now.len = trace.len + 2;
        trace.next[index] = now;

        if (now.len == 1) {
            now.fail = even;
        } else {
            trace = trace.fail;
            while (data[size - trace.len - 2] != c) {
                trace = trace.fail;
            }
            now.fail = trace.next[index];
        }

        buildLast = now;
    }

    public static class Node {
        public Node(int range) {
            next = new Node[range];
        }

        public Node[] next;
        public Node fail;
        public int len;
    }
}