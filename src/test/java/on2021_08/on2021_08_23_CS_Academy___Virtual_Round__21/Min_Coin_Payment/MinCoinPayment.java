package on2021_08.on2021_08_23_CS_Academy___Virtual_Round__21.Min_Coin_Payment;



import template.io.FastInput;
import template.io.FastOutput;

public class MinCoinPayment {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        int ans = 0;
        ans += k / 50;
        k %= 50;
        ans += k / 5;
        k %= 5;
        ans += k;
        out.println(ans);
    }
}
