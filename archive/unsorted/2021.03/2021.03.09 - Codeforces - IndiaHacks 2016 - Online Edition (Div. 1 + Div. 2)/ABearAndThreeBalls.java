package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashSet;
import java.util.Set;

public class ABearAndThreeBalls {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < n; i++){
            set.add(in.ri());
        }
        for(int i = 1; i <= 1000; i++){
            if(set.contains(i) && set.contains(i - 1) && set.contains(i + 1)){
                out.println("YES");
                return;
            }
        }
        out.println("NO");
    }
}
