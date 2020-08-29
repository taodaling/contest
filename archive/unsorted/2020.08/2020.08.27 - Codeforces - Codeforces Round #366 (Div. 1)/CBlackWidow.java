package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class CBlackWidow {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int n = in.readInt();
        IntegerArrayList[] values = new IntegerArrayList[n];
        IntegerArrayList[] occurs = new IntegerArrayList[n];
        for (int i = 0; i < n; i++) {
            occurs[i] = new IntegerArrayList(2);
            values[i] = new IntegerArrayList(2);
        }
        for (int i = 0; i < m; i++) {
            int k = in.readInt();
            for (int j = 0; j < k; j++) {
                int x = in.readInt();
                int id = Math.abs(x) - 1;
                occurs[id].add(i);
                values[id].add(x);
            }
        }

        Node[] nodes = new Node[m];
        for (int i = 0; i < m; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        long extra = 1;
        for (int i = 0; i < n; i++) {
            IntegerArrayList list = occurs[i];
            if (list.size() == 2) {
                int a = list.get(0);
                int b = list.get(1);
                addEdge(nodes[a], nodes[b], values[i].get(0) == values[i].get(1) ? 0 : 1);
                continue;
            }
            if (list.size() == 1) {
                int a = list.get(0);
                nodes[a].virtual++;
                continue;
            }
            if (list.size() == 0) {
                extra = extra * 2 % mod;
            }
        }

        dq = new ArrayList<>();
        List<long[]> data = new ArrayList<>();
        for (Node node : nodes) {
            if (node.visited) {
                continue;
            }
            dq.clear();
            Node leaf = findLeaf(node, null);
            find(leaf, null);

            Node first = dq.get(0);
            Node tail = dq.get(dq.size() - 1);
            sum = new long[2];

            if (tail.next != null) {
                //circle
                if (tail.next.xor == 0) {
                    first.light = tail.light = 1;
                    bf(0);
                    first.light = tail.light = 0;
                    bf(0);
                } else {
                    first.light = 1;
                    bf(0);
                    first.light = 0;
                    tail.light = 1;
                    bf(0);
                }
                first.light = tail.light = 0;
            } else {
                bf(0);
            }

            data.add(sum);
        }


        long[] last = new long[]{1, 0};
        long[] next = new long[2];
        for (long[] d : data) {
            Arrays.fill(next, 0);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    next[i ^ j] += last[i] * d[j] % mod;
                }
            }
            for (int i = 0; i < 2; i++) {
                next[i] %= mod;
            }
            long[] tmp = last;
            last = next;
            next = tmp;
        }

        long ans = last[1] * extra % mod;
        out.println(ans);
    }

    List<Node> dq;
    long[] sum;

    public void bf(int i) {
        if (i >= dq.size()) {
            add(sum, dp(), 0);
            return;
        }
        Node node = dq.get(i);
        int nodeVirtual = node.virtual;
        int light = node.light;
        if (node.virtual > 0) {
            node.virtual--;
            bf(i);
            node.light = 1;
            bf(i);
        } else {
            bf(i + 1);
        }

        node.virtual = nodeVirtual;
        node.light = light;
    }

    public long[] dp() {
        long[][] last = new long[2][2];
        long[][] next = new long[2][2];
        Node first = dq.get(0);
        last[first.light][first.light] = 1;
        for (int i = 1; i < dq.size(); i++) {
            Node node = dq.get(i);
            SequenceUtils.deepFill(next, 0L);
            Edge e = dq.get(i - 1).next;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    long way = last[j][k];
                    if (way == 0) {
                        continue;
                    }
                    if (e.xor == 1) {
                        for (int t = 0; t < 2; t++) {
                            int diff = 0;
                            if (t == 0 && k == 0) {
                                diff = 1;
                            }
                            int go = Math.max(t, node.light);
                            diff ^= go;
                            next[j ^ diff][go] += way;
                        }
                    } else {
                        for (int t = 0; t < 2; t++) {
                            int diff = 0;
                            if (t == 0) {
                                diff = node.light;
                            } else {
                                if (k == 0) {
                                    diff ^= 1;
                                }
                                diff ^= 1;
                            }
                            int go = Math.max(t, node.light);
                            next[j ^ diff][go] += way;
                        }
                    }
                }
            }

            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    next[j][k] %= mod;
                }
            }

            long[][] tmp = last;
            last = next;
            next = tmp;
        }

        long[] ans = new long[2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ans[i] += last[i][j];
                ans[i] %= mod;
            }
        }

        return ans;
    }

    public Node findLeaf(Node root, Edge p) {
        if (root.tag) {
            return root;
        }
        root.tag = true;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            return findLeaf(e.other(root), e);
        }

        return root;
    }

    public void find(Node root, Edge p) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        dq.add(root);
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            root.next = e;
            find(e.other(root), e);
            return;
        }
    }

    public void addEdge(Node a, Node b, int xor) {
        Edge e = new Edge();
        e.a = a;
        e.b = b;
        e.xor = xor;
        a.adj.add(e);
        b.adj.add(e);
    }

    int mod = (int) 1e9 + 7;

    public void add(long[] a, long[] b, int xor) {
        for (int i = 0; i < 2; i++) {
            a[i] += b[i ^ xor];
        }
    }

}

class Edge {
    Node a;
    Node b;
    int xor;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int virtual;
    boolean visited;
    boolean tag;
    int light;
    Edge next;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
