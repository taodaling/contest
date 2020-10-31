package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class PlanetsCycles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].to = nodes[in.readInt() - 1];
        }
        for (Node node : nodes) {
            if (!findCircle(node)) {
                continue;
            }
            for (Node x : dq) {
                x.dist = dq.size();
            }
            dq.clear();
        }
        for(int i = 0; i < n; i++){
            out.println(find(nodes[i]));
        }
    }

    public int find(Node root) {
        if (root.dist == -1) {
            root.dist = find(root.to) + 1;
        }
        return root.dist;
    }

    public boolean findCircle(Node root) {
        if (root.visited) {
            if (root.instk) {
                while (dq.peekFirst() != root) {
                    dq.removeFirst();
                }
                return true;
            }
            return false;
        }
        root.instk = root.visited = true;
        dq.addLast(root);

        if (findCircle(root.to)) {
            root.instk = false;
            return true;
        }

        dq.removeLast();
        root.instk = false;
        return false;
    }

    Deque<Node> dq = new ArrayDeque<>((int) 2e5);
}

class Node {
    Node to;
    boolean instk;
    boolean visited;
    int dist = -1;
}