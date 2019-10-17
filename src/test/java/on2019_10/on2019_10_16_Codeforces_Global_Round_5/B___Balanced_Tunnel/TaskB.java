package on2019_10.on2019_10_16_Codeforces_Global_Round_5.B___Balanced_Tunnel;



import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Power power = new NumberTheory.Power(mod);

        int ans = power.pow(2, m);
        ans = mod.subtract(ans, 1);
        ans = power.pow(ans, n);

        out.println(ans);
    }
}
