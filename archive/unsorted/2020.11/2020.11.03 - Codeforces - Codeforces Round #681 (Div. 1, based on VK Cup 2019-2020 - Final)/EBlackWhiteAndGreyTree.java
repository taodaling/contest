package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EBlackWhiteAndGreyTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].c = in.readInt();
        }
        for (int i = 0; i < n - 1; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            u.adj.add(v);
            v.adj.add(u);
        }

        int ans = solve(nodes, 0);
        ans = Math.min(ans, solve(nodes, 1));
        out.println(ans);
    }

    public int solve(Node[] nodes, int delta) {
        for (Node node : nodes) {
            node.deg = node.adj.size();
            node.removed = false;
        }
        Deque<Node>[] dq = new Deque[3];
        for (int i = 0; i < 3; i++) {
            dq[i] = new ArrayDeque<>();
        }
        for (Node node : nodes) {
            if (node.deg <= 1) {
                dq[node.c].addLast(node);
            }
        }
        int ans = 0;
        while (sizeOf(dq) > 0) {
            int avoid = ((ans & 1) ^ delta) == 0 ? 1 : 2;
            ans++;
            while (sizeOf(dq) - dq[avoid].size() > 0) {
                for (int i = 0; i < 3; i++) {
                    if (dq[i].isEmpty() || i == avoid) {
                        continue;
                    }
                    Node head = dq[i].removeFirst();
                    head.removed = true;
                    for (Node node : head.adj) {
                        node.deg--;
                        if (!node.removed && node.deg <= 1) {
                            dq[node.c].addLast(node);
                        }
                    }
                }
            }
        }
        return ans;
    }

    public int sizeOf(Deque<Node>[] dqs) {
        int sum = 0;
        for (Deque<Node> dq : dqs) {
            sum += dq.size();
        }
        return sum;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int c;
    int[] dp = new int[2];
    int deg;
    boolean removed;
}