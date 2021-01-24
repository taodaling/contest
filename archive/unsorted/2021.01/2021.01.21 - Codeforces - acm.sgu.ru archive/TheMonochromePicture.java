package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

public class TheMonochromePicture {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList all = new IntegerArrayList(a.length * 3);
        for (int i = 0; i < a.length; i++) {
            all.add(a[i]);
            all.add(a[i] - 1);
            all.add(a[i] + 1);
        }
        all.unique();
        for (int i = 0; i < n; i++) {
            a[i] = all.binarySearch(a[i]);
        }
        int m = all.size();
        int[] prev = new int[n];
        int[] dp = new int[n];
        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, m - 1,
                SumImpl::new, UpdateImpl::new, i -> new SumImpl());
        for (int i = 0; i < n; i++) {
            SumImpl range = new SumImpl();
            st.query(0, a[i] - 2, 0, m - 1, range);
            st.query(a[i], a[i], 0, m - 1, range);
            st.query(a[i] + 2, m - 1, 0, m - 1, range);
            prev[i] = range.maxIndex;
            dp[i] = range.max + 1;
            UpdateImpl update = new UpdateImpl();
            update.max = dp[i];
            update.index = i;
            st.update(a[i], a[i], 0, m - 1, update);
        }

        int maxIndex = 0;
        for (int i = 1; i < n; i++) {
            if (dp[maxIndex] < dp[i]) {
                maxIndex = i;
            }
        }
        out.println(n - dp[maxIndex]);
        int now = maxIndex;
        IntegerArrayList ans = new IntegerArrayList(dp[maxIndex]);
        while (now != -1) {
            ans.add(a[now]);
            now = prev[now];
        }
        ans.reverse();
        for (int x : ans.toArray()) {
            out.append(all.get(x)).append(' ');
        }
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int max;
    int index;

    @Override
    public void update(UpdateImpl update) {
    }

    @Override
    public void clear() {
        max = 0;
        index = -1;
    }

    @Override
    public boolean ofBoolean() {
        return !(max == 0);
    }

}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int maxIndex = -1;
    int max;

    @Override
    public void add(SumImpl sum) {
        if (sum.max > max) {
            max = sum.max;
            maxIndex = sum.maxIndex;
        }
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.max > max) {
            max = update.max;
            maxIndex = update.index;
        }
    }

    @Override
    public void copy(SumImpl sum) {
        max = sum.max;
        maxIndex = sum.maxIndex;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }
}