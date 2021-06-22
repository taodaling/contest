package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.E__Lost_Array;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ELostArray {
    public int maxHold(int d) {
        if (d % 2 == 1) {
            return d;
        }
        return d - 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int d = 0;
        while (d <= 500 && !(k * d >= n && (k * d - n) % 2 == 0 && maxHold(d) * n >= k * d)) {
            d++;
        }
        if (d > 500) {
            out.println(-1);
            return;
        }
        int delta = k * d - n;
        List<Integer>[] rounds = new List[d];
        for (int i = 0; i < d; i++) {
            rounds[i] = new ArrayList<>(k);
        }
        int[] occur = new int[n];
        Arrays.fill(occur, 1);
        for (int i = 0; i < n; i++) {
            while (delta > 0 && occur[i] + 2 <= d) {
                occur[i] += 2;
                delta -= 2;
            }
        }
        assert delta == 0;

        int iter = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < occur[i]; j++) {
                rounds[iter % d].add(i);
                iter++;
            }
        }
        long ans = 0;
        for (List<Integer> round : rounds) {
            assert round.size() == 1;
            out.append("? ");
            for (int x : round) {
                out.append(x + 1).append(' ');
            }
            out.println().flush();
            ans ^= in.rl();
        }

        out.printf("! %d", ans).println().flush();
    }
}
