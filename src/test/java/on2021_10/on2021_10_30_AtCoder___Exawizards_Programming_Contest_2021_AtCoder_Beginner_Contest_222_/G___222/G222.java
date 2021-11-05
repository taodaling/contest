package on2021_10.on2021_10_30_AtCoder___Exawizards_Programming_Contest_2021_AtCoder_Beginner_Contest_222_.G___222;



import template.algo.DivisionDescending;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedEulerFunction;
import template.math.GCDs;
import template.math.Power;

public class G222 {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        if (m % 2 == 0) {
            m /= 2;
        }
        m *= 9;
        if (GCDs.gcd(10, m) > 1) {
            out.println(-1);
            return;
        }
        Power pow = new Power(m);
        int phi = CachedEulerFunction.get(m);
        assert pow.pow(10, phi) == 1;
        long ans = DivisionDescending.find(phi, x -> pow.pow(10, x) == 1);
        assert pow.pow(10, ans) == 1;
        out.println(ans);
    }

}
