package on2020_04.on2020_04_20_TopCoder_SRM__766.BannedBook;



import template.datastructure.DSU;

import java.util.ArrayList;
import java.util.List;

public class BannedBook {
    public int[] passAround(String[] friend) {
        int n = friend.length;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (friend[i].charAt(j) == 'N' || dsu.find(i) == dsu.find(j)) {
                    continue;
                }
                dsu.merge(i, j);
                nodes[i].next.add(nodes[j]);
                nodes[j].next.add(nodes[i]);
            }
        }

        for (int i = 0; i < n; i++) {
            if (nodes[i].visited) {
                continue;
            }
            dfs(nodes[i], null, 0);
        }
        return seq.stream().mapToInt(Integer::intValue).toArray();
    }

    List<Integer> seq = new ArrayList<>();

    public void dfs(Node root, Node p, int d) {
        root.visited = true;

        if (d % 2 == 0) {
            seq.add(root.id);
        }

        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root, d + 1);
        }

        if (d % 2 == 1) {
            seq.add(root.id);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<Node>();
    int id;
    boolean visited;
}