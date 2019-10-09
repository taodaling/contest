package template;

import java.util.Comparator;
import java.util.Random;

public class RankArray {
    public RankArray(Comparator cmp) {
        this.cmp = cmp;
    }

    private static class TreapNode implements Cloneable {
        private static Random random = new Random();

        private static TreapNode NIL = new TreapNode();

        static {
            NIL.left = NIL.right = NIL;
        }

        TreapNode left = NIL;
        TreapNode right = NIL;
        int cnt;
        int size;
        int nodeNum;
        Object key;

        public void pushDown() {
        }

        public void pushUp() {
            size = left.size + right.size + cnt;
            nodeNum = left.nodeNum + right.nodeNum + 1;
        }

        private static TreapNode[] splitLE(TreapNode root, Object key, Comparator cmp) {
            if (root == NIL) {
                return new TreapNode[]{NIL, NIL};
            }
            root.pushDown();
            TreapNode[] trees;
            if (cmp.compare(root.key, key) > 0) {
                trees = splitLE(root.left, key, cmp);
                root.left = trees[1];
                trees[1] = root;
            } else {
                trees = splitLE(root.right, key, cmp);
                root.right = trees[0];
                trees[0] = root;
            }
            root.pushUp();
            return trees;
        }

        private static TreapNode[] splitL(TreapNode root, Object key, Comparator cmp) {
            if (root == NIL) {
                return new TreapNode[]{NIL, NIL};
            }
            root.pushDown();
            TreapNode[] trees;
            if (cmp.compare(root.key, key) >= 0) {
                trees = splitL(root.left, key, cmp);
                root.left = trees[1];
                trees[1] = root;
            } else {
                trees = splitL(root.right, key, cmp);
                root.right = trees[0];
                trees[0] = root;
            }
            root.pushUp();
            return trees;
        }

        private static TreapNode merge(TreapNode a, TreapNode b, Comparator cmp) {
            if (a == NIL) {
                return b;
            }
            if (b == NIL) {
                return a;
            }
            if (random.nextBoolean()) {
                TreapNode tmp = a;
                a = b;
                b = tmp;
            }
            a.pushDown();
            if (cmp.compare(a.key, b.key) >= 0) {
                a.left = merge(a.left, b, cmp);
            } else {
                a.right = merge(a.right, b, cmp);
            }
            a.pushUp();
            return a;
        }

        public static void toString(TreapNode root, StringBuilder builder) {
            if (root == NIL) {
                return;
            }
            toString(root.left, builder);
            builder.append(root.key).append(',');
            toString(root.right, builder);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder().append(key).append(":");
            toString(this, builder);
            return builder.toString();
        }
    }

    private TreapNode root = TreapNode.NIL;
    private Comparator cmp;

    public int size() {
        return root.size;
    }

    public int distinctSize() {
        return root.nodeNum;
    }

    /**
     * How many item less than x
     */
    public int rankOf(Object x) {
        TreapNode[] part = TreapNode.splitL(root, x, cmp);
        int result = part[0].size;
        root = TreapNode.merge(part[0], part[1], cmp);
        return result;
    }

    /**
     * Return the x-th element, remember the 0-th is the smallest element, just like an array
     */
    public Object elementWithRank(int x) {
        if (x < 0 || x >= size()) {
            return null;
        }
        TreapNode trace = root;
        while (true) {
            trace.pushDown();
            if (trace.left.size > x) {
                trace = trace.left;
            } else {
                x -= trace.size - trace.right.size;
                if (x < 0) {
                    break;
                } else {
                    trace = trace.right;
                }
            }
        }
        return trace.key;
    }

    /**
     * How many x are stored in this array
     */
    public int countOf(Object x) {
        TreapNode[] part1 = TreapNode.splitLE(root, x, cmp);
        TreapNode[] part2 = TreapNode.splitL(part1[0], x, cmp);
        int result = part2[1].cnt;
        part1[0] = TreapNode.merge(part2[0], part2[1], cmp);
        root = TreapNode.merge(part1[0], part1[1], cmp);
        return result;
    }

    /**
     * How many item less than x
     */
    public boolean exist(Object x) {
        TreapNode[] part1 = TreapNode.splitLE(root, x, cmp);
        TreapNode[] part2 = TreapNode.splitL(part1[0], x, cmp);
        boolean result = part2[1] != TreapNode.NIL;
        part1[0] = TreapNode.merge(part2[0], part2[1], cmp);
        root = TreapNode.merge(part1[0], part1[1], cmp);
        return result;
    }

    public void add(Object x) {
        TreapNode[] part1 = TreapNode.splitLE(root, x, cmp);
        TreapNode[] part2 = TreapNode.splitL(part1[0], x, cmp);
        if (part2[1] == TreapNode.NIL) {
            part2[1] = new TreapNode();
            part2[1].key = x;
            part2[1].nodeNum = 1;
        }
        part2[1].cnt++;
        part2[1].pushUp();
        part1[0] = TreapNode.merge(part2[0], part2[1], cmp);
        root = TreapNode.merge(part1[0], part1[1], cmp);
    }

    public void remove(Object x) {
        TreapNode[] part1 = TreapNode.splitLE(root, x, cmp);
        TreapNode[] part2 = TreapNode.splitL(part1[0], x, cmp);
        part2[1] = TreapNode.NIL;
        part1[0] = TreapNode.merge(part2[0], part2[1], cmp);
        root = TreapNode.merge(part1[0], part1[1], cmp);
    }

    /**
     * Remove part which is less than or equal to x and return
     */
    public RankArray splitLE(Object x) {
        TreapNode[] part1 = TreapNode.splitLE(root, x, cmp);
        RankArray result = new RankArray(cmp);
        result.root = part1[0];
        root = part1[1];
        return result;
    }

    public RankArray splitL(Object x) {
        TreapNode[] part1 = TreapNode.splitL(root, x, cmp);
        RankArray result = new RankArray(cmp);
        result.root = part1[0];
        root = part1[1];
        return result;
    }

    @Override
    public String toString() {
        return root.toString();
    }
}
