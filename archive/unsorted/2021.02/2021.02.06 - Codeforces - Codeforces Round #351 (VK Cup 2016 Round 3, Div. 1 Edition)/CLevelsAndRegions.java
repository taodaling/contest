package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class CLevelsAndRegions {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        prev = new double[n];
        next = new double[n];
        T = new double[n];
        invT = new double[n];
        opt = new int[n];
        for (int i = 0; i < n; i++) {
            T[i] = in.ri();
            invT[i] = 1 / T[i];
        }
        machine = new Machine(T, invT);
        SequenceUtils.deepFill(prev, 1e50);
        for (int i = 0; i < k; i++) {
            machine.init();
            dac(0, n - 1, 0, n - 1);
            double[] tmp = prev;
            prev = next;
            next = tmp;
            debug.debug("i", i);
            debug.debug("prev", prev);
            debug.debug("opt", opt);
        }
        double ans = prev[n - 1];
        out.println(ans);
    }

    double[] prev;
    double[] next;
    double[] invT;
    double[] T;
    int[] opt;
    Machine machine;

    public void dac(int L, int R, int l, int r) {
        if (r < l) {
            return;
        }
        int m = (l + r) / 2;
        int bestChoice = Math.min(R, m);
        double bestValue = 1e50;
        for (int i = Math.min(R, m); i >= L; i--) {
            machine.move(i, m);
            double cand = machine.exp;
            cand += i == 0 ? 0 : prev[i - 1];
            if (cand < bestValue) {
                bestValue = cand;
                bestChoice = i;
            }
        }
        next[m] = bestValue;
        opt[m] = bestChoice;
        dac(L, bestChoice, l, m - 1);
        dac(bestChoice, R, m + 1, r);
    }
}

class Machine {
    double[] T;
    double[] invT;
    double invSum;
    double sum;
    double exp;
    int l, r;

    public Machine(double[] t, double[] invT) {
        T = t;
        this.invT = invT;
    }

    public void init() {
        invSum = 0;
        sum = 0;
        exp = 0;
        l = 0;
        r = -1;
    }

    public void move(int L, int R) {
        while (R > r) {
            r++;
            sum += T[r];
            invSum += invT[r];
            exp += sum / T[r];
        }
        while (L < l) {
            l--;
            sum += T[l];
            invSum += invT[l];
            exp += invSum * T[l];
        }
        while (R < r) {
            exp -= sum / T[r];
            invSum -= invT[r];
            sum -= T[r];
            r--;
        }
        while (L > l) {
            exp -= invSum * T[l];
            invSum -= invT[l];
            sum -= T[l];
            l++;
        }
    }
}