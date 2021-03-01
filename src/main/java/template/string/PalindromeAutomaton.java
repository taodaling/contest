package template.string;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    public Node buildLast;

    public List<Node> all;

    long palindromeSubstringCnt = 0;

    private Node newNode() {
        Node ans = new Node(range);
        all.add(ans);
        return ans;
    }

    public PalindromeAutomaton(int minCharacter, int maxCharacter, int cap) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        range = maxCharacter - minCharacter + 1;
        all = new ArrayList<>(2 + cap);

        data = new char[cap];
        size = 0;

        odd = newNode();
        odd.len = -1;
        odd.depth = 0;

        even = newNode();
        even.fail = odd;
        even.len = 0;
        even.depth = 0;

        all.clear();
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
        } else {
            Node now = newNode();
            now.len = trace.len + 2;
            now.firstOccurRightIndex = size - 1;
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
            now.depth = now.fail.depth + 1;
            buildLast = now;
        }
        buildLast.occurTime++;
        palindromeSubstringCnt += buildLast.depth;
    }

    public void endBuild() {
        for (int i = all.size() - 1; i >= 0; i--) {
            Node node = all.get(i);
            if (node.fail != null) {
                node.fail.occurTime += node.occurTime;
            }
        }
    }


    public void visit(Consumer<Node> consumer) {
        for (int i = all.size() - 1; i >= 0; i--) {
            consumer.accept(all.get(i));
        }
    }

    public long getPalindromeSubstringCnt() {
        return palindromeSubstringCnt;
    }

    public int distinctPalindromeSubstring() {
        return all.size();
    }

    public static class Node {
        public Node(int range) {
            next = new Node[range];
        }

        public Node[] next;
        public Node fail;
        public int len;
        public int occurTime;
        public int firstOccurRightIndex;
        public int depth;
    }
}