package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongObjectHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FDistanceSums {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] d = new long[n];
        for (int i = 0; i < n; i++) {
            d[i] = in.readLong();
        }


        LongObjectHashMap<Node> map = new LongObjectHashMap<>(n, false);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].di = d[i];
            nodes[i].size = 1;
            map.put(d[i], nodes[i]);
        }


        List<int[]> edges = new ArrayList<>(n - 1);
        Arrays.sort(nodes, (a, b) -> -Long.compare(a.di, b.di));
        for (int i = 0; i < n - 1; i++) {
            Node p = map.get(nodes[i].di + nodes[i].size * 2 - n);
            if (p == null) {
                out.println(-1);
                return;
            }
            edges.add(new int[]{p.id, nodes[i].id});
            p.next.add(nodes[i]);
            p.size += nodes[i].size;
        }

        if(dfs(nodes[n - 1]) != nodes[n - 1].di){
            out.println(-1);
            return;
        }

        for (int[] e : edges) {
            out.append(e[0] + 1).append(' ').append(e[1] + 1).println();
        }
    }

    public long dfs(Node root) {
        long ans = 0;
        for (Node node : root.next) {
            ans += dfs(node) + node.size;
        }
        return ans;
    }
}


class Node {
    long di;
    int size;
    int id;

    List<Node> next = new ArrayList<>();
}