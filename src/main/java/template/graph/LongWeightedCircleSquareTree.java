package template.graph;

import template.utils.Pair;
import template.utils.SequenceUtils;

import java.util.function.IntPredicate;

/**
 * input graph must be a cactus
 */
public class LongWeightedCircleSquareTree {
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

    public int parentId() {
        return next[0] == null ? -1 : next[0].id;
    }
}

/**
 * parent located at next[0]
 */
class WeightedSquareNode {
    public WeightedCircleNode[] next;
    public long length;
    public int id;

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
}