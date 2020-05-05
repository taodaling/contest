package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.concurrent.TimeoutException;

public class FYetAnotherMinimizationProblem {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        Machine machine = new Machine(a, new int[n + 1]);
        long[][] dp = new long[k + 1][n + 1];
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 1; i <= k; i++) {
            dac(machine, dp[i - 1], 1, n, dp[i], 1, n);
            // debug.debug("i", i);
            // debug.debug("dp[i]", dp[i]);
        }

        long ans = dp[k][n];

        out.println(ans);
        debug.debug("invokeTime", machine.invokeTime);
    }

    long inf = (long) 1e18;

    public void dac(Machine machine, long[] lastLayer, int ll, int lr, long[] curLayer, int cl, int cr) {
        if (cl > cr) {
            return;
        }
        if (ll == lr) {
            for (int i = cl; i <= cr; i++) {
                machine.moveTo(ll, i);
                curLayer[i] = lastLayer[ll - 1] + machine.cost;
            }
            return;
        }

        int m = (cl + cr) >> 1;
        int best = ll;
        curLayer[m] = inf;
        for (int i = lr; i >= ll; i--) {
            machine.moveTo(i, m);
            if (curLayer[m] > lastLayer[i - 1] + machine.cost) {
                curLayer[m] = lastLayer[i - 1] + machine.cost;
                best = i;
            }
        }

        dac(machine, lastLayer, best, lr, curLayer, m + 1, cr);
        dac(machine, lastLayer, ll, best, curLayer, cl, m - 1);
    }
}

class Machine {
    final int[] a;
    int[] cnts;
    long cost;

    public int l;
    public int r;
    int invokeTime;

    Machine(int[] a, int[] cnts) {
        this.a = a;
        this.cnts = cnts;
        l = 0;
        r = -1;
    }

    public void ll() {
        l--;
        add(a[l]);
    }

    public void lr() {
        sub(a[l]);
        l++;
    }

    public void rr() {
        r++;
        add(a[r]);
    }

    public void rl() {
        sub(a[r]);
        r--;
    }

    public void moveTo(int left, int right) {
        while (l > left) {
            l--;
            add(a[l]);
        }
        while (r < right) {
            r++;
            add(a[r]);
        }
        while (l < left) {
            sub(a[l]);
            l++;
        }
        while (r > right) {
            sub(a[r]);
            r--;
        }
    }

    public void add(int x) {
        cost += cnts[x];
        cnts[x]++;
    }

    public void sub(int x) {
        cnts[x]--;
        cost -= cnts[x];
    }

    @Override
    public String toString() {
        return "" + invokeTime;
    }
}
