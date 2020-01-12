package on2020_01.on2020_01_11_Dwango_Programming_Contest_6th.B___Fusing_Slimes0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.Modular;
import template.math.Power;

public class BFusingSlimes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] x = new int[n];
        Modular mod = new Modular(1e9 + 7);
        InverseNumber inverseNumber = new InverseNumber(n, mod);
        int sum = 0;
        int exp = 0;
        for(int i = 0; i < n; i++){
            x[i] = in.readInt();
        }
        for(int i = 0; i < n - 1; i++){
            int dist = x[i + 1] - x[i];
            sum = mod.plus(sum, inverseNumber.inverse(i + 1));
            exp = mod.plus(exp, mod.mul(sum, dist));
        }
        for(int i = 1; i < n; i++){
            exp = mod.mul(exp, i);
        }
        out.println(exp);
    }
}
