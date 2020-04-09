package on2020_04.on2020_04_09_Codeforces_Round__632__Div__2_.F__Kate_and_imperfection;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;

public class FKateAndImperfection {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] factors = new int[n + 1];
        factors[1] = 1;
        for(int i = 1; i <= n; i++){
            for(int j = i + i; j <= n; j += i){
                factors[j] = i;
            }
        }
        debug.debug("factors", factors);
        Randomized.shuffle(factors, 1, n + 1);
        Arrays.sort(factors, 1, n + 1);
        for(int i = 2; i <= n; i++) {
            out.append(factors[i]).append(' ');
        }
    }
}
