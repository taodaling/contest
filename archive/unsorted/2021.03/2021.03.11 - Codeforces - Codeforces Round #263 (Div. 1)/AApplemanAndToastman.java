package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.PriorityQueue;

public class AApplemanAndToastman {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        PriorityQueue<Long> pq = new PriorityQueue<>(n, Comparator.reverseOrder());
        long ans = 0;
        for(int x : a){
            ans += x;
            pq.add((long)x);
        }
        while(pq.size() > 1){
            long x1 = pq.remove();
            long x2 = pq.remove();
            ans += x1 + x2;
            pq.add(x1 + x2);
        }
        out.println(ans);
    }
}
