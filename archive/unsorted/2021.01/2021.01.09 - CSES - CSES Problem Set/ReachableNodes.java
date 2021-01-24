package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class ReachableNodes {
    int n;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for(int i = 0; i < m; i++){
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
        }
        for(Node node : nodes){
            out.println(solve(node).size());
        }
    }

    public BitSet solve(Node root){
        if(root.reach == null){
            root.reach = new BitSet(n);
            root.reach.set(root.id);
            for(Node node : root.adj){
                root.reach.or(solve(node));
            }
        }
        return root.reach;
    }
}

class Node{
    int id;
    List<Node> adj = new ArrayList<>();
    BitSet reach;
}