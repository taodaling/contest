package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TheGoodTheBadAndTheUgly {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String s = in.rs();
        long v = in.rl();
        long[] decode = new long[4];
        for(int i = 0; i < 4; i++, v >>= 8){
            decode[i] = v & 255;
        }
        long ans = 0;
        for(int i = 0; i < 4; i++){
            ans = (ans << 8) | decode[i];
        }
        out.println(ans);
    }
}
