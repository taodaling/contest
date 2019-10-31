package contest;

import java.util.ArrayList;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class Cleaning {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].stone = in.readInt();
        }

        for(int i = 1; i < n; i++){
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        if(n == 2){
            out.println(nodes[1].stone == nodes[2].stone ? "YES" : "NO");
            return;
        }

        for(int i = 1; i <= n; i++){
            if(nodes[i].next.size() >= 2){
                SequenceUtils.swap(nodes, 1, i);
                break;
            }
        }
        dfs(nodes[1], null);
        out.println(valid && nodes[1].stone == 0 ? "YES" : "NO");
    }

    boolean valid = true;
    public void dfs(Node root, Node father){
        root.next.remove(father);
        if(root.next.isEmpty()){
            return;
        }
        long max = 0;
        for(Node node : root.next){
            dfs(node, root);
            root.sum += node.stone;
            max = Math.max(node.stone, max);
        }
        long k = root.sum - root.stone;
        if(k < 0 || root.sum - max < k){
            valid = false;
        }
        root.stone = root.sum - 2 * k;
        if(root.stone < 0){
            valid = false;
        }
    }


}


class Node {
    List<Node> next = new ArrayList<>();
    long stone;
    long sum;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
