package template.string;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ACAutomaton {
    private final int minCharacter;
    private final int maxCharacter;
    private final int RANGE;
    private Node root;
    private Node buildLast;
    private Node matchLast;
    private List<Node> allNodes = new ArrayList();

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
        Node node = new Node(RANGE);
        node.id = allNodes.size();
        allNodes.add(node);
        return node;
    }

    public ACAutomaton(int minCharacter, int maxCharacter) {
        this.minCharacter = minCharacter;
        this.maxCharacter = maxCharacter;
        RANGE = maxCharacter - minCharacter + 1;
        root = addNode();
    }

    public void beginBuilding() {
        buildLast = root;
    }

    public void endBuilding() {
        Deque<Node> deque = new ArrayDeque(allNodes.size());
        for (int i = 0; i < RANGE; i++) {
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
            head.preSum = head.cnt + head.fail.preSum;
            for (int i = 0; i < RANGE; i++) {
                if (head.next[i] != null) {
                    deque.addLast(head.next[i]);
                }
            }
        }

        for (int i = 0; i < RANGE; i++) {
            if (root.next[i] != null) {
                deque.addLast(root.next[i]);
            } else {
                root.next[i] = root;
            }
        }
        while (!deque.isEmpty()) {
            Node head = deque.removeFirst();
            for (int i = 0; i < RANGE; i++) {
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