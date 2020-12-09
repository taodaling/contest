package on2020_12.on2020_12_06_Codeforces___Codeforces_Global_Round_12.A__Avoid_Trygub;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.Random;

public class AAvoidTrygub {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s, 0);
        Randomized.shuffle(s);
        Arrays.sort(s);
        for(int i = 0; i < n; i++){
            out.append(s[i]);
        }
        out.println();
    }
}
