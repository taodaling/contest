package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Bad_Triplet;



import template.graph.EdgeMap;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BadTriplet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        EdgeMap map = new EdgeMap(m);
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            if (map.exist(a.id, b.id) || a == b) {
                continue;
            }
            map.add(a.id, b.id, 1);
            a.adj.add(b);
            b.adj.add(a);
        }
        List<Node> ans = null;
        for (int i = 0; i < n; i++) {
            if (nodes[i].adj.size() >= 3) {
                //cool
                Node a = nodes[i].adj.get(0);
                Node b = nodes[i].adj.get(1);
                Node c = nodes[i].adj.get(2);
                //ab
                if (a.adj.contains(b)) {
                    ans = Arrays.asList(nodes[i], a, b);
                } else if (a.adj.contains(c)) {
                    ans = Arrays.asList(nodes[i], a, c);
                } else if (b.adj.contains(c)) {
                    ans = Arrays.asList(nodes[i], b, c);
                } else {
                    ans = Arrays.asList(a, b, c);
                }
                break;
            }
        }
        if (ans == null) {
            int minDeg = Arrays.stream(nodes).mapToInt(x -> x.adj.size())
                    .min().orElse(-1);
            if (minDeg < 2) {
                out.println(-1);
                return;
            }
            if (n % 3 != 0) {
                out.println(-1);
                return;
            }
            List<Node> circle = new ArrayList<>(n);
            Node prev = nodes[0].adj.get(0);
            Node next = nodes[0];
            while (circle.size() < n) {
                circle.add(next);
                Node go = null;
                for (Node node : next.adj) {
                    if (node == prev) {
                        continue;
                    }
                    go = node;
                }
                prev = next;
                next = go;
            }
            ans = Arrays.asList(circle.get(0), circle.get(n / 3), circle.get(n / 3 * 2));
        }

        for(Node node : ans){
            out.println(node.id + 1);
        }
    }


}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
}

