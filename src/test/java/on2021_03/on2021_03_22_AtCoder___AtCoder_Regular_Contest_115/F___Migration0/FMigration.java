package on2021_03.on2021_03_22_AtCoder___AtCoder_Regular_Contest_115.F___Migration0;



import template.datastructure.PopOnlyHeap;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class FMigration {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].h = in.ri();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        int k = in.ri();
        Node[] from = new Node[k];
        Node[] to = new Node[k];
        for (int i = 0; i < k; i++) {
            from[i] = nodes[in.ri() - 1];
            to[i] = nodes[in.ri() - 1];
        }

        int[][] pathA = new int[k][n];
        int[][] pathB = new int[k][n];
        for (int i = 0; i < k; i++) {
            dfs(from[i], null, 0, pathA[i]);
        }
        for (int i = 0; i < k; i++) {
            dfs(to[i], null, 0, pathB[i]);
        }

        minA = from.clone();
        minB = to.clone();
        curA = 0;
        curB = 0;
        for (int i = 0; i < k; i++) {
            curA += minA[i].h;
            curB += minB[i].h;
        }
        differ = 0;
        for (int i = 0; i < k; i++) {
            if (minA[i] != minB[i]) {
                differ++;
            }
        }
        long X = Math.max(curA, curB);
        Holder[] hA = new Holder[k];
        Holder[] hB = new Holder[k];
        PriorityQueue<Holder> pqA = new PriorityQueue<>(k, Comparator.comparingLong(x -> x.atLeast));
        PriorityQueue<Holder> pqB = new PriorityQueue<>(k, Comparator.comparingLong(x -> x.atLeast));
        for (int i = 0; i < k; i++) {
            hA[i] = new Holder(i, pathA[i], nodes);
            hB[i] = new Holder(i, pathB[i], nodes);
            hA[i].calc(minA[i]);
            hB[i].calc(minB[i]);
            pqA.add(hA[i]);
            pqB.add(hB[i]);
        }
        while (differ != 0) {
            assert pqA.size() + pqB.size() > 0;
            boolean moved = false;
            while (!pqA.isEmpty() && pqA.peek().atLeast <= X - curA) {
                moved = true;
                Holder h = pqA.remove();
                Node node = h.pq.pop();
                updateA(h.id, node);
                if (h.calc(minA[h.id])) {
                    pqA.add(h);
                }
            }
            while (!pqB.isEmpty() && pqB.peek().atLeast <= X - curB) {
                moved = true;
                Holder h = pqB.remove();
                Node node = h.pq.pop();
                updateB(h.id, node);
                if (h.calc(minB[h.id])) {
                    pqB.add(h);
                }
            }
            if (!moved) {
                long atLeast = Long.MAX_VALUE;
                if (!pqA.isEmpty()) {
                    atLeast = Math.min(atLeast, pqA.peek().atLeast + curA);
                }
                if (!pqB.isEmpty()) {
                    atLeast = Math.min(atLeast, pqB.peek().atLeast + curB);
                }
                X = atLeast;
            }
        }

        out.println(X);
    }

    long curA;
    long curB;
    Node[] minA;
    Node[] minB;
    int differ;
    Comparator<Node> nodeComparator = Comparator.<Node>comparingInt(x -> x.h).thenComparingInt(x -> x.id);

    public void updateA(int i, Node node) {
        if (nodeComparator.compare(minA[i], node) <= 0) {
            return;
        }
        if (minA[i] == minB[i]) {
            differ++;
        }
        curA -= minA[i].h;
        minA[i] = node;
        curA += minA[i].h;
        if (minA[i] == minB[i]) {
            differ--;
        }
    }

    public void updateB(int i, Node node) {
        if (nodeComparator.compare(minB[i], node) <= 0) {
            return;
        }
        if (minA[i] == minB[i]) {
            differ++;
        }
        curB -= minB[i].h;
        minB[i] = node;
        curB += minB[i].h;
        if (minA[i] == minB[i]) {
            differ--;
        }
    }


    public void dfs(Node root, Node p, int largest, int[] res) {
        largest = Math.max(largest, root.h);
        res[root.id] = largest;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root, largest, res);
        }
    }
}

class Node {
    int h;
    int id;
    List<Node> adj = new ArrayList<>();
}

class Holder {
    int id;
    int[] dist;
    Node[] nodes;

    public Holder(int id, int[] dist, Node[] nodes) {
        this.id = id;
        this.dist = dist;
        this.nodes = nodes;
        pq = new PopOnlyHeap<>(dist.length, Comparator.<Node>comparingInt(x -> dist[x.id]));
        pq.init(i -> nodes[i], nodes.length);
    }

    PopOnlyHeap<Node> pq;

    long atLeast;

    public boolean calc(Node now) {
        if (pq.size() == 0)  {
            return false;
        }
        atLeast = dist[pq.peek().id] - now.h;
        return true;
    }
}