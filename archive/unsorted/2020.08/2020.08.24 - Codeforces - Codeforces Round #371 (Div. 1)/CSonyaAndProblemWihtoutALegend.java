package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.PriorityQueue;

public class CSonyaAndProblemWihtoutALegend {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt() - i;
            pq.add(x);
            ans += pq.poll() - x;
            pq.add(x);
        }
        out.println(ans);
    }
}
