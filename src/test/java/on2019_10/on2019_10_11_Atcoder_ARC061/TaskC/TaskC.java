package on2019_10.on2019_10_11_Atcoder_ARC061.TaskC;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import template.FastInput;

public class TaskC {

    int inf = (int) 1e9;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            Integer op = in.readInt();
            a = a.getNode(op);
            b = b.getNode(op);
            a.next.add(b);
            b.next.add(a);
        }

        for (int i = 1; i <= n; i++) {
            nodes[i].dist = inf;
            for (Node node : nodes[i].map.values()) {
                node.dist = inf;
            }
        }

        nodes[1].dist = 0;
        TreeSet<Node> set = new TreeSet<>((a, b) -> (a.dist == b.dist ? a.id - b.id : a.dist - b.dist));
        set.add(nodes[1]);


        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Node node : head.next) {
                int dist = head.dist + head.charge;
                if (dist >= node.dist) {
                    continue;
                }
                set.remove(node);
                node.dist = dist;
                set.add(node);
            }
        }

        if (nodes[n].dist >= inf) {
            out.println(-1);
        } else {
            out.println(nodes[n].dist);
        }
    }
}


class Node {
    List<Node> next = new ArrayList<>();
    Map<Integer, Node> map = new HashMap<>();
    int dist;
    int charge = 1;

    public Node getNode(Integer key) {
        Node node = map.get(key);
        if (node == null) {
            node = new Node();
            next.add(node);
            node.next.add(this);
            node.charge = 0;
            map.put(key, node);
        }
        return node;
    }

    static int cnt = 0;
    int id;

    public Node() {
        id = cnt++;
    }
}
