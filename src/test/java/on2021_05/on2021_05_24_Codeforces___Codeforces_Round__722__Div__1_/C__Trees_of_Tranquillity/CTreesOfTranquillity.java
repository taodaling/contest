package on2021_05.on2021_05_24_Codeforces___Codeforces_Round__722__Div__1_.C__Trees_of_Tranquillity;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CTreesOfTranquillity {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[i];
            a.adj.add(b);
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[i];
            a.badj.add(b);
        }

        order = 0;
        eulerTour(nodes[0], null);
        map = new TreeMap<>();
        best = 0;

        dfs(nodes[0], null);
        out.println(best);
    }

    TreeMap<Integer, Node> map;
    int best;

    public void dfs(Node root, Node p) {
        Node rep = null;
        boolean added = false;
        Node floor = CollectionUtils.floorValue(map, root.l);
        Node ceil = CollectionUtils.ceilValue(map, root.l);
        if (ceil != null && root.contain(ceil)) {
        } else {
            if (floor != null && floor.contain(root)) {
                rep = map.remove(floor.l);
            }
            added = true;
            map.put(root.l, root);
        }

        best = Math.max(best, map.size());
        for(Node node : root.adj){
            if(node == p){
                continue;
            }
            dfs(node, root);
        }

        if(added){
            map.remove(root.l);
        }
        if(rep != null){
            map.put(rep.l, rep);
        }
    }

    int order = 0;

    public void eulerTour(Node root, Node p) {
        root.l = order++;
        for (Node node : root.badj) {
            if (node == p) {
                continue;
            }
            eulerTour(node, root);
        }
        root.r = order++;
    }
}

class Node {
    int l;
    int r;

    List<Node> adj = new ArrayList<>();

    List<Node> badj = new ArrayList<>();

    public boolean contain(Node x) {
        return l < x.l && r > x.r;
    }
}