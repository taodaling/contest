package on2021_08.on2021_08_22_AtCoder___AtCoder_Regular_Contest_125.A___Dial_Up;



import template.io.FastInput;
import template.io.FastOutput;

public class ADialUp {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] s = in.ri(n);
        int[] t = in.ri(m);
        int inf = (int) 1e9;
        int other = (int) 1e9;
        for (int i = 0; i < n; i++) {
            if (s[i] != s[0]) {
                int dist = Math.min(i, n - i);
                other = Math.min(other, dist);
            }
        }
        long cost = 0;
        int last = s[0];
        boolean moved = false;
        for (int x : t) {
            if (x == last) {
                cost++;
                continue;
            }
            last = x;
            if (!moved) {
                moved = true;
                cost += other;
            } else {
                cost++;
            }
            cost++;
        }
        out.println(cost >= inf ? -1 : cost);
    }
}
