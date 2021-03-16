package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class DParcels {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int S = in.ri();

        Event[] events = new Event[n + 1];
        for (int i = 0; i < n; i++) {
            events[i] = new Event();
            events[i].id = i;
            events[i].in = in.ri();
            events[i].out = in.ri();
            events[i].w = in.ri();
            events[i].s = in.ri();
            events[i].v = in.ri();
        }
        events[n] = new Event();
        events[n].in = 0;
        events[n].out = 2 * n;
        events[n].w = 0;
        events[n].s = S;
        events[n].v = 0;
        events[n].id = n;
        IntegerArrayList allInList = new IntegerArrayList(n + 1);
        IntegerArrayList allOutList = new IntegerArrayList(n + 1);
        for (Event e : events) {
            allInList.add(e.in);
            allOutList.add(e.out);
        }
        allInList.unique();
        allOutList.unique();
        for (Event e : events) {
            e.in = allInList.binarySearch(e.in);
            e.out = allOutList.binarySearch(e.out);
        }
        int m = allOutList.size();
        Event[] sortByOut = events.clone();
        Arrays.sort(sortByOut, Comparator.comparingInt(x -> x.out));
        Event[] sortByLen = events.clone();
        Arrays.sort(sortByLen, Comparator.<Event>comparingInt(x -> -x.in).thenComparingInt(x -> x.out));
        int[] dp = new int[m];
        int N = allInList.size();
        int[] prev = new int[N];
        for (int i = N - 1; i >= 0; i--) {
            prev[i] = allOutList.upperBound(allInList.get(i)) - 1;
        }

        int[][] state = new int[n + 1][S + 1];
        for (int i = 0; i <= S; i++) {
            for (Event e : sortByLen) {
                if (e.w > i) {
                    state[e.id][i] = 0;
                    continue;
                }
                int maxS = Math.min(i - e.w, e.s);
                Arrays.fill(dp, 0);
                int dqHead = 0;
                for (int j = 0; j < m; j++) {
                    while (dqHead <= n && sortByOut[dqHead].out == j) {
                        Event head = sortByOut[dqHead++];
                        if (head.in < e.in || head.out > e.out || head == e) {
                            continue;
                        }
                        int cand = state[head.id][maxS];
                        if (prev[head.in] != -1) {
                            cand += dp[prev[head.in]];
                        }
                        dp[j] = Math.max(dp[j], cand);
                    }
                    if (j > 0) {
                        dp[j] = Math.max(dp[j], dp[j - 1]);
                    }
                }
                state[e.id][i] = dp[m - 1] + e.v;
            }
        }

        debug.debug("state", state);
        int ans = state[n][S];
        out.println(ans);
    }
}

class Event {
    int in;
    int out;
    int w;
    int s;
    int v;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}
