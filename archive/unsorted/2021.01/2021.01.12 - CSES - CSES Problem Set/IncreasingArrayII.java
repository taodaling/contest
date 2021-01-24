package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.PriorityQueue;

public class IncreasingArrayII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = in.ri(n);
        long cost = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(n, Comparator.reverseOrder());
        for(int a : x){
            pq.add(a);
            cost += pq.remove() - a;
            pq.add(a);
        }
        out.println(cost);
    }
}
