package on2021_05.on2021_05_19_Library_Checker.Range_Kth_Smallest;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SegmentUtils;

public class RangeKthSmallest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList list = new IntegerArrayList(a);
        list.unique();
        ps = new PersistentSegmentBasedOnArray(0, list.size() - 1, n);
        int[] tree = new int[n + 1];
        tree[0] = 0;
        for (int i = 1; i <= n; i++) {
            int index = list.binarySearch(a[i - 1]);
            tree[i] = ps.update(index, index, 1);
        }
        for (int i = 0; i < q; i++) {
            int l = in.ri() + 1;
            int r = in.ri();
            int k = in.ri() + 1;
            int ans = kth(tree[r], tree[l - 1], 0, list.size() - 1, k);
            out.println(list.get(ans));
        }
    }

    PersistentSegmentBasedOnArray ps;

    int kth(int a, int b, int l, int r, int k) {
        if (l == r) {
            return l;
        }
        int lson = ps.cnt[ps.left[a]] - ps.cnt[ps.left[b]];
        int m = DigitUtils.floorAverage(l, r);
        if (lson >= k) {
            return kth(ps.left[a], ps.left[b], l, m, k);
        }
        return kth(ps.right[a], ps.right[b], m + 1, r, k - lson);
    }
}

class PersistentSegmentBasedOnArray {
    int[] left;
    int[] right;
    int[] cnt;
    int allocator = 1;

    public void clear() {
        while (allocator > 1) {
            allocator--;
            left[allocator] = 0;
            right[allocator] = 0;
            cnt[allocator] = 0;
        }
        last = 0;
    }

    private int alloc() {
        return allocator++;
    }

    private int clone(int root) {
        int node = alloc();
        left[node] = left[root];
        right[node] = right[root];
        cnt[node] = cnt[root];
        return node;
    }

    int begin;
    int end;

    public PersistentSegmentBasedOnArray(int begin, int end, int opNum) {
        this.begin = begin;
        this.end = end;
        int len = end - begin + 1;
        int height = Log2.ceilLog(len) + 1;
        int expSize = height * opNum + 1;
        left = new int[expSize];
        right = new int[expSize];
        cnt = new int[expSize];
    }

    public void query(int L, int R) {
        query(last, L, R);
    }

    public void query(int version, int L, int R) {
        query0(version, L, R, begin, end);
    }

    private int query0(int root, int L, int R, int l, int r) {
        if (SegmentUtils.leave(L, R, l, r)) {
            return root;
        }
        if (SegmentUtils.enter(L, R, l, r)) {
            return root;
        }
        int m = DigitUtils.floorAverage(l, r);
        left[root] = query0(left[root], L, R, l, m);
        right[root] = query0(right[root], L, R, m + 1, r);
        return root;
    }

    private void modify(int root, int l, int r, int x) {
        cnt[root] += x;
    }

    private void pushUp(int root, int l, int r) {
        cnt[root] = cnt[left[root]] + cnt[right[root]];
    }

    int last = 0;

    public int update(int L, int R, int x) {
        return update(last, L, R, x);
    }

    public int update(int version, int L, int R, int x) {
        return last = update0(version, L, R, begin, end, x);
    }

    private int update0(int root, int L, int R, int l, int r, int x) {
        if (SegmentUtils.leave(L, R, l, r)) {
            return root;
        }
        root = clone(root);
        if (SegmentUtils.enter(L, R, l, r)) {
            modify(root, l, r, x);
            return root;
        }
        int m = DigitUtils.floorAverage(l, r);
        left[root] = update0(left[root], L, R, l, m, x);
        right[root] = update0(right[root], L, R, m + 1, r, x);
        pushUp(root, l, r);
        return root;
    }
}
