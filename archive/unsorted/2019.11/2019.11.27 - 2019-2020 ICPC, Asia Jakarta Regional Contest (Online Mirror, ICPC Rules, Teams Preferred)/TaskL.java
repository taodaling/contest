package contest;

import template.graph.LongISAP;
import template.graph.MultiWayDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TaskL {
    int n;

    int idOfRoad(int i) {
        return i;
    }

    int idOfColor(int i) {
        return n + i;
    }

    int idOfSrc() {
        return idOfColor(10000 + 1);
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        Edge[] edges = new Edge[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[i];
            edges[i].b = nodes[in.readInt() - 1];
            edges[i].a.next.add(edges[i]);
            edges[i].b.next.add(edges[i]);
            int m = in.readInt();
            edges[i].color = new IntegerList(m);
            for (int j = 0; j < m; j++) {
                edges[i].color.add(in.readInt());
            }
        }


        dfs(nodes[0], null);
        LongISAP isap = new LongISAP(idOfDst() + 1);
        isap.setSource(idOfSrc());
        isap.setTarget(idOfDst());

        Worker[] workers = new Worker[k];
        for (int i = 0; i < k; i++) {
            workers[i] = new Worker();
            workers[i].c = in.readInt();
        }

        for (int i = 0; i < n; i++) {
            isap.getChannel(idOfSrc(), idOfRoad(i)).modify(1, 0);
        }

        IntegerList allColor = new IntegerList(10000);
        for (int i = 0; i < n; i++) {
            allColor.addAll(edges[i].color);
        }
        for (int i = 0; i < k; i++) {
            allColor.add(workers[i].c);
        }
        DiscreteMap dm = new DiscreteMap(allColor.toArray());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < edges[i].color.size(); j++) {
                edges[i].color.set(j, dm.rankOf(edges[i].color.get(j)));
            }
        }
        for (int i = 0; i < k; i++) {
            workers[i].c = dm.rankOf(workers[i].c);
        }

        int addedEdge = 0;
        for (int i = 0; i < n; i++) {
            if (edges[i].circle) {
                continue;
            }
            addedEdge++;
            for (IntegerIterator iterator = edges[i].color.iterator(); iterator.hasNext(); ) {
                isap.getChannel(idOfRoad(edges[i].a.id), idOfColor(iterator.next())).modify(1, 0);
            }
        }

        for (int i = 0; i < k; i++) {
            isap.getChannel(idOfColor(workers[i].c), idOfDst()).modify(1, 0);
        }

        isap.send(n);
        if (isap.totalFlow() != addedEdge) {
            out.println(-1);
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!edges[i].circle) {
                continue;
            }
            for (IntegerIterator iterator = edges[i].color.iterator(); iterator.hasNext(); ) {
                isap.getChannel(idOfRoad(i), idOfColor(iterator.next())).modify(1, 0);
            }
        }


        isap.send(n - 1 - addedEdge);
        if (isap.totalFlow() != n - 1) {
            out.println(-1);
            return;
        }

        MultiWayDeque<Edge> edgeDeque = new MultiWayDeque<>(10001, n);
        MultiWayDeque<Worker> workerDeque = new MultiWayDeque<>(10001, k);
        for (int i = 0; i < n; i++) {
            for (IntegerIterator iterator = edges[i].color.iterator(); iterator.hasNext(); ) {
                int c = iterator.next();
                if (isap.getChannel(idOfRoad(i), idOfColor(c)).getFlow() == 1) {
                    edgeDeque.addLast(c, edges[i]);
                }
            }
        }
        for (int i = 0; i < k; i++) {
            workerDeque.addLast(workers[i].c, workers[i]);
        }
        for (int i = 0; i <= 10000; i++) {
            while (!edgeDeque.isEmpty(i)) {
                Edge e = edgeDeque.removeFirst(i);
                Worker w = workerDeque.removeFirst(i);
                w.e = e;
            }
        }

        for (int i = 0; i < k; i++) {
            if (workers[i].e == null) {
                out.println("0 0");
            } else {
                out.append(workers[i].e.a.id + 1).append(' ')
                        .append(workers[i].e.b.id + 1).append('\n');
            }
        }
    }

    Deque<Node> deque = new ArrayDeque<>();

    public boolean dfs(Node root, Edge p) {
        if (root.visited) {
            if (root.instk) {
                p.circle = true;
                while (true) {
                    Node tail = deque.removeLast();
                    if (tail == root) {
                        break;
                    }
                    tail.p.circle = true;
                }
                return true;
            }
            return false;
        }
        root.p = p;
        root.visited = true;
        root.instk = true;
        deque.addLast(root);

        for (Edge e : root.next) {
            if (e == p) {
                continue;
            }
            Node node = e.a == root ? e.b : e.a;
            if (dfs(node, e)) {
                return true;
            }
        }

        deque.removeLast();
        root.instk = false;
        return false;
    }
}

class Edge {
    IntegerList color;
    Node a;
    Node b;
    boolean circle;
}

class Node {
    List<Edge> next = new ArrayList<>();
    boolean instk;
    boolean visited;
    Edge p;
    int id;
}

class Worker {
    int c;
    Edge e;
}