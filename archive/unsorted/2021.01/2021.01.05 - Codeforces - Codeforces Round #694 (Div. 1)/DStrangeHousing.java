package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class DStrangeHousing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DSU dsu = new DSU(n);
        dsu.init();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        Deque<Node> activeCand = new ArrayDeque<>(n + m);
        Deque<Node> adjCand = new ArrayDeque<>(n + m);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            dsu.merge(a, b);
            nodes[a].adj.add(nodes[b]);
            nodes[b].adj.add(nodes[a]);
        }
        int cc = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.find(i) == i) {
                cc++;
            }
        }
        if (cc != 1) {
            out.println("NO");
            return;
        }
        out.println("YES");
        activeCand.add(nodes[0]);
        while (!activeCand.isEmpty()) {
            Node head = activeCand.removeFirst();
            if (head.actived || head.forbiden) {
                continue;
            }
            head.actived = true;
            for (Node node : head.adj) {
                if (node.forbiden) {
                    continue;
                }
                node.forbiden = true;
                adjCand.addLast(node);
            }
            while (!adjCand.isEmpty()) {
                Node adjHead = adjCand.removeFirst();
                for (Node node : adjHead.adj) {
                    if (!node.actived && !node.forbiden) {
                        activeCand.addLast(node);
                    }
                }
            }
        }

        int cnt = 0;
        for (Node node : nodes) {
            if (node.actived) {
                cnt++;
            }
        }
        out.println(cnt);
        for (Node node : nodes) {
            if (node.actived) {
                out.append(node.id + 1).append(' ');
            }
        }
        out.println();
    }
}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
    boolean actived;
    boolean forbiden;
}
