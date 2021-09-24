package on2021_07.on2021_07_19_DMOJ___Wesley_s_Anger_Contest_5.Problem_6___Squirrel_Cities_2;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntPredicate;

public class Problem6SquirrelCities2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int q = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        ParentOnTree potOld = new ParentOnTreeByDfs(g, i -> i == 0);
        DepthOnTree dotOld = new DepthOnTreeByParent(n, potOld);
//        LcaOnTree lcaOld = new LcaOnTreeBySchieberVishkin(g, 0);
        CompressedBinaryLift lcaOld = new CompressedBinaryLift(n, dotOld, potOld);
        DistanceOnTree distOnTreeOld = new DistanceOnTreeByLcaAndDepth(lcaOld, dotOld);

        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }

        TarjanBiconnectedComponent bc = new TarjanBiconnectedComponent(n);
        bc.init(g);
        Pair<WeightedCircleNode[], WeightedSquareNode[]> pair = LongWeightedCircleSquareTree.build(n, bc, EdgeMap.ofUnitUndirectedGraph(), i -> i == 0);
        WeightedSquareNode[] sn = pair.b;
        WeightedCircleNode[] cn = pair.a;
        int[] p = new int[sn.length + cn.length];
        for (int i = 0; i < cn.length; i++) {
            p[i] = cn[i].parentId();
        }
        for (int i = 0; i < sn.length; i++) {
            p[i + cn.length] = sn[i].parentId();
        }
        ParentOnTree pot = new ParentOnTreeByGivenArray(p);
        DepthOnTree dot = new DepthOnTreeByParent(p.length, pot);
        CompressedBinaryLift bl = new CompressedBinaryLift(p.length, dot, pot);

        long totalOld = 0;
        for (int i = 0; i < q; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            totalOld += distOnTreeOld.distance(u, v);

            int lca = bl.lca(u, v);
            cn[u].pass++;
            cn[v].pass++;
            if (lca < n) {
                //all node plus
                cn[lca].pass -= 2;
            } else {
                int ul = bl.kthAncestor(u, dot.depth(u) - dot.depth(lca) - 1);
                int vl = bl.kthAncestor(v, dot.depth(v) - dot.depth(lca) - 1);
                cn[ul].pass--;
                cn[vl].pass--;
                sn[lca - n].optimize += distOnTreeOld.distance(ul, vl) - sn[lca - n].distance(cn[ul], cn[vl]);
            }
        }
        dfs(cn[0]);
        for (WeightedCircleNode node : cn) {
            if (node.next[0] == null || node.next[0].next.length <= 2 || node.pass == 0) {
                continue;
            }
            WeightedSquareNode parent = node.next[0];
            WeightedCircleNode grandparent = parent.next[0];
            parent.optimize += (distOnTreeOld.distance(node.id, grandparent.id) - (node.depth - grandparent.depth)) * node.pass;
        }
        WeightedSquareNode[] sorted = sn.clone();
        Arrays.sort(sorted, Comparator.comparingLong(x -> -x.optimize));
        long optimizeSum = 0;
        for (int i = 0; i < sorted.length && i < k; i++) {
            optimizeSum += sorted[i].optimize;
        }
        out.println(totalOld - optimizeSum);
    }

    public void dfs(WeightedSquareNode root) {
        for (int i = 1; i < root.next.length; i++) {
            WeightedCircleNode node = root.next[i];
            dfs(node);
            root.pass += node.pass;
        }
    }

    public void dfs(WeightedCircleNode root) {
        for (int i = 1; i < root.next.length; i++) {
            WeightedSquareNode node = root.next[i];
            dfs(node);
            root.pass += node.pass;
        }
    }
}

class LongWeightedCircleSquareTree {
    public static Pair<WeightedCircleNode[], WeightedSquareNode[]> build(int n, TarjanBiconnectedComponent bc, EdgeMap em, IntPredicate isRoot) {
        int numSquare = bc.vcc.size();
        int[] cnts = bc.occur.clone();
        for (int i = 0; i < n; i++) {
            if (isRoot.test(i)) {
                cnts[i]++;
            }
        }
        WeightedCircleNode[] circleNodes = new WeightedCircleNode[n];
        for (int i = 0; i < n; i++) {
            WeightedCircleNode node = new WeightedCircleNode();
            node.id = i;
            node.next = new WeightedSquareNode[cnts[i]];
            circleNodes[i] = node;
        }
        WeightedSquareNode[] squareNodes = new WeightedSquareNode[numSquare];
        for (int i = 0; i < numSquare; i++) {
            int[] vs = bc.vcc.get(i);
            WeightedSquareNode node = new WeightedSquareNode();
            node.next = new WeightedCircleNode[vs.length];
            node.id = i + n;
            for (int j = 0; j < vs.length; j++) {
                node.next[j] = circleNodes[vs[j]];
                circleNodes[vs[j]].next[--cnts[vs[j]]] = node;
            }
            squareNodes[i] = node;
        }
        for (int i = 0; i < n; i++) {
            if (isRoot.test(i)) {
                dfs(circleNodes[i], null, em);
            }
        }
        return new Pair<>(circleNodes, squareNodes);
    }

    private static <T> void findAndRotateToFirst(T[] data, T target) {
        int index = SequenceUtils.indexOf(data, 0, data.length - 1, target);
        SequenceUtils.rotate(data, 0, data.length - 1, index);
    }

    private static void dfs(WeightedCircleNode root, WeightedSquareNode p, EdgeMap map) {
        findAndRotateToFirst(root.next, p);
        for (WeightedSquareNode node : root.next) {
            if (node == p) {
                continue;
            }
            dfs(node, root, map);
        }
    }


    private static void dfs(WeightedSquareNode root, WeightedCircleNode p, EdgeMap map) {
        findAndRotateToFirst(root.next, p);
        for (int i = 1; i < root.next.length; i++) {
            long w = map.get(root.next[i - 1].id, root.next[i].id);
            root.length += w;
            root.next[i].offset += root.length;
        }
        root.length += map.get(root.next[0].id, root.next[root.next.length - 1].id);
        for (WeightedCircleNode node : root.next) {
            if (node == p) {
                continue;
            }
            node.depth = p.depth + root.distance(p, node);
            dfs(node, root, map);
        }
    }
}

/**
 * parent located at next[0]
 */
class WeightedCircleNode {
    public WeightedSquareNode[] next;
    public int id;
    public long depth;
    public long offset;
    public int pass;

    public int parentId() {
        return next[0] == null ? -1 : next[0].id;
    }

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}

/**
 * parent located at next[0]
 */
class WeightedSquareNode {
    public WeightedCircleNode[] next;
    public long length;
    public int id;
    public long optimize;
    public int pass;

    public int parentId() {
        return next[0] == null ? -1 : next[0].id;
    }

    public long mod(long x) {
        if (x < 0) {
            x += length;
        }
        return x;
    }

    public long distance(WeightedCircleNode a, WeightedCircleNode b) {
        long oa = a == next[0] ? 0 : a.offset;
        long ob = b == next[0] ? 0 : b.offset;
        return Math.min(mod(oa - ob), mod(ob - oa));
    }

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}