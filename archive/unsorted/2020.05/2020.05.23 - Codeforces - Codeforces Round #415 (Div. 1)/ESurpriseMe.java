package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.ArrayList;
import java.util.List;

public class ESurpriseMe {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        gs = new IntegerList(n);
        sum = new int[n + 1];
        fixSum = new int[n + 1];
        curSum = new int[n + 1];
        curFixSum = new int[n + 1];
        added = new boolean[n + 1];
        Node[] nodes = new Node[n];

        IntegerMultiWayStack primeStack = Factorization.factorizeRangePrime(n);
        IntegerList primes = new IntegerList(6);
        IntegerList factors = new IntegerList(64);
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].x = in.readInt();
            primes.clear();
            primes.addAll(primeStack.iterator(nodes[i].x));
            nodes[i].primes = primes.toArray();
            factors.clear();
            enumerate(nodes[i].primes, nodes[i].primes.length - 1, factors, 1);
            nodes[i].factors = factors.toArray();
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }


    }

    public void enumerate(int[] prime, int i, IntegerList factors, int built) {
        if (i == -1) {
            factors.add(built);
            return;
        }
        enumerate(prime, i - 1, factors, built);
        enumerate(prime, i - 1, factors, built * prime[i]);
    }

    public void dfsForSize(Node root, Node p) {
        root.size = 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForSize(node, root);
            root.size += node.size;
        }
    }

    public Node dfsForCentroid(Node root, Node p, int total) {
        int max = total - root.size;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            max = Math.max(max, node.size);
            Node ans = dfsForCentroid(node, root, total);
            if (ans != null) {
                return ans;
            }
        }
        if (max * 2 <= total) {
            return root;
        }
        return null;
    }

    public void dfsForDepth(Node root, Node p, int depth){
        root.depth = depth;
        for(Node node : root.adj){
            if(node == p){
                continue;
            }
            dfsForDepth(node, root, depth + 1);
        }
    }

    public void dfsForAdd(Node root, Node p, int sign) {
        int ordinal = mod.mul(sieve.euler[root.x], sign);
        int
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForAdd(root, p, sign);
        }
    }

    IntegerList gs;
    int[] sum;
    int[] fixSum;
    int[] curSum;
    int[] curFixSum;
    boolean[] added;
    int limit = (int) 2e5;
    Modular mod = new Modular(1e9 + 7);
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(limit, false, true, false);
    InverseNumber inv = new ModPrimeInverseNumber(limit, mod);

    public void dac(Node root) {
        dfsForSize(root, null);
        root = dfsForCentroid(root, null, root.size);

    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int x;
    int size;
    int depth;

    int[] primes;
    int[] factors;
}
