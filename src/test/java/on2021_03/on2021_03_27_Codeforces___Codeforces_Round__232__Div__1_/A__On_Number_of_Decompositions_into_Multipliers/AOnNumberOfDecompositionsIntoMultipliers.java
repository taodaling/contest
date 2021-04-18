package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__232__Div__1_.A__On_Number_of_Decompositions_into_Multipliers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.PollardRho;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AOnNumberOfDecompositionsIntoMultipliers {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Map<Integer, Integer> map = new HashMap<>();
        for (int x : a) {
            Set<Integer> set = PollardRho.findAllFactors(x);
            for (int y : set) {
                int p = 0;
                while (x % y == 0) {
                    x /= y;
                    p++;
                }
                map.put(y, map.getOrDefault(y, 0) + p);
            }
        }

        long ans = 1;
        for (int v : map.values()) {
            //x1 + x2 + ... + xn = v
            ans = ans * comb.combination(v + (n - 1), v) % mod;
        }
        out.println(ans);
    }
}
