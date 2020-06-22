package contest;

import template.algo.IntBinarySearch;
import template.algo.LongBinarySearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class AqaAsadiSaves {
    public long minDamage(int N, int M, int[] PA, int[] PB, int Seed, int X, int Y) {
        int[] A = Arrays.copyOf(PA, M);
        int[] B = Arrays.copyOf(PB, M);
        for (int i = PA.length; i <= M - 1; i++) {
            A[i] = Seed;
            Seed = (int) (((long) Seed * X + Y) % N);
        }
        for (int i = PB.length; i <= M - 1; i++) {
            B[i] = Seed;
            Seed = (int) (((long) Seed * X + Y) % N);
        }
        Node[] nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < M; i++) {
            Edge e = new Edge();
            e.a = nodes[A[i]];
            e.b = nodes[B[i]];
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        dq = new ArrayDeque<>(N);
        for (int i = 0; i < N; i++) {
            tarjan(nodes[i], null);
        }

        List<Node> set = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            if (nodes[i].set == nodes[i]) {
                set.add(nodes[i]);
                nodes[i].adj.clear();
            }
        }

        for (int i = 0; i < M; i++) {
            if (nodes[A[i]].set == nodes[B[i]].set) {
                continue;
            }
            Edge e = new Edge();
            e.a = nodes[A[i]].set;
            e.b = nodes[B[i]].set;
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        for (Node node : set) {
            if (node.visited) {
                continue;
            }
            dfs(node, null);
            calc(node, null, node.subtreeSize);
        }

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                for (Node node : set) {
                    node.visited = false;
                    node.need = 0;
                    node.single = 0;
                }
                int ans = 0;
                for (Node node : set) {
                    if (node.visited) {
                        continue;
                    }
                    dp(node, null, mid);
                    ans += node.need + node.single;
                }
                return ans <= 1;
            }
        };

        long ans = lbs.binarySearch(0, (long) 1e18);
        return ans;
    }

    public void dp(Node root, Edge p, long w) {
        root.visited = true;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dp(node, e, w);
            if (e.w > w) {
                node.single = 1;
            }
            root.need += node.need;
            root.single += node.single;
        }
        root.need += root.single / 2;
        root.single %= 2;
    }

    Deque<Node> dq;
    int order = 0;

    public void calc(Node root, Edge p, int total) {
        if (p != null) {
            p.w = (long) (total - root.subtreeSize) * root.subtreeSize;
        }
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            calc(node, e, total);
        }
    }

    public void dfs(Node root, Edge p) {
        root.visited = true;
        root.subtreeSize = root.weight;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfs(node, e);
            root.subtreeSize += node.subtreeSize;
        }
    }

    public void tarjan(Node root, Edge p) {
        if (root.dfn != -1) {
            return;
        }
        root.low = root.dfn = order++;
        root.instk = true;
        dq.addLast(root);
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            tarjan(node, e);
            if (node.instk) {
                root.low = Math.min(root.low, node.low);
            }
        }
        if (root.low == root.dfn) {
            while (true) {
                Node tail = dq.removeLast();
                tail.set = root;
                tail.set.weight++;
                tail.instk = false;
                if (tail == root) {
                    break;
                }
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    long w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    Node set;
    int dfn = -1;
    int low = -1;
    boolean instk;
    int weight;
    int subtreeSize;
    boolean visited;

    int need;
    int single;

    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}