package on2020_06.on2020_06_10_Codeforces___Codeforces_Round__411__Div__1_.B__Minimum_number_of_steps;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class BMinimumNumberOfSteps {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        Modular mod = new Modular(1e9 + 7);
        int b = 0;
        int ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] == 'a') {
                ans = mod.plus(ans, b);
                b = mod.plus(b, b);
            } else {
                b = mod.plus(b, 1);
            }
        }
        out.println(ans);
    }
}
