package on2021_08.on2021_08_29_Codeforces___Deltix_Round__Summer_2021__open_for_everyone__rated__Div__1___Div__2_.B__Take_Your_Places_;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class BTakeYourPlaces {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        for (int i = 0; i < n; i++) {
            a[i] &= 1;
        }
        long best = inf;
        for (int i = 0; i < 2; i++) {
            int[] test = new int[n];
            for (int j = 0; j < n; j++) {
                test[j] = j & 1;
                test[j] ^= i;
            }
            best = Math.min(best, solve(a, test));
        }
        out.println(best == inf ? -1 : best);
    }

    long inf = (long) 1e18;

    public long solve(int[] a, int[] b) {
        Deque<Integer> dq = new ArrayDeque<>(a.length);
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 1) {
                dq.addLast(i);
            }
        }
        long ans = 0;
        for (int i = 0; i < a.length; i++) {
            if (b[i] == 1) {
                if (dq.isEmpty()) {
                    return inf;
                }
                ans += Math.abs(i - dq.removeFirst());
            }
        }
        if (!dq.isEmpty()) {
            return inf;
        }
        return ans;
    }
}
