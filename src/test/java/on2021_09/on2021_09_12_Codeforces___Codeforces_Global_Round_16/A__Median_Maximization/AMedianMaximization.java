package on2021_09.on2021_09_12_Codeforces___Codeforces_Global_Round_16.A__Median_Maximization;



import template.io.FastInput;
import template.io.FastOutput;

public class AMedianMaximization {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int s = in.ri();
        int rank = (n + 1) / 2;
        int geq = n - rank + 1;
        out.println(s / geq);
    }
}
