package on2021_08.on2021_08_13_CS_Academy___Virtual_Beta_Round__7.Partial_Ladder_Graph;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ModMatrix;
import template.utils.Debug;

public class PartialLadderGraph {
    int mod = (int)1e9 + 7;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        ModMatrix T = new ModMatrix(2, 2);
        T.set(0, 0, 1);
        T.set(0, 1, 2);
        T.set(1, 0, 1);
        T.set(1, 1, 4);

        ModMatrix Tn = ModMatrix.pow(T, n, mod);
        ModMatrix v = new ModMatrix(2, 1);
        v.set(0, 0, 1);
        ModMatrix Tnv = ModMatrix.mul(Tn, v, mod);
        long ans = Tnv.get(0, 1);
        out.println(ans);
    }
}
