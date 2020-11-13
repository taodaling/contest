package template.graph;

import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;

public class UndirectedOneWeightMinCircle {
    private List<UndirectedEdge>[] g;
    int[] dist;
    int[] prev;
    UndirectedEdge[] tag;
    IntegerDeque dq;

    public UndirectedOneWeightMinCircle(List<UndirectedEdge>[] g) {
        this.g = g;
        int n = g.length;
        dist = new int[n];
        prev = new int[n];
        tag = new UndirectedEdge[n];
        dq = new IntegerDequeImpl(n);
    }

    private void collect(int root, IntegerArrayList circle) {
        while (root != -1) {
            circle.add(root);
            root = prev[root];
        }
    }

    /**
     * <pre>
     * 搜索包含root的最小环，并保存在circle中。（如果circle的大小为0表示无解），否则
     * 找到长度小于circle.size()的包含root的最小的环。
     * </pre>
     * <pre>
     * 返回结果表示是否找到这样的环。
     * </pre>
     * <pre>
     *  时间复杂度O(V+E)
     * </pre>
     */
    public boolean findCircle(int root, IntegerArrayList circle, int best) {
        Arrays.fill(dist, -1);
        Arrays.fill(prev, -1);
        Arrays.fill(tag, null);
        dq.clear();

        dist[root] = 0;
        boolean ans = false;
        for (UndirectedEdge e : g[root]) {
            if (dist[e.to] != -1) {
                if (e.to == root && (circle.size() == 0 || circle.size() > 1)) {
                    circle.clear();
                    circle.add(root);
                    ans = true;
                }
                if (circle.size() == 0 || circle.size() > 2) {
                    circle.clear();
                    circle.add(root);
                    circle.add(e.to);
                    ans = true;
                }
                continue;
            }
            dist[e.to] = 1;
            tag[e.to] = e;
            prev[e.to] = root;
            dq.addLast(e.to);
        }

        if (circle.size() == 1 || circle.size() == 2) {
            return ans;
        }

        UndirectedEdge cross = null;
        best = Math.min(best, circle.size() == 0 ? g.length + 1 : circle.size());
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            if (dist[head] * 2 + 1 >= best) {
                break;
            }
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
            collect(cross.to, circle);
            circle.pop();
            int size = circle.size();
            collect(cross.rev.to, circle);
            SequenceUtils.reverse(circle.getData(), size, circle.size() - 1);
            ans = true;
        }
        return ans;
    }
}
