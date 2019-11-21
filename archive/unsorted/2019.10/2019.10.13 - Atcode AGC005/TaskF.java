package contest;

import template.FastInput;
import template.Memory;
import template.NumberTheoryTransform;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskF {
    Modular mod = new Modular(924844033);
    Factorial fact = new Factorial(500000, mod);
    Log2 log2 = new Log2();
    NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 5);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt()];
            Node b = nodes[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        int[] coe = new int[n + 1];
        coe[n] = 1;
        dfsForSize(nodes[1], null);
        dfsForCoe(nodes[1], nodes[1].size, coe);

        int m = log2.ceilLog(n + 1) + 1;
        int properLen = 1 << m;
        int[] f = new int[properLen];
        int[] g = new int[properLen];
        int[] r = new int[properLen];
        for (int i = 0; i <= n; i++) {
            f[i] = mod.mul(coe[i], fact.fact(i));
            g[i] = fact.invFact(i);
        }
        Memory.reverse(f, 0, n + 1);

        ntt.prepareReverse(r, m);

        ntt.dft(r, f, m);
        ntt.dft(r, g, m);
        ntt.dotMul(f, g, f, m);
        ntt.idft(r, f, m);
        Memory.reverse(f, 0, n + 1);

        for (int i = 1; i <= n; i++) {
            int c = mod.mul(f[i], fact.invFact(i));
            out.println(c);
        }
    }

    public void dfsForCoe(Node root, int total, int[] coe) {
        for (Node node : root.next) {
            dfsForCoe(node, total, coe);
            coe[node.size] = mod.subtract(coe[node.size], 1);
            coe[total - node.size] = mod.subtract(coe[total - node.size], 1);
            coe[total] = mod.plus(coe[total], 1);
        }
    }

    public void dfsForSize(Node root, Node fa) {
        root.next.remove(fa);
        root.size = 1;
        for (Node node : root.next) {
            dfsForSize(node, root);
            root.size += node.size;
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>(2);
    int size;
}