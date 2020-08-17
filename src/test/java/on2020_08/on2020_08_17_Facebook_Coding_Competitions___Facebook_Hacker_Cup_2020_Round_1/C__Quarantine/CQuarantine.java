package on2020_08.on2020_08_17_Facebook_Coding_Competitions___Facebook_Hacker_Cup_2020_Round_1.C__Quarantine;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CQuarantine {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);
        debug.debug("testNumber", testNumber);
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].color = in.readChar() == '#' ? 0 : 1;
        }
        int[] p = new int[n + 1];
        for (int i = 2; i <= k + 1; i++) {
            p[i] = in.readInt();
        }
        long a = in.readInt();
        long b = in.readInt();
        long c = in.readInt();
        for (int i = k + 2; i <= n; i++) {
            p[i] = (int) ((a * p[i - 2] + b * p[i - 1] + c) % (i - 1) + 1);
        }
        for (int i = 2; i <= n; i++) {
            Node fa = nodes[p[i] - 1];
            Node child = nodes[i - 1];
            addEdge(fa, child);
        }

        long pairs = 0;
        long way = 0;
        int block = 0;
        for (Node node : nodes) {
            if (node.color == 0 || node.visited) {
                continue;
            }
            block++;
            dfsForSize(node, null);
            dfsForCost(node, null, node.size);
            pairs += countPair(node.size);
        }

        if (block <= 1) {
            bruteForceForSize(nodes[0], null);
            way = bruteForceForWay(nodes[0], null, nodes[0].size, nodes[0].size1);
        } else {
            nodes[0].adj.add(null);
            dfsForTuple(nodes[0], null);
            best = new Tuple();
            upDown(nodes[0], null, 0, new Tuple());

            pairs += best.size;
            way = best.cnt;
        }
        out.append(pairs).append(' ').println(way);
    }

    public long bruteForceForWay(Node root, Edge p, long total, long total1) {
        long ans = 0;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            ans += bruteForceForWay(node, e, total, total1);
            if (e.a.color == 1 && e.b.color == 1) {
                ans += node.size1 * (total1 - node.size1);
            } else {
                ans += node.size * (total - node.size);
            }
        }
        return ans;
    }

    public void bruteForceForSize(Node root, Edge p) {
        root.size1 = root.color;
        root.size = 1;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            bruteForceForSize(node, e);
            root.size1 += node.size1;
            root.size += node.size;
        }
    }

    public void addEdge(Node a, Node b) {
        Edge e = new Edge();
        e.a = a;
        e.b = b;
        a.adj.add(e);
        b.adj.add(e);
    }

    public long countPair(long n) {
        return n * (n - 1) / 2;
    }

    public void dfsForSize(Node root, Edge p) {
        root.visited = true;
        root.size = 1;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            if (node.color == 0) {
                continue;
            }
            dfsForSize(node, e);
            root.size += node.size;
        }
    }

    public void dfsForCost(Node root, Edge p, int total) {
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            if (node.color == 0) {
                continue;
            }
            e.cost = countPair(total) - countPair(total - node.size) -
                    countPair(node.size);
            dfsForCost(node, e, total);
        }
    }


    public void dfsForTuple(Node root, Edge p) {
        root.curSize = root.color;
        for (Edge e : root.adj) {
            if (e == p) {
                continue;
            }
            Node node = e.other(root);
            dfsForTuple(node, e);
            root.tuple.update(node.tuple);
            if (root.color == 1) {
                root.curSize += node.curSize;
            } else {
                root.tuple.update(node.curSize, 1);
            }
        }
    }

    Tuple best;

    public void upDown(Node root, Edge p, int curSize, Tuple fromP) {
        //two way
        if (root.color == 1) {
            curSize += root.curSize;
            int n = root.adj.size();
            Tuple[] l2r = new Tuple[n];
            Tuple[] r2l = new Tuple[n];
            for (int i = 0; i < n; i++) {
                Edge e = root.adj.get(i);
                if (e == p) {
                    r2l[i] = l2r[i] = fromP;
                } else {
                    r2l[i] = l2r[i] = e.other(root).tuple;
                }
            }
            for (int i = 1; i < n; i++) {
                l2r[i] = Tuple.merge(l2r[i - 1], l2r[i]);
            }
            for (int i = n - 2; i >= 0; i--) {
                r2l[i] = Tuple.merge(r2l[i + 1], r2l[i]);
            }

            for (int i = 0; i < n; i++) {
                Edge e = root.adj.get(i);
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                //remove edge e
                Tuple up = new Tuple();
                if (i > 0) {
                    up.update(l2r[i - 1]);
                }
                if (i + 1 < n) {
                    up.update(r2l[i + 1]);
                }
                //recursive
                upDown(node, e, curSize - node.curSize, up);

                up.update(curSize - node.curSize, 1);
                Tuple down = node.tuple.clone();
                down.update(node.curSize, 1);

                best.update(up.size * down.size - e.cost,
                        up.cnt * down.cnt * up.size * down.size);
            }
        } else {
            if (p != null) {
                fromP = fromP.clone();
                fromP.update(curSize, 1);
            }
            int n = root.adj.size();
            Tuple[] l2r = new Tuple[n];
            Tuple[] r2l = new Tuple[n];
            for (int i = 0; i < n; i++) {
                Edge e = root.adj.get(i);
                if (e == p) {
                    r2l[i] = l2r[i] = fromP;
                } else {
                    r2l[i] = l2r[i] = e.other(root).tuple.clone();
                    r2l[i].update(e.other(root).curSize, 1);
                }
            }
            for (int i = 1; i < n; i++) {
                l2r[i] = Tuple.merge(l2r[i - 1], l2r[i]);
            }
            for (int i = n - 2; i >= 0; i--) {
                r2l[i] = Tuple.merge(r2l[i + 1], r2l[i]);
            }

            for (int i = 0; i < n; i++) {
                Edge e = root.adj.get(i);
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                //remove edge e
                Tuple up = new Tuple();
                if (i > 0) {
                    up.update(l2r[i - 1]);
                }
                if (i + 1 < n) {
                    up.update(r2l[i + 1]);
                }
                //recursive
                upDown(node, e, 0, up);

                up.update(0, 1);
                Tuple down = node.tuple.clone();
                down.update(node.curSize, 1);

                best.update(up.size * down.size - e.cost,
                        up.cnt * down.cnt * up.size * down.size);
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    long cost;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Tuple implements Cloneable {
    long size;
    long cnt;

    public static Tuple merge(Tuple a, Tuple b) {
        Tuple ans = new Tuple();
        ans.update(a);
        ans.update(b);
        return ans;
    }

    public void update(Tuple tuple) {
        update(tuple.size, tuple.cnt);
    }

    public void update(long size, long cnt) {
        if (this.size < size) {
            this.size = size;
            this.cnt = 0;
        }
        if (this.size == size) {
            this.cnt += cnt;
        }
    }

    @Override
    public Tuple clone() {
        try {
            return (Tuple) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", size, cnt);
    }
}

class Node {
    boolean visited;
    List<Edge> adj = new ArrayList<>();
    int color;
    int curSize;
    Tuple tuple = new Tuple();
    int size;
    int size1;

    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}
