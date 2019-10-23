
package contest;

import template.FastInput;
import template.FastOutput;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] participant = new Node[n];
        for(int i = 0; i < n; i++){
            participant[i] = new Node();
        }
        Node[] language = new Node[m];
        for(int i = 0; i < m; i++){
            language[i] = new Node();
        }

        for(int i = 0; i < n; i++){
            int k = in.readInt();
            for(int j = 0; j < k; j++){
                Node l = language[in.readInt() - 1];
                Node.merge(participant[i], l);
            }
        }

        for(int i = 1; i < n; i++){
            if(participant[0].find() != participant[i].find()){
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}


class Node {
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
        if(a.rank > b.rank){
            b.p = a;
        }else{
            a.p = b;
        }
    }
}
