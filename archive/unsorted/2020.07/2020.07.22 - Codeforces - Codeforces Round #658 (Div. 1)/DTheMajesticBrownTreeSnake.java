package contest;

import template.datastructure.FixedMinCollection;
import template.datastructure.FixedMinHeap;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DTheMajesticBrownTreeSnake {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        Node start = nodes[in.readInt() - 1];
        Node end = nodes[in.readInt() - 1];
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }


        List<Node> trace = new ArrayList<>(n);
        find(start, null, end, trace);
        debug.debug("trace", trace);
        snake = trace.size();
        dfs(nodes[0], null);
        dfs2(nodes[0], null, 0);
        for (Node node : trace) {
            node.snake = true;
        }
        for (Node node : trace) {
            dfs3(node, null);
        }
        for (int i = 0; i < trace.size(); i++) {
            trace.get(i).index = i;
        }

        Deque<Node> leftEvents = new ArrayDeque<>(n);
        Deque<Node> rightEvents = new ArrayDeque<>(n);
        leftEvents.add(start);
        rightEvents.add(end);
        int l = 0;
        int r = snake - 1;
        boolean match = trace.get(l).cnt >= 3 || trace.get(r).cnt >= 3;
        while (!match && leftEvents.size() + rightEvents.size() > 0) {
            if (leftEvents.size() > 0) {
                Node head = leftEvents.removeFirst();
                int index = Math.max(head.index, head.index + (snake - head.deepest - 1));
                while (r > index) {
                    r--;
                    rightEvents.add(trace.get(r));
                    match = match || trace.get(r).cnt >= 3;
                }
            } else {
                Node head = rightEvents.removeFirst();
                int index = Math.min(head.index, head.index - (snake - head.deepest - 1));
                while (l < index) {
                    l++;
                    leftEvents.add(trace.get(l));
                    match = match || trace.get(l).cnt >= 3;
                }
            }
        }

        out.println(match ? "YES" : "NO");
    }

    public void dfs3(Node root, Node p) {
        root.deepest = 0;
        for (Node node : root.adj) {
            if (node == p || node.snake) {
                continue;
            }
            dfs3(node, root);
            root.deepest = Math.max(root.deepest, node.deepest + 1);
            root.cnt = Math.max(root.cnt, node.cnt);
        }
    }

    public boolean find(Node root, Node p, Node target, List<Node> trace) {
        trace.add(root);
        if (root == target) {
            return true;
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (find(node, root, target, trace)) {
                return true;
            }
        }
        trace.remove(trace.size() - 1);
        return false;
    }


    int snake;

    int inf = (int) 1e8;

    public void dfs(Node root, Node p) {
        root.pq.fill(-inf, 3);
        root.pq.add(0);
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            root.pq.add(node.pq.getKthSmallest(0) + 1);
        }
    }

    public void dfs2(Node root, Node p, int farthest) {
        root.pq.add(farthest);

        for (int dist : root.pq) {
            if (dist + 1 >= snake) {
                root.cnt++;
            }
        }

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            int f = farthest;
            int cand = root.pq.getKthSmallest(0) == node.pq.getKthSmallest(0) + 1 ?
                    root.pq.getKthSmallest(1) : root.pq.getKthSmallest(0);
            dfs2(node, root, Math.max(f, cand) + 1);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    FixedMinHeap<Integer> pq = new FixedMinHeap<>(3, (a, b) -> -a.compareTo(b));
    int id;
    int deepest;
    int cnt;
    boolean snake;
    int index;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
