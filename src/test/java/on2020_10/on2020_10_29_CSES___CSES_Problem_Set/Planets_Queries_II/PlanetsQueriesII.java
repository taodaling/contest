package on2020_10.on2020_10_29_CSES___CSES_Problem_Set.Planets_Queries_II;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlanetsQueriesII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            Node to = nodes[in.readInt() - 1];
            nodes[i].to = to;
            to.revAdj.add(nodes[i]);
        }
        for (Node node : nodes) {
            if (!findCircle(node)) {
                continue;
            }
            Node first = dq.peekFirst();
            int size = dq.size();
            int index = 0;
            for (Node x : dq) {
                x.instk = false;
                x.set = first;
                x.enter = x;
                x.index = index++;
                x.depth = 0;
                x.size = size;
                x.l = -1;
                x.r = -1;
            }
            for (Node x : dq) {
                for (Node y : x.revAdj) {
                    if (y.set != null) {
                        continue;
                    }
                    dfs(y, 1, x);
                }
            }

            dq.clear();
        }

        for (int i = 0; i < q; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            if (a.enter.set != b.enter.set) {
                out.println(-1);
                continue;
            }
            if (b.enter != b && !(b.l <= a.l && b.r >= a.r)) {
                out.println(-1);
                continue;
            }
            if (b.enter != b) {
                out.println(a.depth - b.depth);
                continue;
            }
            int ans = a.depth;
            int index = a.enter.index;
            int to = b.index;
            if (index > to) {
                ans += b.size - index;
                index = 0;
            }
            ans += b.index - index;
            out.println(ans);
        }
    }

    Deque<Node> dq = new ArrayDeque<>(200000);

    int order = 0;

    public void dfs(Node root, int depth, Node top) {
        root.enter = top;
        root.depth = depth;
        root.l = order++;
        for (Node node : root.revAdj) {
            dfs(node, depth + 1, top);
        }
        root.r = order - 1;
    }

    public boolean findCircle(Node root) {
        if (root.visited) {
            if (root.instk) {
                while (dq.peekFirst() != root) {
                    dq.removeFirst().instk = false;
                }
                return true;
            }
            return false;
        }
        root.visited = root.instk = true;
        dq.addLast(root);
        if (findCircle(root.to)) {
            return true;
        }
        root.instk = false;
        dq.removeLast();
        return false;
    }
}

class Node {
    Node to;
    List<Node> revAdj = new ArrayList<>();
    Node enter;
    Node set;
    int index;
    int depth;
    int size;
    boolean visited;
    boolean instk;
    int l;
    int r;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}