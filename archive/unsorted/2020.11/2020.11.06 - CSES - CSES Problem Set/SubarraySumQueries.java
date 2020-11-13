package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.GenericModLog;
import template.utils.Sum;
import template.utils.Update;

public class SubarraySumQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        UpdateImpl u = new UpdateImpl();
        SumImpl s = new SumImpl();
        SegTree<SumImpl, UpdateImpl> segTree = new SegTree<>(0, n - 1, SumImpl::new,
                UpdateImpl::new, i -> {
            SumImpl sum = new SumImpl();
            u.x = a[i];
            sum.update(u);
            return sum;
        });

        for (int i = 0; i < m; i++) {
            int k = in.readInt() - 1;
            int x = in.readInt();
            u.x = x;
            segTree.update(k, k, 0, n - 1, u);
            out.println(segTree.sum.max);
        }
    }
}

class UpdateImpl implements Update<UpdateImpl> {
    int x;

    @Override
    public void update(UpdateImpl update) {
    }

    @Override
    public void clear() {
        x = Integer.MIN_VALUE;
    }

    @Override
    public boolean ofBoolean() {
        return x != Integer.MIN_VALUE;
    }

    @Override
    public UpdateImpl clone() {
        UpdateImpl ans = new UpdateImpl();
        ans.x = x;
        return ans;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long left;
    long right;
    long max;
    long sum;

    @Override
    public void add(SumImpl right) {
        SumImpl left = this;
        long bestLeft = Math.max(left.left, left.sum + right.left);
        long bestRight = Math.max(left.right + right.sum, right.right);
        long bestMax = Math.max(left.max, right.max);
        bestMax = Math.max(bestMax, left.right + right.left);
        long bestSum = left.sum + right.sum;

        this.left = bestLeft;
        this.right = bestRight;
        this.max = bestMax;
        this.sum = bestSum;
    }

    @Override
    public void update(UpdateImpl update) {
        sum = update.x;
        max = left = right = Math.max(0, update.x);
    }

    @Override
    public void copy(SumImpl sum) {
        left = sum.left;
        right = sum.right;
        max = sum.max;
        this.sum = sum.sum;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + sum;
    }
}


