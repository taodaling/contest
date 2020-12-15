package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;
import java.util.stream.Collectors;

public class RecoverPath {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.id = i;
            e.w = in.ri();
            a.adj.add(e);
            b.adj.add(e);
        }
        int k = in.ri();
        List<Node> specials = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            Node node = nodes[in.ri() - 1];
            specials.add(node);
            node.special = 1;
        }
        Node root = specials.get(0);
        dijkstra(nodes, root);
        for (Node node : specials) {
            if (node.dist > root.dist) {
                root = node;
            }
        }
        dijkstra(nodes, root);
        Arrays.sort(nodes, Comparator.<Node>comparingInt(x -> x.dist).reversed());
        for (Node node : nodes) {
            for (Edge e : node.adj) {
                Node other = e.other(node);
                if (other.dist == e.w + node.dist && other.path > node.path) {
                    node.path = other.path;
                    node.out = e;
                }
            }
            node.path += node.special;
        }
        List<Edge> seq = new ArrayList<>(n);
        Node now = root;
        while(now.out != null){
            seq.add(now.out);
            now = now.out.other(now);
        }
        out.println(seq.size());
        for(Edge e : seq){
            out.append(e.id + 1).append(' ');
        }
    }

    int inf = (int) 1e9 + 100;

    public void dijkstra(Node[] nodes, Node root) {
        for (Node node : nodes) {
            node.dist = inf;
        }
        TreeSet<Node> pq = new TreeSet<>((a, b) -> a.dist == b.dist ? Integer.compare(a.id, b.id) :
                Integer.compare(a.dist, b.dist));
        root.dist = 0;
        pq.add(root);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            for (Edge e : head.adj) {
                Node node = e.other(head);
                if (node.dist <= head.dist + e.w) {
                    continue;
                }
                pq.remove(node);
                node.dist = head.dist + e.w;
                pq.add(node);
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    int w;
    int id;

    Node other(Node x) {
        return a == x ? b : a;
    }
}


class Node {
    List<Edge> adj = new ArrayList<>();
    int dist;
    int id;
    int special;
    int path;
    Edge out;
}