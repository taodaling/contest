package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AFindAmir {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if(n <= 2){
            out.println(0);
            return;
        }
        int ans = n / 2 - 1 + n % 2;
        out.println(ans);
    }
}
