package on2020_09.on2020_09_14_Codeforces___Codeforces_Round__359__Div__1_.B__Kay_and_Snowflake;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class BKayAndSnowflake {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for(int i = 1; i < n; i++){
            Node p = nodes[in.readInt() - 1];
            p.adj.add(nodes[i]);
            nodes[i].p = p;
        }

        Node root = nodes[0];
        dfs(root);
        for(int i = 0; i < q; i++){
            Node node = nodes[in.readInt() - 1];
            out.println(node.centroid.id + 1);
        }
    }

    public void dfs(Node root){
        root.size = 1;
        Node maxChild = null;
        for(Node node : root.adj){
            dfs(node);
            root.size += node.size;
            if(maxChild == null || maxChild.size < node.size){
                maxChild = node;
            }
        }
        if(maxChild == null || maxChild.size * 2 <= root.size){
            root.centroid = root;
        }else{
            Node centroid = maxChild.centroid;
            while((root.size - centroid.size) * 2 > root.size){
                centroid = centroid.p;
            }
            root.centroid = centroid;
        }
    }
}

class Node {
    int size;
    int id;
    Node p;
    Node centroid;
    List<Node> adj = new ArrayList<>();
}