package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class InputTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long ans = 0;
        while(in.hasMore()){
            ans ^= in.rl();
        }
        out.println(ans);
    }
}
