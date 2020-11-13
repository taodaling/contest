package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DistinctValuesQueries {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        Query[] qs = new Query[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new Query(in.readInt() - 1, in.readInt() - 1);
        }
        Query[] origin = qs.clone();
        Arrays.sort(qs, (x, y) -> Integer.compare(x.r, y.r));
        Map<Integer, Integer> reg = new HashMap<>();
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            prev[i] = reg.getOrDefault(a[i], -1);
            reg.put(a[i], i);
        }
        SimplifiedDeque<Query> dq = new Range2DequeAdapter<>(i -> qs[i], 0, m - 1);
        IntegerBIT bit = new IntegerBIT(n + 2);
        for (int i = 0; i < n; i++) {
            bit.update(prev[i] + 2, 1);
            while (!dq.isEmpty() && dq.peekFirst().r == i) {
                Query query = dq.removeFirst();
                query.ans = bit.query(query.l - 1 + 2) - query.l;
            }
        }

        for(Query q : origin){
            out.println(q.ans);
        }
    }
}

class Query {
    int l;
    int r;

    public Query(int l, int r) {
        this.l = l;
        this.r = r;
    }

    int ans;
}
