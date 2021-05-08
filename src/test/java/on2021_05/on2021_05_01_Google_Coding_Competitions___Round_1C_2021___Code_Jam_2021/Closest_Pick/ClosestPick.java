package on2021_05.on2021_05_01_Google_Coding_Competitions___Round_1C_2021___Code_Jam_2021.Closest_Pick;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClosestPick {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            list.add(in.ri());
        }
        list.unique();
        List<Interval> intervals = new ArrayList<>(n + 2);
        if (list.first() != 1) {
            Interval interval = new Interval(list.first() - 1, list.first() - 1);
            intervals.add(interval);
        }
        if (list.tail() != k) {
            Interval interval = new Interval(k - list.tail(), k - list.tail());
            intervals.add(interval);
        }
        for (int i = 1; i < list.size(); i++) {
            int L = list.get(i - 1);
            int R = list.get(i);
            intervals.add(new Interval(R - L - 1, DigitUtils.ceilDiv(R - L - 1, 2)));
        }
        intervals.sort(Comparator.comparingInt(x -> -x.best));
        int best = 0;
        for (Interval interval : intervals) {
            best = Math.max(best, interval.size);
        }
        if(intervals.size() > 1){
            best = Math.max(best, intervals.get(0).best + intervals.get(1).best);
        }
        double ans = (double)best / k;
        out.printf("Case #%d: ", testNumber).println(ans);
    }
}

class Interval {
    int size;
    int best;

    public Interval(int size, int best) {
        this.best = best;
        this.size = size;
    }
}
