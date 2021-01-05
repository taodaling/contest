package on2020_12.on2020_12_31_Codeforces___Good_Bye_2020.A__Bovine_Dilemma;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashSet;
import java.util.Set;

public class ABovineDilemma {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = new int[n];
        in.populate(x);
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                set.add(x[j] - x[i]);
            }
        }
        out.println(set.size());
    }
}
