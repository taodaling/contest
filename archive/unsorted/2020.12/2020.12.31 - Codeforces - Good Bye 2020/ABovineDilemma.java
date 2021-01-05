package contest;

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
