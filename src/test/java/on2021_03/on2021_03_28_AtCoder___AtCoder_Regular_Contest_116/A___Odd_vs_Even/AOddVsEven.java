package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.A___Odd_vs_Even;



import template.io.FastInput;
import template.io.FastOutput;

public class AOddVsEven {
    public int log(long a, long b) {
        int ans = 0;
        while (b % a == 0) {
            b /= a;
            ans++;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int log = log(2, n);
        if (log == 1) {
            out.println("Same");
        } else if (log == 0) {
            out.println("Odd");
        } else {
            out.println("Even");
        }
    }
}
