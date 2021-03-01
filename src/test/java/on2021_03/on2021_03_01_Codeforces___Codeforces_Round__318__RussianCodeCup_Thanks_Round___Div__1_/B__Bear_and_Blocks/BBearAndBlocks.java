package on2021_03.on2021_03_01_Codeforces___Codeforces_Round__318__RussianCodeCup_Thanks_Round___Div__1_.B__Bear_and_Blocks;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class BBearAndBlocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                nodes[i].adj.add(nodes[i - 1]);
            }
            if (i + 1 < n) {
                nodes[i].adj.add(nodes[i + 1]);
            }
            nodes[i].dist = in.ri();
        }
        nodes[0].dist = nodes[n - 1].dist = 1;
        TreeSet<Node> set = new TreeSet<>(Comparator.<Node>comparingInt(x -> x.dist).thenComparingInt(x -> x.id));
        set.addAll(Arrays.asList(nodes));
        while (!set.isEmpty()) {
            Node head = set.pollFirst();
            for (Node node : head.adj) {
                if (node.dist > head.dist + 1) {
                    set.remove(node);
                    node.dist = head.dist + 1;
                    set.add(node);
                }
            }
        }
        int max = Arrays.stream(nodes).mapToInt(x -> x.dist).max().orElse(-1);
        out.println(max);
    }
}

class Node {
    List<Node> adj = new ArrayList<>(2);
    int dist;
    int id;
}
