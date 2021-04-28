package on2021_04.on2021_04_27_Codeforces___Codeforces_Round__196__Div__1_.E__Optimize_;



import template.algo.BinarySearch;
import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.*;

import java.util.Arrays;
import java.util.stream.IntStream;

public class EOptimize {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int h = in.ri();
        int[] b = in.ri(m);
        Arrays.sort(b);
        int[] a = in.ri(n);
        int[] indices = IntStream.range(0, n).toArray();
        int[] rank = new int[n];
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(a[i], a[j]), 0, n);
        for (int i = 0; i < n; i++) {
            rank[indices[i]] = i;
        }
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    return ans;
                });
        SumImpl sum = new SumImpl();
        UpdateImpl upd = new UpdateImpl();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            sum.clear();
            st.query(0, rank[i], 0, n - 1, sum);
            int size = sum.size;
            int match = BinarySearch.lowerBound(b, 0, m - 1, h - a[i]);
            upd.clear();
            upd.isSet = true;
            upd.set = size + match;
            upd.size = 1;
            st.update(rank[i], rank[i], 0, n - 1, upd);
            upd.clear();
            upd.set = 1;
            st.update(rank[i] + 1, n - 1, 0, n - 1, upd);
            debug.debug("i", i);
            debug.debug("st", st);
            if (i >= m - 1) {
                if (st.sum.max < m) {
                    ans++;
                }
                int remove = rank[i - (m - 1)];
                upd.clear();
                upd.set = 0;
                upd.size = -1;
                upd.isSet = true;
                st.update(remove, remove, 0, n - 1, upd);
                upd.clear();
                upd.set = -1;
                st.update(remove + 1, n - 1, 0, n - 1, upd);
            }
        }

        out.println(ans);
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int set;
    boolean isSet;
    int size;

    @Override
    public void update(UpdateImpl update) {
        if (update.isSet) {
            set = update.set;
            isSet = true;
        } else {
            set += update.set;
        }
        size += update.size;
    }

    @Override
    public void clear() {
        set = 0;
        isSet = false;
        size = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(set == 0 && isSet == false && size == 0);
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int max;
    int size;

    void clear() {
        max = 0;
        size = 0;
    }

    @Override
    public void add(SumImpl sum) {
        max = Math.max(max, sum.max);
        size += sum.size;
    }

    @Override
    public void update(UpdateImpl update) {
        size += update.size;
        if (size == 0) {
            max = 0;
            return;
        }
        if (update.isSet) {
            max = update.set;
        } else {
            max += update.set;
        }
    }

    @Override
    public void copy(SumImpl sum) {
        this.max = sum.max;
        this.size = sum.size;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", max, size);
    }
}