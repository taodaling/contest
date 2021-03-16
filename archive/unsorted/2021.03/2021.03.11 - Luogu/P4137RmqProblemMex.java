package contest;

import template.algo.MoOnArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

public class P4137RmqProblemMex {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        for (int i = 0; i < n; i++) {
            if (a[i] > n) {
                a[i] = n;
            }
        }
        QueryImpl[] qs = new QueryImpl[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new QueryImpl();
            qs[i].l = in.ri() - 1;
            qs[i].r = in.ri() - 1;
        }

        StateImpl state = new StateImpl(a, n);
        MoOnArray.removeOnlySolve(0, n - 1, state, qs.clone(), 500);
        for(QueryImpl query : qs){
            out.println(query.ans);
        }
    }
}

class QueryImpl implements MoOnArray.Query {
    int l;
    int r;
    int ans;

    @Override
    public int left() {
        return l;
    }

    @Override
    public int right() {
        return r;
    }
}

class StateImpl implements MoOnArray.RemoveOnlyState<QueryImpl> {
    int mex;
    int[] occur;
    int[] data;
    IntegerDeque dq = new IntegerDequeImpl(2);

    public StateImpl(int[] data, int m) {
        this.data = data;
        occur = new int[m + 2];
        for (int x : data) {
            occur[x]++;
        }
        for (int i = 0; i < occur.length; i++) {
            if (occur[i] == 0) {
                mex = i;
                break;
            }
        }
    }

    @Override
    public void answer(QueryImpl query) {
        query.ans = mex;
    }

    @Override
    public void add(int i) {
        occur[data[i]]++;
    }

    @Override
    public void remove(int i) {
        occur[data[i]]--;
        if (occur[data[i]] == 0) {
            mex = Math.min(mex, data[i]);
        }
    }

    @Override
    public void save() {
        dq.addLast(mex);
    }

    @Override
    public void rollback() {
        mex = dq.removeLast();
    }
}