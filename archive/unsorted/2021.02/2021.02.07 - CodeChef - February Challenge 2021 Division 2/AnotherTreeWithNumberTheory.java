package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.HashData;
import template.rand.HashSeed;
import template.rand.LongHashData;
import template.rand.ModifiableHash;
import template.utils.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnotherTreeWithNumberTheory {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int i = 1; i < n; i++) {
            Node p = nodes[in.ri() - 1];
            p.adj.add(nodes[i]);
        }
        int q = in.ri();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            Node node = nodes[in.ri() - 1];
            Query query = new Query();
            qs[i] = query;
            query.w = in.ri();
            node.qs.add(query);
        }
        dfsForSize(nodes[0]);
        dsuOnTree(nodes[0]);
        for (Query query : qs) {
            out.println(query.w - query.ans);
        }
    }

    LongHashMap map = new LongHashMap((int) 5e5, false);

    public void dfsForSize(Node root) {
        root.size = 1;
        root.h1 = h1;
        root.h2 = h2;
        consider(root.adj.size());
        for (Node node : root.adj) {
            dfsForSize(node);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
        h1 = root.h1;
        h2 = root.h2;
    }

    int mod = (int) 1e9 + 7;
    int x, y;

    {
        Pair<Integer, Integer> seed = HashSeed.getSeed2();
        x = seed.a;
        y = seed.b;
        hd1 = new HashData((int) 1e6, x, mod);
        hd2 = new HashData((int) 1e6, y, mod);
    }

    long h1;
    long h2;
    HashData hd1;
    HashData hd2;
    IntegerArrayList primeFactors = new IntegerArrayList();
    int[] primes;
    IntegerMultiWayStack stack = Factorization.factorizeRangePrime((int) 1e6);

    void consider(int n) {
        for (IntegerIterator iterator = stack.iterator(n); iterator.hasNext(); ) {
            int x = iterator.next();
            int p = 0;
            while (true) {
                int t = n / x;
                if (t * x != n) {
                    break;
                }
                p++;
                n = t;
            }
            h1 += (long) hd1.pow[x] * p;
            h2 += (long) hd2.pow[x] * p;
        }
        h1 = DigitUtils.modWithoutDivision(h1, mod);
        h2 = DigitUtils.modWithoutDivision(h2, mod);
    }


    public int count(int i, int w) {
        if (i < 0) {
            h1 = DigitUtils.modWithoutDivision(h1, mod);
            h2 = DigitUtils.modWithoutDivision(h2, mod);
            return w * (int) map.getOrDefault(DigitUtils.asLong(h1, h2), 0);
        }
        int ans = count(i - 1, w);
        int p = 0;
        while (true) {
            int t = w / primes[i];
            if (t * primes[i] != w) {
                break;
            }
            p++;
            w = t;
            h1 += hd1.pow[primes[i]];
            h2 += hd2.pow[primes[i]];
            ans += count(i - 1, w);
        }
        h1 += (long) -p * hd1.pow[primes[i]];
        h2 += (long) -p * hd2.pow[primes[i]];
        return ans;
    }

    public void contribute(Node root) {
        if (root.heavy == null) {
            map.modify(DigitUtils.asLong(root.h1, root.h2), 1);
            return;
        }
        for (Node node : root.adj) {
            contribute(node);
        }
    }

    public void dsuOnTree(Node root) {
        for (Node node : root.adj) {
            if (node == root.heavy) {
                continue;
            }
            dsuOnTree(node);
            map.clear();
        }
        if (root.heavy != null) {
            dsuOnTree(root.heavy);
        } else {
            map.modify(DigitUtils.asLong(root.h1, root.h2), 1);
        }

        for (Node node : root.adj) {
            if (node == root.heavy) {
                continue;
            }
            contribute(node);
        }
        for (Query q : root.qs) {
            primeFactors.clear();
            primeFactors.addAll(stack.iterator(q.w));
            primes = primeFactors.getData();
            h1 = root.h1;
            h2 = root.h2;
            q.ans = count(primeFactors.size() - 1, q.w);
        }
    }
}

class Query {
    int w;
    int ans;
}

class Node {
    List<Node> adj = new ArrayList<>();
    Node heavy;
    int size;
    long h1;
    long h2;
    List<Query> qs = new ArrayList<>();
}