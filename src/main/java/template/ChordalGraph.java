package template;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ChordalGraph {
    public static class Node {
        Set<Node> next = new LinkedHashSet();
        int id;
        int marked = -1;
        int rank;
        int color;

        Node prev;
        Node later;
    }

    private static class LinkedList {
        Node head;

        public boolean isEmpty() {
            return head == null;
        }

        public Node pollHead() {
            Node ret = head;
            head = head.later;
            if (head != null) {
                head.prev = null;
            }
            return ret;
        }

        public void removeNode(Node node) {
            if (node == head) {
                pollHead();
                return;
            }
            node.prev.later = node.later;
            if (node.later != null) {
                node.later.prev = node.prev;
            }
            node.prev = node.later = null;
        }

        public void add(Node node) {
            node.later = head;
            if (head != null) {
                head.prev = node;
            }
            head = node;
        }
    }

    private Node[] perfectRemoveSequence;
    private boolean isChordal;
    Node[] nodes;
    int n;

    public ChordalGraph(int n) {
        this.n = n;
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
    }

    public void addEdge(int a, int b) {
        if (perfectRemoveSequence != null) {
            throw new IllegalStateException();
        }
        nodes[a].next.add(nodes[b]);
        nodes[b].next.add(nodes[a]);
    }

    public boolean isChordal() {
        if (perfectRemoveSequence != null) {
            return isChordal;
        }
        perfectRemoveSequence = new Node[n];
        int perfectRemoveSequenceHead = n - 1;
        LinkedList[] lists = new LinkedList[n];
        int greatest = 0;
        for (int i = 0; i < n; i++) {
            lists[i] = new LinkedList();
        }
        for (Node node : nodes) {
            lists[0].add(node);
        }
        while (greatest >= 0) {
            if (lists[greatest].isEmpty()) {
                greatest--;
                continue;
            }
            Node head = lists[greatest].pollHead();
            head.marked = perfectRemoveSequenceHead;
            perfectRemoveSequence[perfectRemoveSequenceHead--] = head;
            for (Node next : head.next) {
                if (next.marked != -1) {
                    continue;
                }
                lists[next.rank].removeNode(next);
                next.rank++;
                greatest = Math.max(greatest, next.rank);
                lists[next.rank].add(next);
            }
        }
        for (Node node : perfectRemoveSequence) {
            Node minNode = null;
            for (Node next : node.next) {
                if (next.marked <= node.marked) {
                    continue;
                }
                if (minNode == null || minNode.marked > next.marked) {
                    minNode = next;
                }
            }
            if (minNode == null) {
                continue;
            }
            for (Node next : node.next) {
                if (next.marked <= node.marked || next == minNode) {
                    continue;
                }
                if (!minNode.next.contains(next)) {
                    isChordal = false;
                    return isChordal;
                }
            }
        }
        isChordal = true;
        return isChordal;
    }

    private int minColorCover = -1;

    /**
     * 获取最小染色数，处理后node.color表示染色方案，从0开始。
     */
    public int minColorCover() {
        if (minColorCover != -1) {
            return minColorCover;
        }
        if (!isChordal()) {
            throw new IllegalStateException();
        }
        boolean[] occupied = new boolean[1 + n];
        for (int i = 0; i < n; i++) {
            nodes[i].color = -1;
        }
        for (int i = n - 1; i >= 0; i--) {
            Node node = perfectRemoveSequence[i];
            Arrays.fill(occupied, 0, node.next.size() + 1, false);
            for (Node next : node.next) {
                if (next.color == -1) {
                    continue;
                }
                occupied[next.color] = true;
            }
            node.color = 0;
            while (occupied[node.color]) {
                node.color++;
            }
            minColorCover = Math.max(minColorCover, node.color);
        }
        minColorCover++;
        return minColorCover;
    }

    private Set<Node> maxIndependentSet;

    /**
     * 获取最大独立集
     */
    public Set<Node> maxIndependentSet() {
        if (maxIndependentSet != null) {
            return maxIndependentSet;
        }
        if (!isChordal()) {
            throw new IllegalStateException();
        }
        maxIndependentSet = new HashSet(n);
        for (int i = 0; i < n; i++) {
            Node node = perfectRemoveSequence[i];
            boolean flag = true;
            for (Node next : node.next) {
                if (maxIndependentSet.contains(next)) {
                    flag = false;
                    break;
                }
            }
            if (flag == true) {
                maxIndependentSet.add(node);
            }
        }
        return maxIndependentSet;
    }

    /**
     * 获取最小团覆盖，每个团中仅一个元素保存在结果中。对于每个返回集合中的元素e，其对应的团为e+N(e)，
     * N(e)表示与e相邻且在完美消除序列中编号大于e的顶点。
     */
    public Set<Node> minGroupCover() {
        return maxIndependentSet();
    }
}