package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Pok_mon_Evolution;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.function.LongPredicate;

public class PokmonEvolution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long x = in.rl();
        long y = in.rl();
        long ans = (m + n * y) / (x + y);
        ans = Math.min(ans, n);
        out.println(ans);
    }
}
