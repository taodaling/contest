package on2021_08.on2021_08_31_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.G__Gates_to_Another_World;



import template.binary.Bits;
import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SortUtils;

import java.util.*;

public class GGatesToAnotherWorld {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<Query> qs = new ArrayList<>(m);
        List<Query> updates = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            int t = in.rs().equals("block") ? 0 : 1;
            Query q = new Query(t, in.rl(), in.rl());
            qs.add(q);
            if (q.type == 0) {
                updates.add(q);
            }
        }
        debug.debug("updates", updates);
        List<Query> revQs = new ArrayList<>(qs);
        Collections.reverse(revQs);
        debug.debug("revQs", revQs);
        nodeList = new ArrayList<>(m * 2);
        updates.sort(Comparator.comparingLong(x -> x.l));
        long last = 0;
        for (Query q : updates) {
            long l = last;
            long r = q.l - 1;
            addNode(l, r, false);
            addNode(q.l, q.r, true);
            last = q.r + 1;
        }
        addNode(last, (1L << n) - 1, false);
        debug.elapse("createNode");
        Node[] nodes = nodeList.toArray(new Node[0]);
        debug.debug("nodes", nodes);
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].id = i;
        }
        assert SortUtils.notStrictAscending(nodes, 0, nodes.length - 1, Comparator.comparingLong(x -> x.l));
        dac(nodes, 0, nodes.length - 1, 0, (1L << n) - 1, n - 1);

        dsu = new DSU(nodes.length);
        dsu.init();

        for (Node root : nodes) {
            recalc(root);
        }

        debug.elapse("build");

//        search(nodes, nodes[0], nodes[12]);
//        for(Node cur = nodes[12]; cur != null; cur = cur.prev){
//            debug.debug("cur", cur);
//        }
        for (Query q : revQs) {
            Node a = search(nodes, q.l);
            Node b = search(nodes, q.r);
            if (q.type == 0) {
                assert a.del;
                assert b.del;
                a.del = false;
                b.del = false;
                recalc(a);
                recalc(b);
            } else {
                assert !a.del;
                assert !b.del;
                q.ans = dsu.find(a.id) == dsu.find(b.id);
            }
        }

        debug.elapse("answer");
        for (Query q : qs) {
            if (q.type == 1) {
                out.println(q.ans ? 1 : 0);
            }
        }
    }

    public boolean intersect(long l, long r, long L, long R) {
        return l <= R && r >= L;
    }

    public boolean checkInRange(Node[] nodes, int l, int r, long L, long R){
        for(int i = l; i <= r; i++){
            if(nodes[i].l < L || nodes[i].r > R){
                return false;
            }
        }
        return true;
    }

    public void dac(Node[] nodes, int l, int r, long L, long R, int D) {
        assert checkInRange(nodes, l, r, L, R);
        if (l >= r) {
            return;
        }
        if(D < 0){
            assert l == r;
        }
        long M = (L + R) / 2;
        int ml = l - 1;
        while (ml + 1 <= r && nodes[ml + 1].r <= M) {
            ml++;
        }
        int mr = r + 1;
        while (mr - 1 >= l && nodes[mr - 1].l > M) {
            mr--;
        }

        dac(nodes, l, ml, L, M, D - 1);
        dac(nodes, mr, r, M + 1, R, D - 1);

        if (ml + 1 < mr) {
            for (int j = ml + 1; j < mr; j++) {
                for (int i = l; i <= ml; i++) {
                    addEdge(nodes[i], nodes[j]);
                }
                for (int i = mr; i <= r; i++) {
                    addEdge(nodes[i], nodes[j]);
                }
            }
        } else {
            int lIter = l;
            int rIter = mr;
            long mask = (1L << D) - 1;
            while (lIter <= ml && rIter <= r) {
                int comp = Long.compare(nodes[lIter].r & mask, nodes[rIter].r & mask);
                if (intersect(nodes[lIter].l & mask, nodes[lIter].r & mask,
                        nodes[rIter].l & mask, nodes[rIter].r & mask)) {
                    addEdge(nodes[lIter], nodes[rIter]);
                }
                if (comp <= 0) {
                    lIter++;
                }
                if (comp >= 0) {
                    rIter++;
                }
            }
        }
    }

    Debug debug = new Debug(false);
    DSU dsu;

    public void search(Node[] nodes, Node root, Node to) {
        int inf = (int) 1e9;
        for (Node node : nodes) {
            node.dist = inf;
        }
        Deque<Node> dq = new ArrayDeque<>();
        root.prev = null;
        root.dist = 0;
        dq.addLast(root);
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            for (Node node : head.adj) {
                if (node.del) {
                    continue;
                }
                if (node.dist <= head.dist + 1) {
                    continue;
                }
                node.prev = head;
                node.dist = head.dist + 1;
                dq.addLast(node);
            }
        }
    }

    public void recalc(Node root) {
        if (root.del) {
            return;
        }
        for (Node node : root.adj) {
            if (node.del) {
                continue;
            }
            dsu.merge(root.id, node.id);
        }
    }

    public boolean check(Node a, Node b) {
        for (int i = 0; i < 50; i++) {
            for (long j = a.l; j <= a.r; j++) {
                long to = j ^ (1L << i);
                if (b.l <= to && b.r >= to) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addEdge(Node a, Node b) {
        assert check(a, b);
        a.adj.add(b);
        b.adj.add(a);
    }

    public Node search(Node[] nodes, long x) {
        int l = 0;
        int r = nodes.length - 1;
        while (l < r) {
            int m = (l + r + 1) >> 1;
            if (nodes[m].l <= x) {
                l = m;
            } else {
                r = m - 1;
            }
        }
        return nodes[l];
    }

    List<Node> nodeList;

    void addNode(long l, long r, boolean del) {
        if (r < l) {
            return;
        }
        if (l == r) {
            addNode0(l, r, del);
            return;
        }
        int index = Bits.theFirstDifferentIndex(l, r);
        long prefix = (l >> (index + 1)) << (index + 1);
        long mid = prefix | (1L << index);
        addNode0(l, mid - 1, del);
        addNode0(mid, r, del);
    }

    void addNode0(long l, long r, boolean del) {
        if (r < l) {
            return;
        }
        nodeList.add(new Node(l, r, del));
    }
}

class Node {
    long l;
    long r;
    boolean del;
    int id;
    Node prev;
    int dist;

    public Node(long l, long r, boolean del) {
        this.l = l;
        this.r = r;
        this.del = del;
    }

    List<Node> adj = new ArrayList<>();

    @Override
    public String toString() {
        return "Node{" +
                "l=" + l +
                ", r=" + r +
                ", del=" + del +
                ", id=" + id +
                '}';
    }
}

class Query {
    int type;
    long l;
    long r;
    boolean ans;

    public Query(int type, long l, long r) {
        this.type = type;
        this.l = l;
        this.r = r;
    }

    @Override
    public String toString() {
        return "Query{" +
                "type=" + type +
                ", l=" + l +
                ", r=" + r +
                '}';
    }
}
