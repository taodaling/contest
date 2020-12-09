package on2020_11.on2020_11_29_AtCoder___AtCoder_Regular_Contest_108.A___Sum_and_Product;



import template.io.FastInput;
import template.io.FastOutput;

public class ASumAndProduct {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long s = in.rl();
        long p = in.rl();
        for (long i = 1; i * i <= p; i++) {
            if (p % i == 0) {
                long j = p / i;

                if(i + j == s){
                    out.println("Yes");
                    return;
                }
            }
        }
        out.println("No");
    }
}
