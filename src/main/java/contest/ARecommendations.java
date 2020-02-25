package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.PriorityQueue;

public class ARecommendations {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Op[] ops = new Op[n];
        for (int i = 0; i < n; i++) {
            ops[i] = new Op();
            ops[i].a = in.readInt();
        }
        for(int i = 0; i < n; i++){
            ops[i].t = in.readInt();
        }
        Arrays.sort(ops, (a, b) -> Integer.compare(a.a, b.a));
        SimplifiedDeque<Op> dq = new Range2DequeAdapter<>(i -> ops[i], 0, n - 1);
        long ans = 0;
        long sum = 0;
        PriorityQueue<Op> pq = new PriorityQueue<>(n, (a, b) -> -Long.compare(a.t, b.t));
        for (int i = 0; !pq.isEmpty() || !dq.isEmpty(); i++) {
            if (pq.isEmpty()) {
                i = dq.peekFirst().a;
            }
            while (!dq.isEmpty() && dq.peekFirst().a == i) {
                Op op = dq.removeFirst();
                pq.add(op);
                sum += op.t;
            }
            sum -= pq.remove().t;
            ans += sum;
        }
        out.println(ans);
    }
}

class Op {
    int a;
    int t;
}