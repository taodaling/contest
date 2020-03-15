package template.graph;

import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class UndirectedOneWeightMinCircle {
    private List<UndirectedEdge>[] g;
    int[] dist;
    int[] prev;
    UndirectedEdge[] tag;
    IntegerDeque dq;
    IntegerList circle;

    public UndirectedOneWeightMinCircle(List<UndirectedEdge>[] g) {
        this.g = g;
        int n = g.length;
        dist = new int[n];
        prev = new int[n];
        tag = new UndirectedEdge[n];
        dq = new IntegerDequeImpl(n);
        circle = new IntegerList(n + n);
    }

    private void collect(int root) {
        while (root != -1) {
            circle.add(root);
            root = prev[root];
        }
    }

    public IntegerList getCircle() {
        return circle.size() == 0 ? null : circle;
    }

    public void optimize(int root) {
        Arrays.fill(dist, -1);
        Arrays.fill(prev, -1);
        Arrays.fill(tag, null);
        dq.clear();

        dist[root] = 0;
        for (UndirectedEdge e : g[root]) {
            if (dist[e.to] != -1) {
                if (e.to == root && (circle.size() == 0 || circle.size() > 1)) {
                    circle.clear();
                    circle.add(root);
                }
                if (circle.size() == 0 || circle.size() > 2) {
                    circle.clear();
                    circle.add(root);
                    circle.add(e.to);
                }
                continue;
            }
            dist[e.to] = 1;
            tag[e.to] = e;
            prev[e.to] = root;
            dq.addLast(e.to);
        }

        UndirectedEdge cross = null;
        int best = circle.size() == 0 ? g.length + 1 : circle.size();
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            for (UndirectedEdge e : g[head]) {
                if (dist[e.to] != -1) {
                    if (e.to != root && tag[e.to] != tag[head] && best > dist[head] + dist[e.to] + 1) {
                        best = dist[head] + dist[e.to] + 1;
                        cross = e;
                    }
                    continue;
                }
                dist[e.to] = dist[head] + 1;
                tag[e.to] = tag[head];
                prev[e.to] = head;
                dq.addLast(e.to);
            }
        }

        if (cross != null && (circle.size() == 0 || best < circle.size())) {
            circle.clear();
            collect(cross.to);
            circle.pop();
            int size = circle.size();
            collect(cross.rev.to);
            SequenceUtils.reverse(circle.getData(), size, circle.size() - 1);
        }
    }
}
