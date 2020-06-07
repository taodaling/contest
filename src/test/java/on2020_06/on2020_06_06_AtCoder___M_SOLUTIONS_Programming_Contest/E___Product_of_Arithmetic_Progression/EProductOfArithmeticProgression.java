package on2020_06.on2020_06_06_AtCoder___M_SOLUTIONS_Programming_Contest.E___Product_of_Arithmetic_Progression;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class EProductOfArithmeticProgression {
    Modular mod = new Modular(1000003);
    Factorial fact = new Factorial(1000003, mod);
    Power pow = new Power(mod);
    InverseNumber inv = new ModPrimeInverseNumber(1000003 - 1, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int d = in.readInt();
        int n = in.readInt();

        if (d == 0) {
            out.println(pow.pow(x, n));
            return;
        }
        x = mod.mul(x, inv.inverse(d));
        //x(x+1)...(x+n-1)
        if (x + n - 1 >= mod.getMod()) {
            out.println(0);
            return;
        }
        int ans = fact.fact(x + n - 1);
        if (x - 1 >= 0) {
            ans = mod.mul(ans, fact.invFact(x - 1));
        }
        ans = mod.mul(ans, pow.pow(d, n));
        out.println(ans);
    }


}
