package on2021_06.on2021_06_08_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.A__Advertisement_Matching;



import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.Randomized;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

import java.util.Arrays;
import java.util.Comparator;

public class AAdvertisementMatching {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Item[] a = new Item[n];
        Item[] b = new Item[m];
        for (int i = 0; i < n; i++) {
            a[i] = new Item(in.ri());
        }
        for (int i = 0; i < m; i++) {
            b[i] = new Item(in.ri());
        }

        Item[] sortedA = a.clone();
        Item[] sortedB = b.clone();
        Arrays.sort(sortedA, Comparator.<Item>naturalOrder().reversed());
        Arrays.sort(sortedB, Comparator.<Item>naturalOrder().reversed());
        LongPreSum aps = new LongPreSum(i -> sortedA[i].x, n);
        LongPreSum bps = new LongPreSum(i -> sortedB[i].x, m);
        int[] fill = new int[n];
        for (int i = 0, r = m - 1; i < n; i++) {
            while (r >= 0 && sortedB[r].x <= i) {
                r--;
            }
            fill[i] = r;
        }

        debug.debugArray("fill", fill);
        int L = (int) 5e5;
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl sum = new SumImpl();
                    sum.min = (fill[i] + 1) * (long) (i + 1) + bps.post(fill[i] + 1)
                            - aps.prefix(i);
                    return sum;
                });

        IntegerBIT aDistribute = new IntegerBIT(L + 1);
        for (Item x : a) {
            aDistribute.update(x.x + 1, 1);
        }
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            debug.debug("i", i);
            debug.debug("st", st);
            int t = in.ri();
            int index = in.ri() - 1;
            UpdateImpl upd = new UpdateImpl();
            int l = -1;
            int r = -1;
            if (t == 1) {
                aDistribute.update(a[index].x + 1, -1);
                a[index].x++;
                aDistribute.update(a[index].x + 1, 1);
                int kth = n - aDistribute.query(a[index].x);
                upd.dx = -1;
                l = kth - 1;
                r = n - 1;
            } else if (t == 2) {
                int kth = n - aDistribute.query(a[index].x);
                upd.dx = 1;
                l = kth - 1;
                r = n - 1;
                aDistribute.update(a[index].x + 1, -1);
                a[index].x--;
                aDistribute.update(a[index].x + 1, 1);
            } else if (t == 3) {
                b[index].x++;
                upd.dx = 1;
                l = b[index].x - 1;
                r = n - 1;
            }else if(t == 4){
                upd.dx = -1;
                l = b[index].x - 1;
                r = n - 1;
                b[index].x--;
            }

            st.update(l, r, 0, n - 1, upd);
            if(st.sum.min >= 0){
                out.println(1);
            }else{
                out.println(0);
            }
        }
    }

    Debug debug = new Debug(false);
}

class Item implements Comparable<Item> {
    int x;

    public Item(int x) {
        this.x = x;
    }

    @Override
    public int compareTo(Item o) {
        return Integer.compare(x, o.x);
    }

    @Override
    public String toString() {
        return "" + x;
    }
}


class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long dx;

    @Override
    public void update(UpdateImpl update) {
        dx += update.dx;
    }

    @Override
    public void clear() {
        dx = 0;
    }

    @Override
    public boolean ofBoolean() {
        return dx != 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long min;

    @Override
    public void add(SumImpl sum) {
        min = Math.min(min, sum.min);
    }

    @Override
    public void update(UpdateImpl update) {
        min += update.dx;
    }

    @Override
    public void copy(SumImpl sum) {
        min = sum.min;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + min;
    }
}