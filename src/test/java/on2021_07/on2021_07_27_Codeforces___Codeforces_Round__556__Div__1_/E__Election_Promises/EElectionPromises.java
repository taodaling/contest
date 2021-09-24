package on2021_07.on2021_07_27_Codeforces___Codeforces_Round__556__Div__1_.E__Election_Promises;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerVersionArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EElectionPromises {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].h = in.ri();
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
        }
        int[] sg = new int[n];
        for (int i = 0; i < n; i++) {
            sg[sg(nodes[i])] ^= nodes[i].h;
        }
        int max = Arrays.stream(sg).max().orElse(-1);
        if (max == 0) {
            out.println("LOSE");
            return;
        }
        out.println("WIN");
        int maxIndex = 0;
        for(int i = 0; i < n; i++){
            if(sg[i] > 0){
                maxIndex = i;
            }
        }
        Node end = null;
        for(Node node : nodes){
            if(sg(node) == maxIndex && (node.h ^ sg[maxIndex]) < node.h){
                end = node;
                break;
            }
        }
        end.h ^= sg[maxIndex];
        sg[maxIndex] = 0;
        for(Node node : end.adj){
            node.h ^= sg[sg(node)];
            sg[sg(node)] = 0;
        }
        for(Node node : nodes){
            out.append(node.h).append(' ');
        }
    }

    IntegerVersionArray iva = new IntegerVersionArray((int)1e6);
    public int sg(Node root) {
        if (root.sg == -1) {
            root.sg = 0;
            for (Node node : root.adj) {
                sg(node);
            }
            iva.clear();
            for (Node node : root.adj) {
                iva.set(sg(node), 1);
            }
            while(iva.get(root.sg) == 1){
                root.sg++;
            }
        }
        return root.sg;
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int h;
    int sg = -1;
}
