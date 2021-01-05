package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class FMaxCorrectSet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        int[] state = new int[1 << 22];
        int[] sum = new int[(int) 1e7];
        Arrays.fill(state, -1);
        int cur = 0;
        int total = 0;
        int mask = state.length - 1;
        for (int i = 1; i <= n; i++) {
            if (Bits.get(cur, a - 1) + Bits.get(cur, b - 1) == 0) {
                total++;
                cur = (cur << 1) | 1;
                cur &= mask;
            } else {
                cur = (cur << 1);
                cur &= mask;
            }
            if (state[cur] != -1) {
                int got = total - sum[state[cur]];
                int ans = total;
                int peorid = i - state[cur];
                int remain = n - i;
                ans += remain / peorid * got + sum[state[cur] + remain % peorid] - sum[state[cur]];
                out.println(ans);
                return;
            }
            sum[i] = total;
            state[cur] = i;
        }
        out.println(sum[n]);
    }
}
