package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P4___AQT_and_Directed_Graph;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class P4AQTAndDirectedGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = nodes[i].max = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            b.adj.add(a);
        }
        TreeSet<Node> set = new TreeSet<>(Comparator.<Node>comparingInt(x -> x.max).thenComparingInt(x -> x.id));
        set.addAll(Arrays.asList(nodes));
        while(!set.isEmpty()){
            Node head = set.pollLast();
            for(Node node : head.adj){
                if(node.max < head.max){
                    set.remove(node);
                    node.max = head.max;
                    set.add(node);
                }
            }
        }
        for(int i = n - 1; i >= 0; i--){
            if(nodes[i].max > i){
                out.append(i + 1).append(' ').append(nodes[i].max + 1).println();
                return;
            }
        }
        out.println(-1);
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int max;
    int id;
}