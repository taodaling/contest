package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__232__Div__1_.E__On_Iteration_of_One_Well_Known_Function;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class EOnIterationOfOneWellKnownFunction {
    public int log(int a, int b) {
        int ans = 0;
        while (b % a == 0) {
            b /= a;
            ans++;
        }
        return ans;
    }

    int lim = (int) 1e6;
    long now;

//    public int depth(Node root) {
//        if (root.depth == -1) {
//            root.depth = 0;
//            for (Node node : root.adj) {
//                root.depth = Math.max(node.depth + 1, root.depth);
//            }
//        }
//        return root.depth;
//    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        Node[] nodes = new Node[lim + 1];
        for (int i = 0; i <= lim; i++) {
            nodes[i] = new Node();
            nodes[i].val = i;
        }

        boolean[] isComp = new boolean[lim + 1];
        for (int i = 2; i <= lim; i++) {
            if (isComp[i]) {
                continue;
            }
            for (int j = i; j < lim; j += i) {
                isComp[j] = true;
                nodes[j + 1].adj.add(new Edge(nodes[i], log(i, j)));
            }
        }
        debug.elapse("build graph");
//        int max = 0;
//        for(Node node : nodes){
//            max = Math.max(max, depth(node));
//        }
//        debug.debug("max", max);

        for (int i = 0; i < m; i++) {
            int x = in.ri();
            nodes[x].pow = in.rl();
        }

        long k = in.rl();
        int bf = (int) Math.min(k, 11);
        k -= bf;
        for (int i = 0; i < bf; i++) {
            for (Node node : nodes) {
                if (node.pow > 0) {
                    node.pow--;
                    for (Edge e : node.adj) {
                        Node child = e.node;
                        child.pow += e.log;
                    }
                }
            }
        }
        debug.elapse("bf");
        for (int i = lim; i >= 0; i--) {
            if (nodes[i].pow > 0) {
                long cast = Math.min(nodes[i].pow, k);
                for (Edge e : nodes[i].adj) {
                    e.node.pow += cast * e.log;
                }
                nodes[i].pow -= cast;
            }
        }
        now = k;
        int cnt = 0;
        for (Node node : nodes) {
            if (node.pow == 0) {
                continue;
            }
            cnt++;
        }
        out.println(cnt);
        for (Node node : nodes) {
            if (node.pow == 0) {
                continue;
            }
            out.append(node.val).append(' ').append(node.pow).println();
        }
    }
}

class Edge {
    Node node;
    int log;

    public Edge(Node node, int log) {
        this.node = node;
        this.log = log;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    long pow;
    int val;
//    int depth = -1;

    @Override
    public String toString() {
        return "" + val;
    }
}