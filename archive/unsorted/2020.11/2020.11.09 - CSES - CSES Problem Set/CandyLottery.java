package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CandyLottery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        double exp = 0;
        for (int i = 1; i <= k; i++) {
            exp += (prob(k, n, i) - prob(k, n, i - 1)) * i;
        }
        out.printf("%.6f", exp);
    }

    //1, ... , k
    //max <= x
    double prob(int k, int n, int x) {
        return Math.pow((double) x / k, n);
    }
}
