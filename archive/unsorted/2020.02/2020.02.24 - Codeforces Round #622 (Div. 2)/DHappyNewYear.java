package contest;

import template.algo.SubsetGenerator;
import template.binary.Bits;
import template.binary.CachedBitCount;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class DHappyNewYear {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();

        int mask = 1 << 8;
        IntegerList list = new IntegerList(5 * n);
        list.add(1);
        list.add(0);
        list.add(m);
        list.add(m + 1);
        Interval[] intervals = new Interval[n];
        for (int i = 0; i < n; i++) {
            intervals[i] = new Interval(in.readInt(), in.readInt());
            intervals[i].id = i;
            list.add(intervals[i].l);
            list.add(intervals[i].l - 1);
            list.add(intervals[i].r);
            list.add(intervals[i].r + 1);
        }

        IntegerDiscreteMap dm = new IntegerDiscreteMap(list.getData(), 0, list.size());

        PriorityQueue<Interval> pq = new PriorityQueue<>(16, (a, b) -> Integer.compare(a.r, b.r));
        Arrays.sort(intervals, (a, b) -> Integer.compare(a.l, b.l));
        SimplifiedDeque<Interval> dq = new Range2DequeAdapter<>(i -> intervals[i], 0, n - 1);
        DP[] dp = new DP[dm.maxRank()];
        for (int i = 1; i <= dm.maxRank(); i++) {
            int l = dm.iThElement(i - 1);
            int r = dm.iThElement(i) - 1;
            while (!pq.isEmpty() && pq.peek().r < r) {
                pq.remove();
            }
            while (!dq.isEmpty() && dq.peekFirst().l <= l) {
                pq.add(dq.removeFirst());
            }
            dp[i - 1] = new DP();
            dp[i - 1].l = l;
            dp[i - 1].r = r;
            dp[i - 1].intervals = new ArrayList<>(pq);
            dp[i - 1].intervals.sort((a, b) -> Integer.compare(a.id, b.id));
        }

//        for (int i = 0; i < dp[0].intervals.size(); i++) {
//            dp[0].intervals.get(i).mask = 1 << i;
//        }
        for (int i = 1; i < dp.length; i++) {
            int finalI = i;
            int remain = mask - 1;
            SimplifiedDeque<Interval> dq1 = new Range2DequeAdapter<>(j -> dp[finalI - 1].intervals.get(j), 0, dp[i - 1].intervals.size() - 1);
            SimplifiedDeque<Interval> dq2 = new Range2DequeAdapter<>(j -> dp[finalI].intervals.get(j), 0, dp[i].intervals.size() - 1);
            while (!dq1.isEmpty() && !dq2.isEmpty()) {
                int comp = dq1.peekFirst().id - dq2.peekFirst().id;
                if (comp == 0) {
                    Interval a = dq1.removeFirst();
                    Interval b = dq2.removeFirst();
                    b.mask = a.mask;
                    remain -= b.mask;
                } else if (comp < 0) {
                    dq1.removeFirst();
                } else {
                    dq2.removeFirst();
                }
            }
            for (Interval interval : dp[i].intervals) {
                if (interval.mask == 0) {
                    interval.mask = remain & -remain;
                    remain -= interval.mask;
                } else {
                    dp[i].maskLast |= interval.mask;
                }
                dp[i].mask |= interval.mask;
            }
        }

        SubsetGenerator sg = new SubsetGenerator();
        for (int i = 1; i < dp.length; i++) {
            sg.reset(dp[i - 1].mask);
            while(sg.hasNex()){
                int state = j & dp[i].maskLast;
                dp[i - 1].dp[state] = Math.max(dp[i - 1].dp[state], dp[i - 1].dp[j]);
            }
            int len = dp[i].r - dp[i].l + 1;
            sg.reset(dp[i].mask);
            while (sg.hasNext()) {
                int next = sg.next();
                dp[i].dp[next] = Math.max(dp[i].dp[next], dp[i - 1].dp[next & dp[i].maskLast] +
                        (CachedBitCount.bitCount(next) & 1) * len);
            }

        }

        int ans = 0;
        for (int i = 0; i < mask; i++) {
            ans = Math.max(dp[dp.length - 1].dp[i], ans);
        }
        out.println(ans);
    }
}

class DP {
    int l;
    int r;
    List<Interval> intervals;
    int mask;
    int maskLast;
    int[] dp = new int[1 << 8];
}

class Interval {
    int l;
    int r;
    int id;
    int mask;

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }
}