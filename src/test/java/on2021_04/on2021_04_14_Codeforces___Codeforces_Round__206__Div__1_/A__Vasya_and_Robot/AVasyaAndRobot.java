package on2021_04.on2021_04_14_Codeforces___Codeforces_Round__206__Div__1_.A__Vasya_and_Robot;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

public class AVasyaAndRobot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long l = in.ri();
        long r = in.ri();
        long ql = in.ri();
        long qr = in.ri();
        int[] w = in.ri(n);
        IntegerPreSum ps = new IntegerPreSum(i -> w[i], n);
        long ans = (long) 1e18;
        for (int i = 0; i <= n; i++) {
            int L = i;
            int R = n - i;
            long contrib = l * ps.intervalSum(0, i - 1) +
                    r * ps.intervalSum(i, n - 1);
            if (L > R) {
                contrib += (L - R - 1) * ql;
            } else if (L < R) {
                contrib += (R - L - 1) * qr;
            }
            ans = Math.min(ans, contrib);
        }

        out.println(ans);
    }
}
