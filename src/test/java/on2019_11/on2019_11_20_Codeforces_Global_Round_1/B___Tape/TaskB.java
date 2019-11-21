package on2019_11.on2019_11_20_Codeforces_Global_Round_1.B___Tape;



import template.FastInput;
import template.FastOutput;

import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].x = in.readInt();
        }

        Edge[] edges = new Edge[n - 1];
        for (int i = 0; i < n - 1; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[i];
            edges[i].b = nodes[i + 1];
            edges[i].length = nodes[i + 1].x - nodes[i].x;
        }

        Arrays.sort(edges, (a, b) -> Integer.compare(a.length, b.length));
        int cc = n;
        int ans = 0;
        for (Edge edge : edges) {
            if (cc <= k) {
                break;
            }
            if (edge.a.find() == edge.b.find()) {
                continue;
            }
            cc--;
            Node.merge(edge.a, edge.b);
            ans += edge.length;
        }
        ans += cc;
        out.println(ans);
    }
}

class Edge {
    Node a;
    Node b;
    int length;
}

class Node {
    Node p = this;
    int rank;
    int x;

    Node find() {
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
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }
}