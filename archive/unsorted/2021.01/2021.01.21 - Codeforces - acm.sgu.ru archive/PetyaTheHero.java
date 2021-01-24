package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PetyaTheHero {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String a = in.rs();
        String b = in.rs();
        PalindromeAutomaton pa = new PalindromeAutomaton('a', 'z' + 2, a.length() + b.length() + 2);
        for (char c : a.toCharArray()) {
            pa.build(c);
        }
        pa.endBuild();
        pa.visitAll(node -> {
            node.oldTotalOccur = node.totalOccur;
            node.totalOccur = 0;
        });
        pa.build((char) ('z' + 1));
        pa.build((char) ('z' + 2));
        for (char c : b.toCharArray()) {
            pa.build(c);
        }

        pa.endBuild();
        BestVisitor visitor = new BestVisitor();
        pa.visitAll(visitor);
        PalindromeAutomaton.Node best = visitor.best;
        assert best != null;
        int l = best.firstOccurRightIndex - best.len + 1;
        int r = best.firstOccurRightIndex;
        out.println(a.substring(l, r + 1));
    }
}

class BestVisitor implements Consumer<PalindromeAutomaton.Node> {
    PalindromeAutomaton.Node best;

    @Override
    public void accept(PalindromeAutomaton.Node node) {
        if (node.oldTotalOccur == node.totalOccur || node.oldTotalOccur == 0) {
            return;
        }
        if (best == null || node.len > best.len) {
            best = node;
        }
    }
}

class PalindromeAutomaton {
    final int minCharacter;
    final int maxCharacter;
    int range;

    Node odd;
    Node even;

    char[] data;
    int size;
    Node buildLast;

    List<Node> all;

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

            buildLast = now;
        }
        buildLast.occurTime++;
    }

    public void endBuild() {
        for (int i = all.size() - 1; i >= 0; i--) {
            Node node = all.get(i);
            node.totalOccur += node.occurTime;
            if (node.fail != null) {
                node.fail.totalOccur += node.totalOccur;
            }
        }
    }


    public void visitAll(Consumer<Node> consumer) {
        for (int i = all.size() - 1; i >= 2; i--) {
            consumer.accept(all.get(i));
        }
    }


    public static class Node {
        public Node(int range) {
            next = new Node[range];
        }

        public Node[] next;
        public Node fail;
        public int len;
        public int occurTime;
        public int totalOccur;
        public int firstOccurRightIndex;
        public int oldTotalOccur;
    }
}