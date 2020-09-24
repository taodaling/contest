package on2020_09.on2020_09_23_Codeforces___Codeforces_Round__352__Div__1_.D__Roads_in_Yusland;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.*;

public class DRoadsInYusland {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }

        for (int i = 0; i < m; i++) {
            Node u = nodes[in.readInt() - 1];
            Node v = nodes[in.readInt() - 1];
            int c = in.readInt();
            Repair repair = new Repair();
            repair.top = v;
            repair.cost = c;
            u.repairs.add(repair);
        }

        dfs(nodes[0], null);
        if (!possible) {
            out.println(-1);
        } else {
            out.println(sum);
        }
    }

    long sum = 0;

    Debug debug = new Debug(true);
    public TreeSetHolder dfs(Node root, Node p) {
        debug.debug("root", root);
        root.depth = p == null ? 0 : p.depth + 1;
        root.adj.remove(p);
        TreeSetHolder holder = new TreeSetHolder();
        for (Node node : root.adj) {
            holder = TreeSetHolder.merge(holder, dfs(node, root));
        }
        for (Repair r : root.repairs) {
            holder.add(r.top.depth, r.cost);
        }
        if(p != null) {
            holder.pop(root.depth - 1);
            if (holder.costMap.isEmpty()) {
                possible = false;
            } else {
                long cost = holder.costMap.lastEntry().getValue() + holder.modify;
                holder.mod(-cost);
                sum += cost;
            }
        }
        return holder;
    }

    boolean possible = true;
}

class TreeSetHolder {
    TreeMap<Integer, Long> costMap = new TreeMap<>();
    long modify;

    void pop(int d) {
        while (!costMap.isEmpty()) {
            int largestKey = costMap.lastKey();
            if (largestKey > d) {
                costMap.remove(largestKey);
            }else{
                break;
            }
        }

    }

    public void mod(long x) {
        modify += x;
    }

    public void add(Integer x, long c) {
        c -= modify;

        Map.Entry<Integer, Long> floor = costMap.floorEntry(x);
        if (floor != null && floor.getValue() <= c) {
            return;
        }

        while (true) {
            Map.Entry<Integer, Long> ceil = costMap.ceilingEntry(x);
            if (ceil == null || ceil.getValue() < c) {
                break;
            }
            costMap.remove(ceil.getKey());
        }
        costMap.put(x, c);
    }

    public static TreeSetHolder merge(TreeSetHolder a, TreeSetHolder b) {
        if (a.costMap.size() > b.costMap.size()) {
            TreeSetHolder tmp = a;
            a = b;
            b = tmp;
        }
        for (Map.Entry<Integer, Long> entry : a.costMap.entrySet()) {
            b.add(entry.getKey(), entry.getValue() + a.modify);
        }
        return b;
    }
}

class Repair {
    Node top;
    long cost;
}

class Node {
    int depth;
    List<Repair> repairs = new ArrayList<>();
    List<Node> adj = new ArrayList<>();
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}