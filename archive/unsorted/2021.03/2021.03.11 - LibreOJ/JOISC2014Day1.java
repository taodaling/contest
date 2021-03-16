package contest;

import template.algo.MoOnArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongDequeImpl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class JOISC2014Day1 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList all = new IntegerArrayList(a);
        all.unique();
        for(int i = 0; i < n; i++){
            a[i] = all.binarySearch(a[i]);
        }
        QueryImpl[] qs = new QueryImpl[q];
        for(int i = 0; i < q; i++){
            qs[i] = new QueryImpl();
            qs[i].l = in.ri() - 1;
            qs[i].r = in.ri() - 1;
        }
        StateImpl state = new StateImpl(a, all.getData(), new long[all.size()], 0);
        MoOnArray.addOnlySolve(0, n - 1, state, qs.clone(), 350);
        for(QueryImpl query : qs){
            //assert query.ans == solve(a, all.getData(), query.l, query.r);
            out.println(query.ans);
        }
    }

    public long solve(int[] a, int[] val, int l, int r){
        Map<Integer, Long> map  = new HashMap<>();
        for(int j = l; j <= r; j++){
            map.put(a[j], map.getOrDefault(a[j], 0L) + val[a[j]]);
        }
        return map.values().stream().max(Comparator.naturalOrder()).get();
    }
}

class QueryImpl implements MoOnArray.Query {
    long ans;
    int l;
    int r;

    @Override
    public int left() {
        return l;
    }

    @Override
    public int right() {
        return r;
    }
}

class StateImpl implements MoOnArray.AddOnlyState<QueryImpl> {
    LongDequeImpl dq = new LongDequeImpl(2);
    int[] a;
    int[] val;
    long[] sum;
    long max;

    public StateImpl(int[] a, int[] val, long[] sum, long max) {
        this.a = a;
        this.val = val;
        this.sum = sum;
        this.max = max;
    }

    @Override
    public void answer(QueryImpl query) {
        query.ans = max;
    }

    @Override
    public void add(int i) {
        sum[a[i]] += val[a[i]];
        max = Math.max(max, sum[a[i]]);
    }

    @Override
    public void remove(int i) {
        sum[a[i]] -= val[a[i]];
    }

    @Override
    public void save() {
        dq.addLast(max);
    }

    @Override
    public void rollback() {
        max = dq.removeLast();
    }
}