package template.datastructure;

import template.binary.Log2;
import template.math.DigitUtils;
import template.utils.SegmentUtils;

import java.util.Arrays;

public class PersistentSegTreeBasedOnArray {
    int[] left;
    int[] right;
    int allocator = 1;

    public void clear() {
        while (allocator > 1) {
            allocator--;
            left[allocator] = 0;
            right[allocator] = 0;
        }
        last = 0;
    }

    private void duplicate() {
        int n = left.length * 2;
        left = Arrays.copyOf(left, n);
        right = Arrays.copyOf(right, n);
    }

    private int alloc() {
        if (allocator == left.length) {
            duplicate();
        }
        return allocator++;
    }

    private int clone(int root) {
        int node = alloc();
        left[node] = left[root];
        right[node] = right[root];
        return node;
    }

    int begin;
    int end;

    public PersistentSegTreeBasedOnArray(int begin, int end, int opNum) {
        this.begin = begin;
        this.end = end;
        int len = end - begin + 1;
        int height = Log2.ceilLog(len) + 1;
        int expSize = height * opNum + 1;
        left = new int[expSize];
        right = new int[expSize];
    }

    public void query(int L, int R) {
        query(last, L, R);
    }

    public void query(int version, int L, int R) {
        query0(version, L, R, begin, end);
    }

    private void query0(int root, int L, int R, int l, int r) {
        if (SegmentUtils.leave(L, R, l, r)) {
            return;
        }
        if (SegmentUtils.enter(L, R, l, r)) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        query0(left[root], L, R, l, m);
        query0(right[root], L, R, m + 1, r);
        return;
    }

    private void modify(int root, int l, int r) {

    }

    private void pushUp(int root, int l, int r) {

    }

    int last = 0;

    public int update(int L, int R) {
        return update(last, L, R);
    }

    public int update(int version, int L, int R) {
        return last = update0(version, L, R, begin, end);
    }

    private int update0(int root, int L, int R, int l, int r) {
        if (SegmentUtils.leave(L, R, l, r)) {
            return root;
        }
        root = clone(root);
        if (SegmentUtils.enter(L, R, l, r)) {
            modify(root, l, r);
            return root;
        }
        int m = DigitUtils.floorAverage(l, r);
        left[root] = update0(left[root], L, R, l, m);
        right[root] = update0(right[root], L, R, m + 1, r);
        pushUp(root, l, r);
        return root;
    }
}
