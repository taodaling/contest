package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SegmentBeat;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.Arrays;
import java.util.Comparator;

public class IncreasingArrayQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] x = new int[n];
        in.populate(x);
        LongPreSum lps = new LongPreSum(i -> x[i], n);
        SegmentBeat sb = new SegmentBeat(0, n - 1, i -> -x[i]);

        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query(in.ri() - 1, in.ri() - 1);
        }
        Query[] sorted = qs.clone();
        Arrays.sort(sorted, Comparator.comparingInt(t -> t.l));
        SimplifiedDeque<Query> dq = new Range2DequeAdapter<>(i -> sorted[i], 0, q - 1);
        for (int i = n - 1; i >= 0; i--) {
            sb.updateMin(i, n - 1, 0, n - 1, -x[i]);
            while (!dq.isEmpty() && dq.peekLast().l == i) {
                Query head = dq.removeLast();
                head.ans = -sb.querySum(head.l, head.r, 0, n - 1) - lps.intervalSum(head.l, head.r);
            }
        }
        for(Query query : qs){
            out.println(query.ans);
        }
    }
}

class Query {
    int l;
    int r;
    long ans;

    public Query(int l, int r) {
        this.l = l;
        this.r = r;
    }
}