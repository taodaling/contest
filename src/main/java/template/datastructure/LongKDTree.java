package template.datastructure;

import template.utils.SortUtils;
import template.utils.ToLongFunction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LongKDTree {
    private int dimension;
    private Node root = Node.NIL;
    private double factor;
    private Node[] buf;
    private int bufTail = 0;
    private Comparator<Node>[] comparators;

    public int getSize() {
        return size;
    }

    private int size;
    private long inf;

    private void prepareBuf() {
        int target = buf.length;
        if (target < size) {
            while (target < root.size) {
                target = Math.max(target * 2, 10);
            }
            buf = Arrays.copyOf(buf, target);
        }
    }

    public LongKDTree(int dimension, long inf) {
        this(dimension, inf, 0.75D, 0, null);
    }

    public LongKDTree(int dimension, long inf, double factor, int expect, long[][] points) {
        this.inf = inf;
        this.dimension = dimension;
        this.factor = factor;
        comparators = new Comparator[dimension];
        delta = new long[dimension];
        curLow = new long[dimension];
        curHigh = new long[dimension];
        buf = new Node[Math.max(expect, points == null ? 0 : points.length)];
        for (int i = 0; i < dimension; i++) {
            int finalI = i;
            comparators[i] = (a, b) -> Long.compare(a.coordinates[finalI], b.coordinates[finalI]);
        }

        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                buf[i] = new Node();
                buf[i].coordinates = points[i].clone();
                buf[i].pushUp();
            }
            root = refactorDfs(0, points.length - 1, 0);
            size = points.length;
        }
    }

    public void add(long[] coordinates) {
        this.coordinates = coordinates;
        root = add(root, 0);
    }

    public void clear() {
        root = Node.NIL;
        size = 0;
    }

    public void searchRange(long[] low, long[] high) {
        this.queryHigh = high;
        this.queryLow = low;
        prepareCurLowHigh();
        searchRange(root, 0);
    }

    private PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> -Long.compare(a.dist, b.dist));
    private int topK;
    private ToLongFunction<long[]> distFunc;

    private void update(Node node) {
        if (pq.size() + 1 <= topK) {
            pq.add(node);
            return;
        }
        if (pq.peek().dist > node.dist) {
            pq.poll();
            pq.add(node);
        }
    }

    public void searchNearest(long[] coordinates, ToLongFunction<long[]> distFunc, int topK, List<Node> ans) {
        if (topK == 0) {
            return;
        }
        this.topK = topK;
        this.coordinates = coordinates;
        this.distFunc = distFunc;
        pq.clear();
        prepareCurLowHigh();
        searchNearest(root, 0);
        ans.addAll(pq);
    }

    private void searchNearest(Node root, int depth) {
        if (root == Node.NIL) {
            return;
        }
        if (pq.size() == topK) {
            for (int i = 0; i < dimension; i++) {
                delta[i] = 0;
                if (curLow[i] > coordinates[i]) {
                    delta[i] = curLow[i] - coordinates[i];
                } else if (curHigh[i] < coordinates[i]) {
                    delta[i] = coordinates[i] - curHigh[i];
                }
            }
            if (distFunc.apply(delta) >= pq.peek().dist) {
                return;
            }
        }

        for (int i = 0; i < dimension; i++) {
            delta[i] = Math.abs(root.coordinates[i] - coordinates[i]);
        }
        root.dist = distFunc.apply(delta);
        update(root);

        int d = depth % dimension;
        long low = curLow[d];
        long high = curHigh[d];
        if (coordinates[d] > root.coordinates[d]) {
            curLow[d] = root.coordinates[d];
            searchNearest(root.right, depth + 1);
            curLow[d] = low;
            curHigh[d] = root.coordinates[d];
            searchNearest(root.left, depth + 1);
            curHigh[d] = high;
        } else {
            curHigh[d] = root.coordinates[d];
            searchNearest(root.left, depth + 1);
            curHigh[d] = high;
            curLow[d] = root.coordinates[d];
            searchNearest(root.right, depth + 1);
            curLow[d] = low;
        }
    }

    private void prepareCurLowHigh() {
        Arrays.fill(curLow, -inf);
        Arrays.fill(curHigh, inf);
    }

    private void searchRange(Node root, int depth) {
        if (root == Node.NIL) {
            return;
        }
        if (!intersect(queryLow, queryHigh, curLow, curHigh)) {
            return;
        }
        if (include(queryLow, queryHigh, curLow, curHigh)) {
            //this space is under query
            return;
        }
        if (include(queryLow, queryHigh, root.coordinates)) {
            //this.point is under query
        }
        int d = depth % dimension;
        long low = curLow[d];
        long high = curHigh[d];
        curHigh[d] = root.coordinates[d];
        searchRange(root.left, depth + 1);
        curHigh[d] = high;
        curLow[d] = root.coordinates[d];
        searchRange(root.right, depth + 1);
        curLow[d] = low;
    }

    private long[] delta;
    private long[] queryLow;
    private long[] queryHigh;
    private long[] curLow;
    private long[] curHigh;
    long[] coordinates;

    private Node add(Node root, int depth) {
        if (root == Node.NIL) {
            root = new Node();
            root.pushUp();
            root.coordinates = coordinates.clone();
            size++;
            return root;
        }
        int d = depth % dimension;
        root.pushDown();
        if (root.coordinates[d] < coordinates[d]) {
            root.right = add(root.right, depth + 1);
        } else {
            root.left = add(root.left, depth + 1);
        }
        root.pushUp();
        if (Math.max(root.left.size, root.right.size) >= root.size * factor) {
            root = refactor(root, depth);
        }
        return root;
    }

    private Node refactor(Node root, int depth) {
        prepareBuf();
        bufTail = 0;
        collect(root);
        return refactorDfs(0, bufTail - 1, depth);
    }

    private Node refactorDfs(int l, int r, int depth) {
        if (r < l) {
            return Node.NIL;
        }
        int k = ((r - l) >>> 1) + 1;

        SortUtils.theKthSmallestElement(buf, comparators[depth % dimension],
                l, r + 1, k);
        Node root = buf[l + k - 1];
        root.left = refactorDfs(l, l + k - 2, depth + 1);
        root.right = refactorDfs(l + k, r, depth + 1);
        root.pushUp();
        return root;
    }

    private void collect(Node root) {
        if (root == Node.NIL) {
            return;
        }
        buf[bufTail++] = root;
        root.pushDown();
        collect(root.left);
        collect(root.right);
    }

    private int compare(long[] a, long[] b) {
        for (int i = 0; i < dimension; i++) {
            if (a[i] != b[i]) {
                return Long.compare(a[i], b[i]);
            }
        }
        return 0;
    }

    private boolean include(long[] low, long[] high, long[] coordinates) {
        for (int i = 0; i < dimension; i++) {
            if (!(low[i] <= coordinates[i] && coordinates[i] <= high[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean include(long[] low, long[] high, long[] low1, long[] high1) {
        for (int i = 0; i < dimension; i++) {
            if (!(low[i] <= low1[i] && high1[i] <= high[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean intersect(long[] low, long[] high, long[] low1, long[] high1) {
        for (int i = 0; i < dimension; i++) {
            if (high[i] < low1[i] && low[i] > high1[i]) {
                return false;
            }
        }
        return true;
    }

    public static class Node {
        static Node NIL = new Node();

        static {
            NIL.size = 0;
            NIL.left = NIL.right = NIL;
        }

        public long[] coordinates;
        Node left = NIL;
        Node right = NIL;
        int size;
        public long dist;

        @Override
        public String toString() {
            return Arrays.toString(coordinates);
        }

        void pushDown() {
            if (this == NIL) {
                return;
            }
        }

        void pushUp() {
            if (this == NIL) {
                return;
            }
            size = left.size + right.size + 1;
        }
    }
}
