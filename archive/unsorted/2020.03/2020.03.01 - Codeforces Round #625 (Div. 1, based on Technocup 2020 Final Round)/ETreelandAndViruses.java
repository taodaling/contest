package contest;

import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class ETreelandAndViruses {
    List<UndirectedEdge>[] g;
    int[] depths;
    int[] dfn;
    LcaOnTree lcaOnTree;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        g = Graph.createUndirectedGraph(n);
        depths = new int[n];
        dfn = new int[n];
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        dfs(0, -1, 0);
        lcaOnTree = new LcaOnTree(g, 0);
        int q = in.readInt();
        appear = new IntegerVersionArray(n);
        nodes = new IntegerList(n);
        deque = new IntegerDequeImpl(n);
        vtree = Graph.createUndirectedGraph(n);
        events = new PriorityQueue<>(2 * n, (a, b) ->
                a.turn == b.turn ? Integer.compare(a.virus, b.virus) :
                        Integer.compare(a.turn, b.turn));
        endWith = new int[n];
        for (int i = 0; i < q; i++) {
            solveOnce(in, out);
        }
    }

    int[] endWith;
    IntegerList nodes;
    IntegerVersionArray appear;
    IntegerDeque deque;

    public void addNode(int i) {
        if (appear.get(i) == 0) {
            appear.set(i, 1);
            nodes.add(i);
        }
    }

    List<UndirectedEdge>[] vtree;

    public void solveOnce(FastInput in, FastOutput out) {
        int k = in.readInt();
        int m = in.readInt();
        appear.clear();
        int[][] virus = new int[k][2];
        nodes.clear();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < 2; j++) {
                virus[i][j] = in.readInt();
            }
            virus[i][0]--;
            addNode(virus[i][0]);
        }
        int[] importance = new int[m];
        for (int i = 0; i < m; i++) {
            importance[i] = in.readInt() - 1;
            addNode(importance[i]);
        }

        nodes.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
        for (int i = 1, end = nodes.size(); i < end; i++) {
            int last = nodes.get(i - 1);
            int cur = nodes.get(i);
            addNode(lcaOnTree.lca(last, cur));
        }
        nodes.sort((a, b) -> Integer.compare(dfn[a], dfn[b]));
        for (int i = 0, end = nodes.size(); i < end; i++) {
            int node = nodes.get(i);
            vtree[node].clear();
            endWith[node] = -1;
        }
        deque.clear();
        deque.addLast(nodes.get(0));
        for (int i = 1, end = this.nodes.size(); i < end; i++) {
            int node = this.nodes.get(i);
            while (!deque.isEmpty() && lcaOnTree.lca(deque.peekLast(), node) !=
                    deque.peekLast()) {
                deque.removeLast();
            }
            Graph.addUndirectedEdge(vtree, deque.peekLast(), node);
            deque.addLast(node);
        }
        events.clear();
        for (int i = 0; i < k; i++) {
            publish(i, 0, virus[i][0]);
        }

        while (!events.isEmpty()) {
            Event event = events.remove();
            if (endWith[event.node] != -1) {
                continue;
            }
            endWith[event.node] = event.virus;
            for (UndirectedEdge e : vtree[event.node]) {
                if (endWith[e.to] != -1) {
                    continue;
                }
                int dist = distance(e.to, virus[event.virus][0]);
                int turn = DigitUtils.ceilDiv(dist, virus[event.virus][1]);
                publish(event.virus, turn, e.to);
            }
        }

        for(int i : importance){
            out.append(endWith[i] + 1).append(' ');
        }
        out.println();
    }

    public int distance(int a, int b) {
        int c = lcaOnTree.lca(a, b);
        return depths[a] + depths[b] - depths[c] * 2;
    }

    public void publish(int virus, int turn, int node) {
        Event event = new Event();
        event.virus = virus;
        event.turn = turn;
        event.node = node;
        events.add(event);
    }

    int order = 0;
    PriorityQueue<Event> events;

    public void dfs(int root, int p, int depth) {
        depths[root] = depth;
        dfn[root] = order++;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, depth + 1);
        }
    }
}

class Event {
    int turn;
    int virus;
    int node;
}