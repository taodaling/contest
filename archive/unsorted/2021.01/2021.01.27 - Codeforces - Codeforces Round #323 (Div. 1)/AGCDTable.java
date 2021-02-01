package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerPriorityQueue;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.PriorityQueue;

public class AGCDTable {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = in.ri(n * n);
        Randomized.shuffle(x);
        Arrays.sort(x);
        SequenceUtils.reverse(x);
        IntegerArrayList ans = new IntegerArrayList(n);
        ans.add(x[0]);
        IntegerPriorityQueue pq = new IntegerPriorityQueue(n * n, IntegerComparator.REVERSE_ORDER);
        for (int i = 1; i < x.length; i++) {
            int v = x[i];
            if (!pq.isEmpty() && pq.peek() == v) {
                pq.pop();
                continue;
            }
            assert ans.size() + 1 <= n;
            for (int j = 0; j < ans.size(); j++) {
                pq.add(GCDs.gcd(ans.get(j), v));
                pq.add(GCDs.gcd(ans.get(j), v));
            }
            ans.add(v);
        }
        for (int v : ans.toArray()) {
            out.append(v).append(' ');
        }
    }
}
