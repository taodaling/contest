package on2020_08.on2020_08_31_Codeforces___AIM_Tech_Round_3__Div__1_.C__Centroids;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class CCentroids {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        dfsForSize(nodes[0], null, n);
        dfs(nodes[0], null, n, 0);
        for(Node node : nodes){
            out.append(node.possible ? 1 : 0).append(' ');
        }
    }

    public void dfs(Node root, Node p, int n, int noMoreThan) {
        Node maxChild = p;
        int childSize = n - root.size;
        int m = root.adj.size();
        int[] pre = new int[m];
        int[] post = new int[m];

        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            if (childSize < node.size) {
                childSize = node.size;
                maxChild = node;
            }
        }

        int childNoMoreThan = maxChild == p ? noMoreThan : maxChild.noMoreThan;
        if ((childSize - childNoMoreThan) * 2 <= n) {
            root.possible = true;
        }


        for (int i = 0; i < m; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                pre[i] = post[i] = noMoreThan;
            } else {
                pre[i] = post[i] = node.noMoreThan;
            }
        }
        for (int i = 1; i < m; i++) {
            pre[i] = Math.max(pre[i], pre[i - 1]);
            post[m - 1 - i] = Math.max(post[m - 1 - i], post[m - i]);
        }

        for (int i = 0; i < m; i++) {
            Node node = root.adj.get(i);
            if (node == p) {
                continue;
            }
            int top = 0;
            if (i > 0) {
                top = Math.max(top, pre[i - 1]);
            }
            if (i + 1 < m) {
                top = Math.max(top, post[i + 1]);
            }
            if ((n - node.size) * 2 <= n) {
                top = Math.max(top, n - node.size);
            }
            dfs(node, root, n, top);
        }
    }

    public void dfsForSize(Node root, Node p, int n) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root, n);
            root.size += node.size;
            root.noMoreThan = Math.max(root.noMoreThan, node.noMoreThan);
        }
        if (root.size * 2 <= n) {
            root.noMoreThan = Math.max(root.noMoreThan, root.size);
        }
    }
}

class Node {
    boolean possible;
    int size;
    int noMoreThan;
    List<Node> adj = new ArrayList<>();
}
