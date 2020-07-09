package contest;

import template.math.EulerSieve;
import template.math.IntRadix;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PrimeSubstrings {
    int limit = (int) 1e7;

    public String construct(int N, int L) {
        EulerSieve sieve = new EulerSieve((int) 1e7);

        for (int i = 0; i < sieve.getPrimeCount(); i++) {
            int p = sieve.get(i);
            if (!containZero(p) && digit(p) == L) {
                nodes[p] = new Node();
                nodes[p].val = p;
            }
        }

        for (int i = 0; i < limit; i++) {
            if (nodes[i] != null) {
                for (int j = 1; j < 10; j++) {
                    int next = radix.set(i, L - 1, 0) * 10 + j;
                    if (nodes[next] != null) {
                        nodes[i].adj.add(nodes[next]);
                    }
                }
            }
        }

        for (int i = 0; i < limit; i++) {
            if (nodes[i] != null) {
                if (findCircle(nodes[i])) {
                    break;
                }
            }
        }

        StringBuilder ans = new StringBuilder();
        ans.append(dq.peekFirst().val);
        dq.addLast(dq.removeFirst());
        while (ans.length() < N) {
            ans.append(dq.peekFirst().val % 10);
            dq.addLast(dq.removeFirst());
        }
        return ans.toString();
    }

    IntRadix radix = new IntRadix(10);
    Node[] nodes = new Node[(int) limit];
    Deque<Node> dq = new ArrayDeque<>();

    public boolean findCircle(Node root) {
        if (root.visited) {
            if (root.instk) {
                while (dq.peekFirst() != root) {
                    dq.removeFirst();
                }
                return true;
            }
            return false;
        }
        root.visited = root.instk = true;
        dq.addLast(root);
        for (Node node : root.adj) {
            if (findCircle(node)) {
                return true;
            }
        }
        dq.removeLast();
        root.instk = false;
        return false;
    }

    public int digit(int x) {
        if (x == 0) {
            return 0;
        }
        return 1 + digit(x / 10);
    }

    public boolean containZero(int x) {
        if (x == 0) {
            return false;
        }
        return x % 10 == 0 || containZero(x / 10);
    }

}

class Node {
    List<Node> adj = new ArrayList();
    int val;
    boolean visited;
    boolean instk;
}