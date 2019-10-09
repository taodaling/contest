package template;

/**
 * Created by dalt on 2018/5/25.
 */
public class PalindromeAutomaton {
    static final int MIN_CHARACTER = 'a';
    static final int MAX_CHARACTER = 'z';
    static final int RANGE_SIZE = MAX_CHARACTER - MIN_CHARACTER + 1;

    Node odd;
    Node even;

    char[] data;
    int size;
    Node buildLast;

    public PalindromeAutomaton(int cap) {
        data = new char[cap];
        size = 0;

        odd = new Node();
        odd.len = -1;

        even = new Node();
        even.fail = odd;
        even.len = 0;

        buildLast = odd;
    }

    public void build(char c) {
        data[size++] = c;

        int index = c - MIN_CHARACTER;

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

        Node now = new Node();
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
        Node[] next = new Node[RANGE_SIZE];
        Node fail;
        int len;
    }
}