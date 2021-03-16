package contest;

import template.algo.UndoOperation;
import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CloneSupportObject;
import template.utils.SequenceUtils;
import template.utils.Sum;
import template.utils.Update;

import java.util.Arrays;
import java.util.Comparator;

public class ESignOnFence {
    UpdateImpl bufUpd = new UpdateImpl();
    SumImpl bufSum = new SumImpl();
    SegTree<SumImpl, UpdateImpl> st;
    int low;
    int high;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Wall[] walls = new Wall[n];
        for (int i = 0; i < n; i++) {
            walls[i] = new Wall();
            walls[i].x = i;
            walls[i].h = in.ri();
        }
        low = 0;
        high = n - 1;
        st = new SegTree<>(0, n - 1, SumImpl::new, UpdateImpl::new, i -> {
            SumImpl sum = new SumImpl();
            sum.size = 1;
            return sum;
        });
        Arrays.sort(walls, Comparator.comparingInt(x -> x.h));
        UndoOperation[] ops = new UndoOperation[n];
        for (int i = 0; i < n; i++) {
            Wall wall = walls[i];
            ops[i] = new UndoOperation() {
                @Override
                public void apply() {
                    bufUpd.mod = 1;
                    st.update(wall.x, wall.x, low, high, bufUpd);
                }

                @Override
                public void undo() {
                    bufUpd.mod = -1;
                    st.update(wall.x, wall.x, low, high, bufUpd);
                }
            };
        }
        machine = new Machine(n, ops);

        int q = in.ri();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].l = in.ri() - 1;
            qs[i].r = in.ri() - 1;
            qs[i].w = in.ri();
        }
        this.qs = qs.clone();
        dac(0, n - 1, 0, q - 1);
        for (Query query : qs) {
            int ans = walls[query.ans].h;
            out.println(ans);
        }
    }

    Query[] qs;
    Machine machine;

    void dac(int L, int R, int l, int r) {
        assert L <= R;
        if (l > r) {
            return;
        }
        if (L == R) {
            for (int i = l; i <= r; i++) {
                qs[i].ans = L;
            }
            return;
        }
        int M = (L + R + 1) / 2;
        machine.move(M);
        int m = l;
        for (int i = l; i <= r; i++) {
            bufSum.clear();
            st.query(qs[i].l, qs[i].r, this.low, this.high, bufSum);
            if (bufSum.max < qs[i].w) {
                SequenceUtils.swap(qs, m, i);
                m++;
            }
        }
        dac(L, M - 1, l, m - 1);
        dac(M, R, m, r);
    }
}


class Wall {
    int h;
    int x;
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    int mod;

    @Override
    public void update(UpdateImpl update) {
        mod = update.mod;
    }

    @Override
    public void clear() {
        mod = 0;
    }

    @Override
    public boolean ofBoolean() {
        return mod != 0;
    }

}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    int left;
    int right;
    int max;
    int size;

    void clear() {
        left = right = max = size = 0;
    }

    @Override
    public void add(SumImpl sum) {
        max = Math.max(max, sum.max);
        max = Math.max(max, right + sum.left);
        if (left == size) {
            left += sum.left;
        }
        if (sum.right == sum.size) {
            right += sum.right;
        } else {
            right = sum.right;
        }
        size += sum.size;
    }

    @Override
    public void update(UpdateImpl update) {
        if (update.mod == 1) {
            left = right = max = 1;
        } else {
            left = right = max = 0;
        }
    }

    @Override
    public void copy(SumImpl sum) {
        max = sum.max;
        size = sum.size;
        left = sum.left;
        right = sum.right;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "" + max;
    }
}

class Query {
    int l;
    int r;
    int w;
    int ans;
}

class Machine {
    int cur;
    UndoOperation[] ops;

    public Machine(int cur, UndoOperation[] ops) {
        this.cur = cur;
        this.ops = ops;
    }

    void move(int to) {
        while (cur < to) {
            ops[cur].undo();
            cur++;
        }
        while (cur > to) {
            cur--;
            ops[cur].apply();
        }
    }


}