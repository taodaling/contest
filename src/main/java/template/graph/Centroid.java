package template.graph;

import java.util.List;
import java.util.function.IntConsumer;

public class Centroid {
    private int[] size;
    private List<? extends DirectedEdge>[] g;
    private IntConsumer consumer;

    public Centroid(int n) {
        size = new int[n];
    }

    public void findCentroid(List<? extends DirectedEdge>[] g, IntConsumer consumer) {
        this.g = g;
        this.consumer = consumer;
        dfsForSize(0, -1);
        find(0, -1);
    }

    private void find(int root, int p) {
        int maxSub = g.length - size[root];
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            find(e.to, root);
            maxSub = Math.max(maxSub, size[e.to]);
        }
        if (maxSub * 2 <= g.length) {
            consumer.accept(root);
        }
    }

    private void dfsForSize(int root, int p) {
        size[root] = 1;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForSize(e.to, root);
            size[root] += size[e.to];
        }
    }
}
