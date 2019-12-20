package contest;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class DSegmentTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Interval[] intervals = new Interval[n];
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval();
            intervals[i].l = in.readInt();
            intervals[i].r = in.readInt();
        }
        Arrays.sort(intervals, (a, b) -> a.r - b.r);
        TreeMap<Integer, Interval> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            map.put(intervals[i].l, intervals[i]);
        }

        Deque<Interval> deque = new ArrayDeque<>(n);

        for (int i = 0; i < n; i++) {
            while (!map.isEmpty() && map.floorKey(intervals[i].r) > intervals[i].l) {
                Map.Entry<Integer, Interval> floor = map.floorEntry(intervals[i].r);
                if (floor.getValue().r <= intervals[i].r) {
                    map.remove(floor.getKey());
                    continue;
                }

                if (intervals[i].find() == floor.getValue().find()) {
                    //find loop
                    out.println("NO");
                    return;
                }

                Interval.merge(intervals[i], floor.getValue());
                deque.addLast(map.remove(floor.getKey()));
            }

            while (!deque.isEmpty()) {
                map.put(deque.peekFirst().l, deque.removeFirst());
            }
        }

        for(int i = 1; i < n; i++){
            if(intervals[i].find() != intervals[0].find()){
                out.println("NO");
                return;
            }
        }

        out.println("YES");
        return;
    }
}

class Interval {
    int l;
    int r;

    Interval p = this;
    int rank;

    Interval find() {
        return p.p == p ? p : (p = p.find());
    }

    static void merge(Interval a, Interval b) {
        a = a.find();
        b = b.find();
        if (a == b) {
            return;
        }
        if (a.rank == b.rank) {
            a.rank++;
        }
        if (a.rank > b.rank) {
            b.p = a;
        } else {
            a.p = b;
        }
    }
}
