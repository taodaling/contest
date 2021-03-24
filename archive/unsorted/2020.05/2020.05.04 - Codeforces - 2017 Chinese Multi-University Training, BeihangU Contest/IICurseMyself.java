package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.*;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.primitve.generated.graph.IntegerWeightUndirectedEdge;
import template.problem.KthSmallestCardGroup;
import template.rand.Randomized;
import template.utils.SortUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.*;

public class IICurseMyself {
    long intToLongMask = (1L << 32) - 1;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);

        int n = in.readInt();
        int m = in.readInt();

        DSU dsu = new DSU(n);
        List<int[]> nonTreeEdges = new ArrayList<>(m);
        dq = new ArrayDeque<>(m);
        g = IntegerWeightGraph.createUndirectedGraph(n);
        int sum = 0;
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int z = in.readInt();
            sum += z;
            if (dsu.find(a) != dsu.find(b)) {
                dsu.merge(a, b);
                IntegerWeightGraph.addUndirectedEdge(g, a, b, z);
                continue;
            }

            nonTreeEdges.add(new int[]{a, b, z});
        }

        List<long[]> groups = new ArrayList<>(m);
        for (int[] e : nonTreeEdges) {
            dq.clear();
            collect(e[0], -1, e[1]);
            long[] g = new long[dq.size() + 1];
            int head = 0;
            g[head++] = -e[2];
            while (!dq.isEmpty()) {
                g[head++] = -dq.removeFirst().weight;
            }
            groups.add(g);
        }

        int k = in.readInt();

        KthSmallestCardGroup cg = new KthSmallestCardGroup(groups.toArray(new long[0][]));
        LongList list = cg.theFirstKSmallestSet(k);

        long ans = 0;
        for (int i = 0; i < list.size(); i++) {
            long sub = -list.get(i);
            ans += (long) (i + 1) * (sum - sub);
            ans &= intToLongMask;
        }

        out.println(ans);
    }

    List<IntegerWeightUndirectedEdge>[] g;
    Deque<IntegerWeightUndirectedEdge> dq;

    private boolean collect(int root, int p, int target) {
        if (root == target) {
            return true;
        }
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dq.addLast(e);

            if (collect(e.to, root, target)) {
                return true;
            }

            dq.removeLast();
        }
        return false;
    }
}

