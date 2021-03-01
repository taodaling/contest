package contest;

import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        LinearBasis lb = new LinearBasis();
        for(int i = 0; i < n; i++){
            lb.add(in.rl());
        }
        long max = lb.theMaximumNumberXor(0);
        out.println(max);
    }
}
