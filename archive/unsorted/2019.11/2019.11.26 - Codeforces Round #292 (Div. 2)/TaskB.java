package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Gcd;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Node[] boys = new Node[n];
        Node[] girls = new Node[m];
        for (int i = 0; i < n; i++) {
            boys[i] = new Node();
        }
        for (int i = 0; i < m; i++) {
            girls[i] = new Node();
        }
        int b = in.readInt();
        for (int i = 0; i < b; i++) {
            boys[in.readInt()].happy = true;
        }
        int g = in.readInt();
        for (int i = 0; i < g; i++) {
            girls[in.readInt()].happy = true;
        }
        int gcd = new Gcd().gcd(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int abs = Math.abs(i - j);
                if (abs % gcd == 0) {
                    Node.merge(boys[i], girls[j]);
                }
            }
        }
        boolean allHappy = true;
        for(int i = 0; i < n; i++){
            allHappy = allHappy && boys[i].find().happy;
        }
        out.println(allHappy ? "Yes" : "No");
    }
}

class Node {
    boolean happy;
    Node p = this;
    int rank;

    Node find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Node a, Node b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (b.rank > a.rank) {
            Node tmp = a;
            a = b;
            b = tmp;
        }
        b.p = a;
        a.happy = a.happy || b.happy;
    }
}
