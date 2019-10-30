package on2019_10.on2019_10_30_2019_10_30.Zabuton;



import java.util.Arrays;
import java.util.PriorityQueue;

import template.FastInput;
import template.FastOutput;

public class Zabuton {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Participant[] ps = new Participant[n];
        for (int i = 0; i < n; i++) {
            ps[i] = new Participant();
            ps[i].h = in.readInt();
            ps[i].p = in.readInt();
        }

        PriorityQueue<Participant> pq = new PriorityQueue<>(n, (a, b) -> -(a.p - b.p));

        int used = 0;
        Arrays.sort(ps, (a, b) -> Integer.compare(a.h + a.p, b.h + b.p));
        for (Participant p : ps) {
            if(used <= p.h){
                used += p.p;
                pq.add(p);
            }else{
                if(pq.peek().p >= p.p){
                    used -= pq.poll().p;
                    used += p.p;
                    pq.add(p);
                }
            }
        }

        out.println(pq.size());
    }
}


class Participant {
    int h;
    int p;
}
