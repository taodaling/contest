package on2020_08.on2020_08_16_.Quarantine;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Quarantine {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        debug.debug("testNumber", testNumber);
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].color = in.readChar() == '#' ? 0 : 1;
            nodes[i].weight = 1;
            nodes[i].id = i;
        }
        int[] p = new int[n + 1];
        for (int i = 2; i <= k + 1; i++) {
            p[i] = in.readInt();
        }
        long a = in.readLong();
        long b = in.readLong();
        long c = in.readLong();
        for (int i = k + 2; i <= n; i++) {
            p[i] = (int) ((a * p[i - 2] + b * p[i - 1] + c) % (i - 1) + 1);
        }
        for (int i = 2; i <= n; i++) {
            Node x = nodes[p[i] - 1];
            Node y = nodes[i - 1];
            addEdge(x, y, 1);
            if (x.color == 1 && y.color == 1) {
                Node.merge(x, y);
            }
        }

        int oneBlock = 0;
        for (Node node : nodes) {
            if (node.color == 1 && node.find() == node) {
                oneBlock++;
            }
        }

        if (oneBlock <= 1) {
            dfsForSize(nodes[0], null);
            dfsForSize1(nodes[0], null);
            long way = countWay(nodes[0], null, nodes[0].size, nodes[0].size1);
            out.append(pair(nodes[0].size1)).append(' ').println(way);
            return;
        }

        dfs(nodes[0], null);
        best = new Pair();
        dac(nodes[0]);

        long ans = best.size;
        for (Node node : nodes) {
            if (node.find() != node) {
                continue;
            }
            ans += pair(node.setSize);
        }
        out.append(ans).append(' ').println(best.cnt);
    }

    public long countWay(Node root, Edge p, int total, int totalSize1) {
        long way = 0;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            way += countWay(node, e, total, totalSize1);
            if (e.a.color == 1 && e.b.color == 1) {
                way += (long) (totalSize1 - node.size1) * node.size1;
            } else {
                way += (long) (total - node.size) * node.size;
            }
        }
        return way;
    }

    public long pair(long n) {
        return n * (n - 1) / 2;
    }

    public void addEdge(Node a, Node b, int w) {
        Edge e = new Edge();
        e.a = a;
        e.b = b;
        e.w = w;
        a.adj.add(e);
        b.adj.add(e);
    }

    public void dfsForSize1(Node root, Edge p) {
        root.size1 = root.color;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForSize1(node, e);
            root.size1 += node.size1;
        }
    }

    public void dfsForSize(Node root, Edge p) {
        root.size = 1;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForSize(node, e);
            root.size += node.size;
        }
    }

    public int valueOf(Edge e, int total) {
        int min = Math.min(e.a.size, e.b.size);
        return Math.max(min, total - min);
    }

    public Edge better(Edge a, Edge b, int total) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return valueOf(a, total) < valueOf(b, total) ? a : b;
    }

    public Edge dfsForCentroid(Node root, Edge p, int total) {
        Edge best = null;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            best = better(best, e, total);
            best = better(dfsForCentroid(node, e, total), best, total);
        }
        return best;
    }

    public int weight(Edge e) {
        return e.a.color == 1 && e.b.color == 1 ? 0 : e.w;
    }

    public int dfsForUpdate(Node root, Edge p, Pair pair, int depth) {
        int size = root.color == 1 ? root.weight : 0;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            int sub = dfsForUpdate(node, e, pair, depth + weight(e));
            if (root.color == 1) {
                size += sub;
            }
        }
        if (root.color == 1 && p != null && p.other(root).color == 0) {
            pair.update(size, 1, depth);
        }
        return size;
    }


    Pair best;

    public void update(Pair a, Pair b) {
        best.update(a.size * b.size, (a.cnt * b.depth + a.depth * b.cnt) * a.size * b.size, 0);
    }

    public void dac(Node root) {
        dfsForSize(root, null);
        if (root.size == 1) {
            return;
        }
        Edge centroid = dfsForCentroid(root, null, root.size);
        centroid.a.adj.remove(centroid);
        centroid.b.adj.remove(centroid);
        Pair a = new Pair();
        Pair b = new Pair();
        int sizeA = dfsForUpdate(centroid.a, null, a, 0);
        int sizeB = dfsForUpdate(centroid.b, null, b, weight(centroid));
        if (weight(centroid) == 1) {
            a.update(sizeA, 1, 0);
            b.update(sizeB, 1, 1);
        } else {
            Pair c = new Pair();
            c.update(sizeA + sizeB, 1, 0);
            update(a, c);
            update(b, c);
        }
        update(a, b);

        dac(centroid.a);
        dac(centroid.b);
    }

    int order = -1;
    //Set<Node> set = new HashSet<>();
    public void dfs(Node root, Edge p) {
//        if(set.size() > 1000000 || set.contains(root)){
//            throw new RuntimeException();
//        }
//        set.add(root);
        if (root.adj.size() - (p == null ? 0 : 1) > 2) {
            root.adj.remove(p);
            List<Edge> list = root.adj;
            root.adj = new ArrayList<>();
            root.adj.add(list.remove(list.size() - 1));
            Node last = root;
            for (Edge e : list) {
                Node next = new Node();
                next.id = order--;
                next.color = root.color;
                addEdge(last, next, 0);
                e.replace(root, next);
                next.adj.add(e);
                last = next;
            }
            if (p != null) {
                root.adj.add(p);
            }
        }

        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            dfs(e.other(root), e);
        }
    }
}

class Pair {
    long size;
    long cnt;
    long depth;


    public void update(Pair p) {
        update(p.size, p.cnt, p.depth);
    }

    public void update(long size, long cnt, long depth) {
        if (this.size < size) {
            this.size = size;
            this.depth = 0;
            this.cnt = 0;
        }
        if (this.size == size) {
            this.cnt += cnt;
            this.depth += depth;
        }
    }
}

class Edge {
    Node a;
    Node b;

    int w;

    public void replace(Node x, Node y) {
        if (a == x) {
            a = y;
        } else {
            b = y;
        }
    }

    Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int color = 1;
    int weight;
    Node p = this;
    int rank;
    int setSize = 1;
    int size;
    int size1;
    int id = -1;
    boolean visited;

    @Override
    public String toString() {
        return "" + (id + 1);
    }

    public Node find() {
        return p == p.p ? p : (p = p.find());
    }

    public static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank < b.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
        a.setSize += b.setSize;
    }
}