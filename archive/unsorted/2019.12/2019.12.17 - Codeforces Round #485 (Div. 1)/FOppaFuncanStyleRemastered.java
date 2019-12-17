package contest;

import template.datastructure.LongList;
import template.datastructure.PairingHeap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ExtGCD;
import template.math.ILongModular;
import template.math.LongModularDanger;
import template.math.LongPollardRho;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FOppaFuncanStyleRemastered {
    LongPollardRho pollardRho = new LongPollardRho();
    ExtGCD extGCD = new ExtGCD();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.readInt();
        Query[] queries = new Query[t];
        for (int i = 0; i < t; i++) {
            queries[i] = new Query();
            queries[i].n = in.readLong();
            queries[i].k = in.readLong();
        }
        Map<Long, List<Query>> map = Arrays.stream(queries).collect(Collectors.groupingBy(a -> a.k));
        for (Map.Entry<Long, List<Query>> entry : map.entrySet()) {
            handle(entry.getKey(), entry.getValue());
        }

        for (Query q : queries) {
            out.println(q.ans ? "YES" : "NO");
        }
    }


    long inf = (long) 1e18;
    LongList collector = new LongList(30000);
    public void handle(long k, List<Query> queries) {
        Map<Long, Long> allPrimeFactors = pollardRho.findAllFactors(k, true);
        long[][] pairs = allPrimeFactors.entrySet().stream().map(x -> new long[]{x.getKey(), x.getValue()})
                .toArray(n -> new long[n][]);

        if (pairs.length == 0) {
            for (Query q : queries) {
                q.ans = false;
            }
            return;
        }
        if (pairs.length == 1) {
            for (Query q : queries) {
                q.ans = q.n % pairs[0][0] == 0;
            }
            return;
        }

        if (pairs.length == 2) {
            ILongModular modular = ILongModular.getInstance(pairs[1][0]);
            for (Query q : queries) {
                long g = extGCD.extgcd(pairs[0][0], pairs[1][0]);
                if (q.n % g != 0) {
                    q.ans = false;
                    continue;
                }
                long x = modular.mul(modular.valueOf(extGCD.getX()), modular.valueOf(q.n));
                q.ans = !DigitUtils.isMultiplicationOverflow(x, pairs[0][0], q.n);
            }
            return;
        }


        collector.clear();
        for (long[] p : pairs) {
            collector.add(p[0]);
        }
        collector.unique();
        long[] allFactors = collector.getData();
        int m = collector.size();
        int min = (int) allFactors[0];
        //System.err.println(min + "-" + allFactors.length);
        Node[] nodes = new Node[min];
        PairingHeap<Node> heap = PairingHeap.NIL;
        for (int i = 0; i < min; i++) {
            nodes[i] = new Node();
            nodes[i].remain = i;
            nodes[i].dist = i == 0 ? 0 : inf;
            nodes[i].heap = new PairingHeap<>(nodes[i]);
            heap = PairingHeap.merge(heap, nodes[i].heap, Node.sortByDist);
        }
        //TreeSet<Node> set = new TreeSet<>(Node.sortByDist);
        //set.add(nodes[0]);
        while (!PairingHeap.isEmpty(heap)) {
            Node node = PairingHeap.peek(heap);
            heap = PairingHeap.pop(heap, Node.sortByDist);
            for (int i = 0; i < m; i++) {
                long d = allFactors[i];
                Node next = nodes[DigitUtils.mod(node.remain + d, min)];
                if (next.dist <= node.dist + d) {
                    continue;
                }
                //set.remove(next);
                next.dist = node.dist + d;
                //set.add(next);
                heap = PairingHeap.decrease(heap, next.heap, next, Node.sortByDist);
            }
        }

        for (Query q : queries) {
            Node node = nodes[DigitUtils.mod(q.n, min)];
            q.ans = node.dist <= q.n;
        }
    }

    private void getAllFactors(LongList list, long[][] pairs, int i, long val) {
        if (i == pairs.length) {
            list.add(val);
            return;
        }
        for (long t = 1; ; t *= pairs[i][0]) {
            getAllFactors(list, pairs, i + 1, val * t);
            if (t >= pairs[i][1]) {
                break;
            }
        }
    }
}

class Node {
    long dist;
    int remain;
    PairingHeap<Node> heap;

    static Comparator<Node> sortByDist = (a, b) -> a.dist == b.dist ? a.remain - b.remain : Long.compare(a.dist, b.dist);
}

class Query {
    long n;
    long k;
    boolean ans;
}