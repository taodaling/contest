package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Sum;
import template.utils.Update;

public class RangeUpdatesAndSums {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        SumImpl sum = new SumImpl();
        UpdateImpl update = new UpdateImpl();
        SegTree<SumImpl, UpdateImpl> seg =
                new SegTree<>(0, n - 1, SumImpl::new,
                        UpdateImpl::new,
                        i -> {
                            SumImpl ans = new SumImpl();
                            ans.size = 1;
                            ans.sum = a[i];
                            return ans;
                        });
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                update.a = 1;
                update.b = in.readInt();
                seg.update(l, r, 0, n - 1, update);
            }else if(t == 2){
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                update.a = 0;
                update.b = in.readInt();
                seg.update(l, r, 0, n - 1, update);
            }else{
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                sum.sum = 0;
                sum.size = 0;
                seg.query(l, r, 0, n - 1, sum);
                out.println(sum.sum);
            }
        }
    }
}

class UpdateImpl implements Update<UpdateImpl> {
    long a;
    long b;

    @Override
    public void update(UpdateImpl update) {
        a = a * update.a;
        b = b * update.a + update.b;
    }

    @Override
    public void clear() {
        a = 1;
        b = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(a == 1 && b == 0);
    }

    @Override
    public UpdateImpl clone() {
        UpdateImpl ans = new UpdateImpl();
        ans.a = a;
        ans.b = b;
        return ans;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long sum;
    int size = 0;

    @Override
    public void add(SumImpl right) {
        this.sum += right.sum;
        this.size += right.size;
    }

    @Override
    public void update(UpdateImpl update) {
        sum = update.a * sum + update.b * size;
    }

    @Override
    public void copy(SumImpl sum) {
        this.size = sum.size;
        this.sum = sum.sum;
    }

    @Override
    public SumImpl clone() {
        SumImpl sum = new SumImpl();
        sum.copy(this);
        return sum;
    }
}