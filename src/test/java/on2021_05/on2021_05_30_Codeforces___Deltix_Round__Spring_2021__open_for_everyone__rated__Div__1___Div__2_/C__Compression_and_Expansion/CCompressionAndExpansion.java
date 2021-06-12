package on2021_05.on2021_05_30_Codeforces___Deltix_Round__Spring_2021__open_for_everyone__rated__Div__1___Div__2_.C__Compression_and_Expansion;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CCompressionAndExpansion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();

        List<Node> seq = new ArrayList<>(n);
        Deque<Node> dq = new ArrayDeque<>(n);
        end = new Node(null, -1);
        dq.addLast(end);
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            while (true) {
                Node last = dq.peekLast();
                if (last.nextChild != x) {
                    dq.removeLast();
                    continue;
                }
                last.nextChild++;
                dq.addLast(new Node(dq.peekLast(), x));
                break;
            }
            seq.add(dq.peekLast());
        }

        this.out = out;
        for (Node node : seq) {
            print(node);
            out.println();
        }
    }

    Node end = new Node(null, -1);
    FastOutput out;

    void print(Node root) {
        if (root.p != end) {
            print(root.p);
            out.append('.');
        }
        out.append(root.index);
    }
}

class Node {
    Node p;
    int index;
    int nextChild = 1;

    public Node(Node p, int index) {
        this.p = p;
        this.index = index;
    }
}
