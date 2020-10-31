package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class FRemovingRobots {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(in.readInt(), in.readInt());
        }
        Arrays.sort(nodes, (a, b) -> Integer.compare(a.x, b.x));
        Deque<Node> dq = new ArrayDeque<>();
        for (int i = n - 1; i >= 0; i--) {
            while (!dq.isEmpty() && dq.peekFirst().x < nodes[i].x + nodes[i].d) {
                Node head = dq.removeFirst();
                nodes[i].adj.add(head);
            }
            dq.addFirst(nodes[i]);
        }
        long ans = 1;
        for (Node node : dq) {
            ans = ans * dp(node) % mod;
        }
        out.println(ans);
    }

    int mod = 998244353;

    public long dp(Node root) {
        long ans = 1;
        for (Node node : root.adj) {
            ans = ans * dp(node) % mod;
        }
        ans++;
        ans %= mod;
        return ans;
    }
}


class Node {
    int x;
    int d;
    List<Node> adj = new ArrayList<>();

    public Node(int x, int d) {
        this.x = x;
        this.d = d;
    }
}
