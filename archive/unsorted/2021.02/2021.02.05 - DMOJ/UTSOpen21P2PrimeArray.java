package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

public class UTSOpen21P2PrimeArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        if (k == 0) {
            out.println("1\n2");
            return;
        }
        int[] data = new int[100];
        int wpos = 0;
        while (k > 0) {
            data[wpos++] = 1;
            k -= wpos;
        }
        int now = 0;
        while (k < 0) {
            data[now++] = getPrime();
            k++;
        }

        out.println(wpos);
        for (int i = 0; i < wpos; i++) {
            out.append(data[i]).append(' ');
        }
    }

    public int minChoose2(int k) {
        int ans = 0;
        while (choose2(ans + 1) <= k) {
            ans++;
        }
        return ans;
    }

    public int choose2(int n) {
        return n * (n - 1) / 2;
    }

    EulerSieve sieve = new EulerSieve(100000);
    int iterator = 0;

    public int getPrime() {
        return sieve.get(iterator++);
    }
}
