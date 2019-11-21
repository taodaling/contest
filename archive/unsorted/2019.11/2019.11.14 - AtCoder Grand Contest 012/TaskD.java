package contest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import template.CollectionUtils;
import template.CompareUtils;
import template.FastInput;
import template.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(1e9 + 7);
        Factorial fact = new Factorial(200000, mod);
        int n = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        if (n == 1) {
            out.println(1);
            return;
        }

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].c = in.readInt();
            nodes[i].w = in.readInt();
            nodes[i].colorCnts.put(nodes[i].c, 1);
        }

        Node min = CompareUtils.minOf(nodes, 0, n - 1, (a, b) -> Integer.compare(a.w, b.w));
        Node second = Arrays.stream(nodes).filter((a) -> a.c != min.c).min((a, b) -> Integer.compare(a.w, b.w))
                        .orElse(min);


        Map<Integer, List<Node>> map = Arrays.stream(nodes).collect(Collectors.groupingBy(a -> a.c));
        for (List<Node> nodeList : map.values()) {
            nodeList.sort((a, b) -> Integer.compare(a.w, b.w));
            Node head = nodeList.get(0);
            for (Node node : nodeList) {
                if (head.w + node.w <= x) {
                    Node.merge(node, head);
                }
                if (node.c != min.c && node.w + min.w <= y) {
                    Node.merge(node, min);
                }
                if (node.c != second.c && node.w + second.w <= y) {
                    Node.merge(node, second);
                }
            }
        }

        int ans = 1;
        for (Node node : nodes) {
            if (node.find() != node) {
                continue;
            }
            int local = fact.fact(node.size);
            for(int cnt : node.colorCnts.values()){
                local = mod.mul(local, fact.invFact(cnt));
            }
            ans = mod.mul(ans, local);
        }

        out.println(ans);
    }
}


class Node {
    int c;
    int w;

    Node p = this;
    int size = 1;
    int rank;
    int id;

    Map<Integer, Integer> colorCnts = new HashMap<>(1);


    @Override
    public String toString() {
        return "" + id;
    }

    public Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
        a.size += b.size;
        a.colorCnts = CollectionUtils.mergeCountMapHeuristically(a.colorCnts, b.colorCnts);
    }
}
