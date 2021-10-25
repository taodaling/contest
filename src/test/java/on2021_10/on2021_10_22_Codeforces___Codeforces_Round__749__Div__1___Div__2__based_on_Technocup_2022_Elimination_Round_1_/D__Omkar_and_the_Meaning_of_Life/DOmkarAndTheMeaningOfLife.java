package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.D__Omkar_and_the_Meaning_of_Life;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DOmkarAndTheMeaningOfLife {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            int[] a = new int[n];
            a[i] = 1;
            int res = ask(a);
            if (res < 0 || res == i) {
                continue;
            }
            concat(nodes[i], nodes[res]);
        }
        for (int i = 0; i < n; i++) {
            int[] a = new int[n];
            Arrays.fill(a, 1);
            a[i] = 0;
            int res = ask(a);
            if (res < 0 || res == i) {
                continue;
            }
            concat(nodes[res], nodes[i]);
        }
        int idAlloc = 0;
        for(Node node : nodes){
            if(node.prev == null){
                for(Node trace = node; trace != null; trace = trace.next){
                    trace.val = ++idAlloc;
                }
            }
        }
        out.append("!");
        for (Node node : nodes) {
            out.append(' ').append(node.val);
        }
        out.println().flush();
    }

    Debug debug = new Debug(false);

    public void concat(Node a, Node b) {
        assert a.next == null && b.prev == null;
        a.next = b;
        b.prev = a;
    }

    FastInput in;
    FastOutput out;

    public int ask(int[] a) {
        out.append("?");
        for (int x : a) {
            out.append(' ').append(x + 1);
        }
        out.println().flush();
        return in.ri() - 1;
    }
}

class Node {
    int id;
    int val;
    Node prev;
    Node next;

    @Override
    public String toString() {
        return ("" + (id + 1)) + ", " + (next == null ? "" : next.toString());
    }
}