package template.graph;

import template.utils.IntComparator;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;

/**
 * 笛卡尔树，中序遍历为下标递增，根元素为最小元素（如果有多个，下标较小的作为根）
 */
public class DescartesTree {
    private Node root;
    private Node[] nodes;

    public <T> DescartesTree(T[] data, int l, int r, Comparator<T> comp) {
        int len = r - l + 1;
        nodes = new Node[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = new Node();
            nodes[i].index = i + l;
        }
        Deque<Node> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comp.compare(data[deque.peekLast().index], data[nodes[i].index]) > 0) {
                Node tail = deque.removeLast();
                tail.right = nodes[i].left;
                nodes[i].left = tail;
            }
            deque.addLast(nodes[i]);
        }
        while (deque.size() > 1) {
            Node tail = deque.removeLast();
            deque.peekLast().right = tail;
        }
        root = deque.removeLast();
    }

    public DescartesTree(int[] data, int l, int r, IntComparator comparator) {
        int len = r - l + 1;
        nodes = new Node[len];
        for (int i = 0; i < len; i++) {
            nodes[i] = new Node();
            nodes[i].index = i + l;
        }
        Deque<Node> deque = new ArrayDeque<>(len);
        for (int i = 0; i < len; i++) {
            while (!deque.isEmpty() && comparator.compare(data[deque.peekLast().index], data[nodes[i].index]) > 0) {
                Node tail = deque.removeLast();
                tail.right = nodes[i].left;
                nodes[i].left = tail;
            }
            deque.addLast(nodes[i]);
        }
        while (deque.size() > 1) {
            Node tail = deque.removeLast();
            deque.peekLast().right = tail;
        }
        root = deque.removeLast();
    }

    public Node getRoot() {
        return root;
    }

    public static class Node {
        public int index;
        public Node left;
        public Node right;

        @Override
        public String toString() {
            return "" + index;
        }
    }
}
