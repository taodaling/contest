package on2021_08.on2021_08_19_CS_Academy___Virtual_Round__11.Single_Digit_Numbers;



import template.io.FastInput;
import template.io.FastOutput;

public class SingleDigitNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        int ans = 0;
        for (int i = 1; i <= 9; i++) {
            int cur = i;
            while (cur <= b) {
                if (a <= cur) {
                    ans++;
                }
                cur = cur * 10 + i;
            }
        }
        out.println(ans);
    }
}
