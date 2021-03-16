package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DLittleArtemAndTimeMachine {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Query[] qs = new Query[n];
        IntegerArrayList times = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            qs[i] = new Query(in.ri(), in.ri(), in.ri());
            times.add(qs[i].t);
        }
        times.unique();
        for (Query q : qs) {
            q.t = times.binarySearch(q.t) + 1;
        }
        Map<Integer, List<Query>> groupByX = Arrays.stream(qs).collect(Collectors.groupingBy(x -> x.x));
        IntegerBIT bit = new IntegerBIT(times.size());
        for (List<Query> list : groupByX.values()) {
            for (Query q : list) {
                if (q.a == 1) {
                    //add
                    bit.update(q.t, 1);
                } else if (q.a == 2) {
                    bit.update(q.t, -1);
                } else {
                    q.ans = bit.query(q.t);
                }
            }
            for (Query q : list) {
                if (q.a == 1) {
                    //add
                    bit.update(q.t, -1);
                } else if (q.a == 2) {
                    bit.update(q.t, 1);
                } else {
                }
            }
        }
        for(Query q : qs){
            if(q.a == 3){
                out.println(q.ans);
            }
        }
    }
}

class Query {
    int a;
    int t;
    int x;
    int ans;

    public Query(int a, int t, int x) {
        this.a = a;
        this.t = t;
        this.x = x;
    }
}
