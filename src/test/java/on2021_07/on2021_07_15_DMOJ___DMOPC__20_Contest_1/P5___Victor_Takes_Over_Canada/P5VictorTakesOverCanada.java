package on2021_07.on2021_07_15_DMOJ___DMOPC__20_Contest_1.P5___Victor_Takes_Over_Canada;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectPointSumProblem;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class P5VictorTakesOverCanada {
    TreeSet<Integer>[] occur;
    Node[] nodes;
    Node[] openToNode;

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int m = in.ri();
        nodes = new Node[n];
        openToNode = new Node[n];
        occur = new TreeSet[k];
        for (int i = 0; i < k; i++) {
            occur[i] = new TreeSet<>();
        }
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].color = in.ri() - 1;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        debug.debug("nodes", nodes);
        pts = new ArrayList<>(n + 6 * m);
        List<RectPointSumProblem.Query3D> qs = new ArrayList<>(m);
        for(int i = 0; i < n; i++){
            occur[nodes[i].color].add(nodes[i].open);
        }
        for (int i = 0; i < n; i++) {
            add(i, -1, 1);
        }
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            if (t == 1) {
                int city = in.ri() - 1;
                int color = in.ri() - 1;
                //remove color
                add(city, i, -1);
                Integer ceil = occur[nodes[city].color].ceiling(nodes[city].open);
                if (ceil != null) {
                    add(openToNode[ceil].id, i, -1);
                }
                occur[nodes[city].color].remove(nodes[city].open);
                if (ceil != null) {
                    add(openToNode[ceil].id, i, 1);
                }
                nodes[city].color = color;
                ceil = occur[nodes[city].color].ceiling(nodes[city].open);
                if (ceil != null) {
                    add(openToNode[ceil].id, i, -1);
                }
                occur[nodes[city].color].add(nodes[city].open);
                if (ceil != null) {
                    add(openToNode[ceil].id, i, 1);
                }
                add(city, i, 1);
            } else {
                int city = in.ri() - 1;
                int l = nodes[city].open;
                int r = nodes[city].close;
                qs.add(new RectPointSumProblem.Query3D(l, r, -1, l - 1, -1, i));
            }
        }
        debug.debug("occur", occur);
        debug.debug("pts", pts);
        debug.debug("qs", qs);
        long[] ans = RectPointSumProblem.solve(pts.toArray(new RectPointSumProblem.Point3D[0]), qs.toArray(new RectPointSumProblem.Query3D[0]));
        for (long x : ans) {
            out.println(x);
        }
    }

    List<RectPointSumProblem.Point3D> pts;

    public void add(int i, int time, int add) {
        Integer prev = occur[nodes[i].color].floor(nodes[i].open - 1);
        if (prev == null) {
            prev = -1;
        }
        pts.add(new RectPointSumProblem.Point3D(nodes[i].open, prev, time, add));
    }

    int order = 0;

    public void dfs(Node root, Node p) {
        root.open = order++;
        openToNode[root.open] = root;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
        root.close = order - 1;
    }

}

class Node {
    List<Node> adj = new ArrayList<>();
    int open;
    int close;
    int color;
    int id;

    @Override
    public String toString() {
        return "Node{" +
                "open=" + open +
                ", close=" + close +
                ", id=" + id +
                ", color=" + color +
                '}';
    }
}