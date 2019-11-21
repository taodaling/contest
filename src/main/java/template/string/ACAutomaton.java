package template.string;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Created by dalt on 2018/5/25.
 */
public class ACAutomaton {
    static final int MIN_CHARACTER = 'a';
    static final int MAX_CHARACTER = 'z';
    static final int RANGE_SIZE = MAX_CHARACTER - MIN_CHARACTER + 1;
    Node root;
    Node buildLast;
    Node matchLast;
    List<Node> allNodes = new ArrayList(1);

    private Node addNode() {
        Node node = new Node();
        node.id = allNodes.size();
        allNodes.add(node);
        return node;
    }

    public ACAutomaton() {
        root = addNode();
    }

    public void beginBuilding() {
        buildLast = root;
    }

    public void endBuilding() {
        Deque<Node> deque = new ArrayDeque(allNodes.size());
        for (int i = 0; i < RANGE_SIZE; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            }
        }

        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            Node fail = visit(head.father.fail, head.index);
            if (fail == null) {
                head.fail = root;
            } else {
                head.fail = fail.next[head.index];
            }
            head.word = head.word || head.fail.word;
            for (int i = 0; i < RANGE_SIZE; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
                }
            }
        }

        for (int i = 0; i < RANGE_SIZE; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            } else {
                root.next[i] = root;
            }
        }
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            for (int i = 0; i < RANGE_SIZE; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
                } else {
                    head.next[i] = head.fail.next[i];
                }
            }
        }
    }

    public Node visit(Node trace, int index) {
        while (trace != null && trace.next[index] == null) {
            trace = trace.fail;
        }
        return trace;
    }

    public void build(char c) {
        int index = c - MIN_CHARACTER;
        if (buildLast.next[index] == null) {
            Node node = addNode();
            node.father = buildLast;
            node.index = index;
            buildLast.next[index] = node;
        }
        buildLast = buildLast.next[index];
    }

    public void beginMatching() {
        matchLast = root;
    }

    public void match(char c) {
        int index = c - MIN_CHARACTER;
        matchLast = matchLast.next[index];
    }

    public static class Node {
        Node[] next = new Node[RANGE_SIZE];
        Node fail;
        Node father;
        int index;
        int id;
        boolean word;

        @Override
        public String toString() {
            return father == null ? "" : (father.toString() + (char) (MIN_CHARACTER + index));
        }
    }
}