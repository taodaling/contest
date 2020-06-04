package template.datastructure;

import template.math.DigitUtils;
import template.rand.Randomized;

import java.util.Arrays;

public class PrefixIncrementOnePriorityQueue {
    QueryKthRecorder query = new QueryKthRecorder();
    Segment segment;
    int l;
    int r;

    public PrefixIncrementOnePriorityQueue(int[] data) {
        Randomized.shuffle(data);
        Arrays.sort(data);
        l = 0;
        r = data.length - 1;
        segment = new Segment(l, r, data);
    }

    public int peek() {
        segment.queryKthIndex(l, r, 1, query);
        return query.key;
    }

    public int pop() {
        segment.queryKthIndex(l, r, 1, query);
        segment.delete(query.index, query.index, l, r);
        return query.key;
    }

    public void increment(int k) {
        segment.queryKthIndex(l, r, k, query);
        int left = segment.queryFirstIndexOfKey(l, r, query.key);
        int right = segment.queryLastIndexOfKey(l, r, query.key);
        segment.update(l, left - 1, l, r, 1);
        left = right + left - query.index;
        segment.update(left, right, l, r, 1);
    }

    public int size() {
        return segment.size;
    }

    public boolean isEmpty() {
        return segment.size == 0;
    }

    private static class QueryKthRecorder {
        int key;
        int index;
    }

    private static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private int size;
        private int key;
        private int maxKey;
        private int minKey;

        public void modify(int x) {
            if (size == 0) {
                return;
            }
            key += x;
            maxKey += x;
            minKey += x;
        }

        public void delete() {
            size = 0;
            minKey = Integer.MAX_VALUE;
            maxKey = Integer.MIN_VALUE;
        }

        public void pushUp() {
            size = left.size + right.size;
            maxKey = Math.max(left.maxKey, right.maxKey);
            minKey = Math.min(left.minKey, right.minKey);
        }

        public void pushDown() {
            if (key != 0) {
                left.modify(key);
                right.modify(key);
                key = 0;
            }
        }

        public Segment(int l, int r, int[] vals) {
            if (l < r) {
                int m = DigitUtils.floorAverage(l, r);
                left = new Segment(l, m, vals);
                right = new Segment(m + 1, r, vals);
                pushUp();
            } else {
                size = 1;
                minKey = maxKey = key = vals[l];
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, int x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                modify(x);
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public void delete(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                delete();
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.delete(ll, rr, l, m);
            right.delete(ll, rr, m + 1, r);
            pushUp();
        }

        public void queryKthIndex(int l, int r, int k, QueryKthRecorder q) {
            Segment trace = this;
            while (l < r) {
                int m = (l + r) >>> 1;
                trace.pushDown();
                if (trace.left.size >= k) {
                    trace = trace.left;
                    r = m;
                } else {
                    k -= trace.left.size;
                    l = m + 1;
                    trace = trace.right;
                }
            }
            q.key = trace.key;
            q.index = l;
        }

        public int queryFirstIndexOfKey(int l, int r, int key) {
            Segment trace = this;
            while (l < r) {
                int m = (l + r) >>> 1;
                trace.pushDown();
                if (trace.left.maxKey >= key) {
                    trace = trace.left;
                    r = m;
                } else {
                    l = m + 1;
                    trace = trace.right;
                }
            }
            return l;
        }

        public int queryLastIndexOfKey(int l, int r, int key) {
            Segment trace = this;
            while (l < r) {
                int m = (l + r) >>> 1;
                trace.pushDown();
                if (trace.right.minKey <= key) {
                    l = m + 1;
                    trace = trace.right;
                } else {
                    trace = trace.left;
                    r = m;
                }
            }
            return l;
        }

        public void query(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.query(ll, rr, l, m);
            right.query(ll, rr, m + 1, r);
        }
    }
}
