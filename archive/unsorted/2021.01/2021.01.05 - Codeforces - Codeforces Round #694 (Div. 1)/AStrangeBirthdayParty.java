package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.PriorityQueue;

public class AStrangeBirthdayParty {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] k = new int[n];
        int[] c = new int[m];
        in.populate(k);
        in.populate(c);
        for(int i = 0; i < n; i++){
            k[i]--;
        }
        Randomized.shuffle(k);
        int now = 0;
        long ans = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(m);
        for (int x : k) {
            while(now < x){
                pq.add(c[now]);
                now++;
            }
            pq.add(c[x]);
            ans += pq.poll();
        }
        out.println(ans);
    }
}
