package on2021_11.on2021_11_02_Codeforces___Codeforces_Round__752__Div__1_.B__Moderate_Modular_Mode;



import template.io.FastInput;
import template.io.FastOutput;

public class BModerateModularMode {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int y = in.ri();
        if (x == y) {
            out.println(x);
            return;
        }
        if (x < y) {
            int m = (x + y) / 2;
            int mod = m % x;
            out.println(y - mod);
            return;
        }
        out.println(x + y);
    }
}
