package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

import java.util.HashMap;
import java.util.Map;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int l = in.readInt();

        Node[] n1 = new Node[n + 1];
        Node[] n2 = new Node[n + 1];
        for(int i = 1; i <= n; i++){
            n1[i] = new Node();
            n2[i] = new Node();
        }

        for(int i = 0; i < k; i++){
            Node a = n1[in.readInt()];
            Node b = n1[in.readInt()];
            Node.merge(a, b);
        }

        for(int i = 0; i < l; i++){
            Node a = n2[in.readInt()];
            Node b = n2[in.readInt()];
            Node.merge(a, b);
        }

        int g1 = 0;
        int g2 = 0;
        for(int i = 1; i <= n; i++){
            if(n1[i].find() == n1[i]){
                n1[i].group = g1++;
            }
            if(n2[i].find() == n2[i]){
                n2[i].group = g2++;
            }
        }

        Map<Long, Integer> cntMap = new HashMap<>(n);
        for(int i = 1; i <= n; i++){
            Long g = DigitUtils.asLong(n1[i].find().group,
                    n2[i].find().group);
            cntMap.put(g, cntMap.getOrDefault(g, 0) + 1);
        }

        for(int i = 1; i <= n; i++){
            Long g = DigitUtils.asLong(n1[i].find().group,
                    n2[i].find().group);
            int cnt = cntMap.get(g);
            out.append(cnt).append(' ');
        }
    }
}


class Node {
    int group;
    Node p = this;
    int rank;

    public Node find() {
        return p.p == p ? p : (p = p.find());
    }

    public static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }
}
