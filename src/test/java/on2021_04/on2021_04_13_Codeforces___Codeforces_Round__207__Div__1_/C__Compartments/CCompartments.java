package on2021_04.on2021_04_13_Codeforces___Codeforces_Round__207__Div__1_.C__Compartments;



import template.io.FastInput;
import template.io.FastOutput;

public class CCompartments {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] cnts = new int[5];
        for (int i = 0; i < n; i++) {
            cnts[in.ri()]++;
        }
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += cnts[i] * i;
        }
        if (sum == 0) {
            out.println(0);
            return;
        }
        if (sum <= 2 || sum == 5) {
            out.println(-1);
            return;
        }
        int best = (int) 1e9;
        for (int k = 1; k <= n; k++) {
            if (sum < 3 * k || sum > 4 * k) {
                continue;
            }
            int fourCnt = sum - 3 * k;
            int step = 0;
            int takeTotal = 0;
            int remain = k;
            for (int i = 4; i >= 0; i--) {
                int take = Math.min(remain, cnts[i]);
                remain -= take;
                takeTotal += i * take;
                if (i == 4) {
                    step += Math.max(0, take - fourCnt);
                }
            }
            step += sum - takeTotal;
            best = Math.min(best, step);
        }
        out.println(best);
    }
}
