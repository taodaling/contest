package on2020_05.on2020_05_28_Luogu.P5357_____AC__________;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
        ac.endBuilding();

        int m = in.readString(patterns, 0);
        ac.beginMatching();
        for (int i = 0; i < m; i++) {
            ac.match(patterns[i]);
            ac.getMatchLast().increaseCnt();
        }

        ac.pushUp();
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
    private List<Node> allNodes = new ArrayList();
    private List<Node> treeOrder;

    public Node getBuildLast() {
        return buildLast;
    }

    public Node getMatchLast() {
        return matchLast;
    }

    public List<Node> getAllNodes() {
        return allNodes;
    }

    private Node addNode() {
        Node node = new Node(range);
        node.id = allNodes.size();
        allNodes.add(node);
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

    public void endBuilding() {
        Deque<Node> deque = new ArrayDeque(allNodes.size());
        treeOrder = new ArrayList<>(allNodes.size());
        treeOrder.add(root);
        for (int i = 0; i < range; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            }
        }

        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            treeOrder.add(head);
            Node fail = visit(head.father.fail, head.index);
            if (fail == null) {
                head.fail = root;
            } else {
                head.fail = fail.next[head.index];
            }
            head.preSum = head.cnt + head.fail.preSum;
            for (int i = 0; i < range; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
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
            node.father = buildLast;
            node.index = index;
            buildLast.next[index] = node;
        }
        buildLast = buildLast.next[index];
    }

    public void pushUp() {
        for (int i = treeOrder.size() - 1; i >= 1; i--) {
            Node node = treeOrder.get(i);
            node.fail.cnt += node.cnt;
        }
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
        Node fail;
        Node father;
        int index;
        int id;
        int cnt;
        int preSum;

        public int getId() {
            return id;
        }

        public int getCnt() {
            return cnt;
        }

        public void decreaseCnt() {
            cnt--;
        }

        public void increaseCnt() {
            cnt++;
        }

        public int getPreSum() {
            return preSum;
        }

        public Node(int range) {
            next = new Node[range];
        }

        @Override
        public String toString() {
            return father == null ? "" : (father.toString() + (char) ('a' + index));
        }
    }
}