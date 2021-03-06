package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.*;

public class P5357AC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] patterns = new char[(int) 2e6];
        ACAutomaton.Node[] nodes = new ACAutomaton.Node[n];
        ACAutomaton ac = new ACAutomaton('a', 'z');
        for (int i = 0; i < n; i++) {
            int m = in.readString(patterns, 0);
            ac.beginBuilding();
            for (int j = 0; j < m; j++) {
                ac.build(patterns[j]);
            }
            nodes[i] = ac.getBuildLast();
        }
        ac.endBuilding(true);
        int m = in.readString(patterns, 0);
        ac.beginMatching();
        for (int i = 0; i < m; i++) {
            ac.match(patterns[i]);
            ac.getMatchLast().cnt++;
        }
        List<ACAutomaton.Node> treeOrder = ac.getTreeOrder();
        for (int i = treeOrder.size() - 1; i >= 1; i--) {
            ACAutomaton.Node node = treeOrder.get(i);
            node.fail.cnt += node.cnt;
        }

        for (ACAutomaton.Node node : nodes) {
            out.println(node.cnt);
        }
    }
}


class ACAutomaton {
    private final int minCharacter;
    private final int maxCharacter;
    private final int range;
    private Node root;
    private Node buildLast;
    private Node matchLast;
    private List<Node> allNodes;
    private List<Node> treeOrder;
    private int cap;

    public void enableAllNodes(int exp) {
        allNodes = new ArrayList<>(exp);
    }

    public Node getBuildLast() {
        return buildLast;
    }

    public Node getMatchLast() {
        return matchLast;
    }

    public List<Node> getAllNodes() {
        return allNodes;
    }

    public Node getRoot() {
        return root;
    }

    private Node addNode() {
        Node node = new Node(range);
        node.id = cap++;
        if (allNodes != null) {
            allNodes.add(node);
        }
        return node;
    }

    public ACAutomaton(int minCharacter, int maxCharacter) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        range = maxCharacter - minCharacter + 1;
        root = addNode();
    }

    public void beginBuilding() {
        buildLast = root;
    }

    public List<ACAutomaton.Node> getTreeOrder() {
        return treeOrder;
    }

    public void endBuilding(boolean calcTreeOrder) {
        if (calcTreeOrder) {
            treeOrder = new ArrayList<>(cap);
            treeOrder.add(root);
        }
        Deque<Node> deque = new ArrayDeque<>();
        for (int i = 0; i < range; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
                root.next[i].fail = root;
            }
        }

        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            if (calcTreeOrder) {
                treeOrder.add(head);
            }

            for (int i = 0; i < range; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
                    Node fail = visit(head.fail, i);
                    if (fail == null) {
                        fail = root;
                    } else {
                        fail = fail.next[i];
                    }
                    head.next[i].fail = fail;
                }
            }
        }

        for (int i = 0; i < range; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            } else {
                root.next[i] = root;
            }
        }
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            for (int i = 0; i < range; i++) {
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
        int index = c - minCharacter;
        if (buildLast.next[index] == null) {
            Node node = addNode();
//            node.father = buildLast;
//            node.index = index;
            buildLast.next[index] = node;
        }
        buildLast = buildLast.next[index];
    }

    public void beginMatching() {
        matchLast = root;
    }

    public void match(char c) {
        int index = c - minCharacter;
        matchLast = matchLast.next[index];
    }

    public static class Node {
        public Node[] next;
        public Node fail;
        public int id;
        public int cnt;

        public int getId() {
            return id;
        }

        public Node(int range) {
            next = new Node[range];
        }

//        @Override
//        public String toString() {
//            return father == null ? "" : (father.toString() + (char) ('a' + index));
//        }
    }
}