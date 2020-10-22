package on2020_10.on2020_10_20_AtCoder___AtCoder_Beginner_Contest_175.F___Making_Palindrome;



import template.io.FastInput;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FMakingPalindrome {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        ss = new String[n];
        long[] costs = new long[n];

        long inf = (long) 1e18;
        long ans = inf;

        for (int i = 0; i < n; i++) {
            ss[i] = in.readString();
            costs[i] = in.readInt();
        }
        prefNode = new Node[n][];
        suffixNode = new Node[n][];
        for (int i = 0; i < n; i++) {
            int m = ss[i].length();
            prefNode[i] = new Node[m];
            suffixNode[i] = new Node[m];
            for (int j = 0; j < m; j++) {
                prefNode[i][j] = newNode();
                suffixNode[i][j] = newNode();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < ss[i].length(); j++) {
                if (palindrome(ss[i], 0, j)) {
                    prefNode[i][j].adj.add(new Edge(end, 0));
                }
                if (palindrome(ss[i], j, ss[i].length() - 1)) {
                    suffixNode[i][j].adj.add(new Edge(end, 0));
                }
                for (int k = 0; k < n; k++) {
                    //left match
                    Node pMatch = match(i, j, k, ss[k].length() - 1);
                    if (pMatch != null) {
                        suffixNode[i][j].adj.add(new Edge(pMatch, costs[k]));
                    }
                    Node sMatch = match(k, 0, i, j);
                    if (sMatch != null) {
                        prefNode[i][j].adj.add(new Edge(sMatch, costs[k]));
                    }
                }
            }
        }


        for (Node node : nodes) {
            node.dist = inf;
        }
        for (int i = 0; i < n; i++) {
            suffixNode[i][0].dist = costs[i];
        }
        for (int i = 0; i < nodes.size(); i++) {
            Node head = null;
            for (Node node : nodes) {
                if (node.handled) {
                    continue;
                }
                if (head == null || node.dist < head.dist) {
                    head = node;
                }
            }
            head.handled = true;
            for (Edge e : head.adj) {
                if (e.to.dist > head.dist + e.w) {
                    e.to.dist = head.dist + e.w;
                }
            }
        }

        ans = Math.min(ans, end.dist);
        if (ans == inf) {
            out.println(-1);
            return;
        }
        out.println(ans);
    }

    List<Node> nodes = new ArrayList<>();
    Node[][] prefNode;
    Node[][] suffixNode;
    Node end = newNode();
    String[] ss;

    public Node match(int i, int l, int j, int r) {
        while (l < ss[i].length() && r >= 0) {
            if (ss[i].charAt(l) != ss[j].charAt(r)) {
                return null;
            }
            l++;
            r--;
        }
        if (l >= ss[i].length() && r < 0) {
            return end;
        }
        if (l < ss[i].length()) {
            return suffixNode[i][l];
        }
        return prefNode[j][r];
    }


    public boolean palindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    public Node newNode() {
        Node node = new Node();
        nodes.add(node);
        return node;
    }
}

class Edge {
    Node to;
    long w;

    public Edge(Node to, long w) {
        this.to = to;
        this.w = w;
    }
}

class Node {
    boolean handled = false;
    List<Edge> adj = new ArrayList<>();
    long dist;
}
