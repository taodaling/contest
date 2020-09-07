package on2020_09.on2020_09_04_Library_Checker.Discrete_Logarithm;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.GenericModLog;
import template.math.RelativePrimeModLog;

public class DiscreteLogarithm {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();
        int m = in.readInt();

        GenericModLog log = new GenericModLog(x, m);
        int k = log.log(y);
        out.println(k);
    }
}
