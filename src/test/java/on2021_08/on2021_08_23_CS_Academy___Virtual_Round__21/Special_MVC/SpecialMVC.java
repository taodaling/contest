package on2021_08.on2021_08_23_CS_Academy___Virtual_Round__21.Special_MVC;



import template.datastructure.DSU;
import template.graph.EdgeMap;
import template.graph.Graph;
import template.graph.TarjanBiconnectedComponent;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

public class SpecialMVC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        EdgeMap em = new EdgeMap(m);
        DSUExt dsu = new DSUExt(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            dsu.merge(a, b);
            dsu.edge[dsu.find(a)]++;
            em.add(a, b, 0);
            Graph.addUndirectedEdge(g, a, b);
        }
        TarjanBiconnectedComponent tbc = new TarjanBiconnectedComponent(n);
        tbc.init(g);
        Pair<WeightedCircleNode[], WeightedSquareNode[]> pair = LongWeightedCircleSquareTree.build(n, tbc, em, i -> dsu.find(i) == i);
        int ans = 0;
        for (WeightedCircleNode node : pair.a) {
            if (node.parentId() == -1) {
                if (dsu.size[node.id] == 1) {
                    if(dsu.edge[node.id] > 0){
                        ans++;
                    }
                    continue;
                }
                dfs(node);
                ans += Math.min(node.dp[0], node.dp[1]);
            }
        }
        out.println(ans);
    }

    public int min(int[][] data) {
        int ans = inf;
        ans = Math.min(ans, data[0][0]);
        ans = Math.min(ans, data[0][1]);
        ans = Math.min(ans, data[1][0]);
        ans = Math.min(ans, data[1][1]);
        return ans;
    }

    public void dfs(WeightedCircleNode root) {
        int[] prev = new int[2];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        prev[1] = 1;
        for (int i = 1; i < root.next.length; i++) {
            WeightedSquareNode node = root.next[i];
            dfs(node);
            prev[0] += node.dp[1][1];
            prev[1] += min(node.dp);
        }
        debug.debug("root", root);
        debug.debug("dp", prev);
        root.dp = prev;
    }

    Debug debug = new Debug(false);

    int inf = (int) 1e8;

    public void dfs(WeightedSquareNode root) {

        //0 not cover, 1 cover, 2 set
        int[][] prev = new int[2][2];
        int[][] next = new int[2][2];
        SequenceUtils.deepFill(prev, inf);
        dfs(root.next[1]);
        prev[0][0] = root.next[1].dp[0];
        prev[1][1] = root.next[1].dp[1];
        for (int i = 2; i < root.next.length; i++) {
            WeightedCircleNode node = root.next[i];
            dfs(node);
            SequenceUtils.deepFill(next, inf);
            for (int t = 0; t < 2; t++) {
                for (int k = 0; k < 2; k++) {
                    for (int j = 0; j < 2; j++) {
                        if (k == 0 && j == 0) {
                            continue;
                        }
                        next[t][j] = Math.min(next[t][j], prev[t][k] + node.dp[j]);
                    }
                }
            }
            int[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        root.dp = prev;
    }
}


class DSUExt extends DSU {
    int[] edge;

    public DSUExt(int n) {
        super(n);
        edge = new int[n];
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        edge[a] += edge[b];
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
    int[] dp;

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
    int[][] dp;

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