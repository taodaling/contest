package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class UOJ016 {

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.next.add(b);
            b.next.add(a);
        }
        for (int i = 0; i < n; i++) {
            nodes[i].w = in.readInt();
        }

        dfs(nodes[0], null);
        out.append(max).append(' ').append(sum % 10007);
    }

    long sum;
    long max;


    List<Node> pq = new ArrayList<>(2);

    void add(Node node) {
        if (pq.size() < 2) {
            pq.add(node);
        } else if (pq.get(0).w < node.w) {
            pq.set(0, node);
            if(pq.get(0).w > pq.get(1).w){
                SequenceUtils.swap(pq, 0, 1);
            }
        }
    }

    public void dfs(Node root, Node p) {
        long sum = 0;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
        pq.clear();
        for (Node node : root.next) {
            sum += node.w;
            add(node);
        }
        if (pq.size() != 2) {
            return;
        }
        debug.debug("root", root);
        debug.debug("pq", pq);
        max = Math.max(max, pq.get(0).w * pq.get(1).w);
        for (Node node : root.next) {
            this.sum += node.w * (sum - node.w);
        }
    }

}

class Node {
    List<Node> next = new ArrayList<>();
    int w;
    int size;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}