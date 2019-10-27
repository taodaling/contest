package contest;

import template.ArrayUtils;
import template.FastInput;
import template.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.readInt()];
            p.next.add(nodes[i]);
        }
        dfs(nodes[0], 0);
        merge(nodes[0]);

        for (Node node = nodes[0]; node != null; node = node.queue.isEmpty() ? null : node.queue.first()) {
            out.append(node.id).append(' ');
        }
        out.println();
        ArrayUtils.reverse(seq, 0, seq.size());
        out.println(seq.size());
        for (Node node : seq) {
            out.append(node.id).append(' ');
        }
    }

    List<Node> seq = new ArrayList<>(100000);

    public void merge(Node root) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        if (root.queue.isEmpty()) {
            return;
        }
        Node min = root.queue.pollFirst();
        merge(min);
        while (!root.queue.isEmpty()) {
            Node deepest = root.queue.pollFirst();
            min.depthest++; 
            deepest.queue.add(min);
            seq.add(min);
            merge(deepest);
            min = deepest;
        }
        root.queue.add(min);
        root.depthest = min.depthest;
    }


    public void dfs(Node root, int depth) {
        root.depth = depth;
        root.depthest = depth;
        for (Node node : root.next) {
            dfs(node, depth + 1);
            root.depthest = Math.max(node.depthest, root.depthest);
            root.queue.add(node);
        }
    }
}

class Node {
    int id;
    int depth;
    int depthest;
    boolean visited;
    TreeSet<Node> queue = new TreeSet<>((a, b) -> a.depthest == b.depthest ? a.id - b.id : -(a.depthest - b.depthest));
    List<Node> next = new ArrayList<>();

    @Override
    public String toString() {
        return "" + id;
    }
}