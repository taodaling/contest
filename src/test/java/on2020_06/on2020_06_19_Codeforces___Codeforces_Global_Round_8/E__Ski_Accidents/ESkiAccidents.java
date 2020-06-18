package on2020_06.on2020_06_19_Codeforces___Codeforces_Global_Round_8.E__Ski_Accidents;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class ESkiAccidents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int limit = 4 * n / 7;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
            a.to.add(b);
            a.sum++;
            b.sum++;
        }

        TreeSet<Node> set = new TreeSet<>((a, b) -> a.sum == b.sum ? Integer.compare(a.id, b.id) :
                Integer.compare(a.sum, b.sum));
        set.addAll(Arrays.asList(nodes));


        List<Node> deletion = new ArrayList<>(n);
        while (set.last().sum >= 3) {
            Node last = set.pollLast();
            deletion.add(last);
            last.removed = true;
            for (Node node : last.adj) {
                if (node.removed) {
                    continue;
                }
                set.remove(node);
                node.sum--;
                set.add(node);
            }
        }

        for (int i = 0; i < n; i++) {
            find(nodes[i], 0, deletion);
        }

        if (deletion.size() <= limit) {
            out.println(deletion.size());
            for (Node node : deletion) {
                out.append(node.id + 1).append(' ');
            }
            out.println();
            return;
        }

        //shit random
        while(true){
            Randomized.shuffle(nodes, 0, n);
            for(Node node : nodes){
                node.removed = false;
            }
            for(Node node : nodes){
                boolean prev = false;
                boolean next = false;
                for(Node to : node.to){
                    if(!to.removed){
                        prev = true;
                        break;
                    }
                }
                for(Node from : node.prev){
                    if(!from.removed){

                    }
                }
            }
        }
    }

    private void find(Node root, int depth, List<Node> deletion) {
        if (root.removed) {
            return;
        }
        root.removed = true;
        if (depth % 3 == 2) {
            deletion.add(root);
        }
        for (Node out : root.to) {
            find(out, depth + 1, deletion);
        }
    }
}

class Node {
    boolean removed;
    int id;
    List<Node> to = new ArrayList<>();
    List<Node> prev = new ArrayList<>();
    List<Node> adj = new ArrayList<>();
    int sum;
    boolean visited;
}
