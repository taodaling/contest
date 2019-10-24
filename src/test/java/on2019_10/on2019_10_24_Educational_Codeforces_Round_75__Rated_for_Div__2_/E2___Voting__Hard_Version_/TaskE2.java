package on2019_10.on2019_10_24_Educational_Codeforces_Round_75__Rated_for_Div__2_.E2___Voting__Hard_Version_;



import template.FastInput;
import template.FastOutput;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;

public class TaskE2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Vote[] votes = new Vote[n];
        for (int i = 0; i < n; i++) {
            votes[i] = new Vote();
            votes[i].m = in.readInt();
            votes[i].p = in.readInt();
        }

        Arrays.sort(votes, (a, b) -> a.m - b.m);
        long total = 0;
        Deque<Vote> deque = new ArrayDeque<>(n);
        PriorityQueue<Vote> pq = new PriorityQueue<>(n, (a, b) -> -(a.p - b.p));
        for (int i = 0; i < n; i++) {
            while(!deque.isEmpty() && deque.peekFirst().m <= i){
                pq.add(deque.removeFirst());
            }
            if (votes[i].m <= i) {
                if (pq.size() > 0 && pq.peek().p > votes[i].p) {
                    total -= pq.remove().p;
                    total += votes[i].p;
                    pq.add(votes[i]);
                }
            } else {
                if (pq.size() == 0) {
                    total += votes[i].p;
                    deque.addLast(votes[i]);
                } else {
                    total -= pq.remove().p;
                    total += votes[i].p;
                    deque.addLast(votes[i]);
                }
            }
        }

        out.println(total);
    }
}

class Vote {
    int p;
    int m;
}
