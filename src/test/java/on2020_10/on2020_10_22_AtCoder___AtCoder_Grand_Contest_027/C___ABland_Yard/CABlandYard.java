package on2020_10.on2020_10_22_AtCoder___AtCoder_Grand_Contest_027.C___ABland_Yard;



import template.io.FastInput;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CABlandYard {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].c = s[i] == 'A' ? 0 : 1;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        Deque<Node> dq = new ArrayDeque<>();
        for (Node node : nodes) {
            for (Node near : node.adj) {
                node.cnts[near.c]++;
            }
            if (node.cnts[0] == 0 || node.cnts[1] == 0) {
                dq.addLast(node);
                node.deleted = true;
            }
        }
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            for (Node node : head.adj) {
                if (node.deleted) {
                    continue;
                }
                node.cnts[head.c]--;
                if (node.cnts[head.c] == 0) {
                    dq.addLast(node);
                    node.deleted = true;
                }
            }
        }

        for (Node node : nodes) {
            if (!node.deleted) {
                out.println("Yes");
                return;
            }
        }
        out.println("No");
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int[] cnts = new int[2];
    int c;
    boolean deleted;
}