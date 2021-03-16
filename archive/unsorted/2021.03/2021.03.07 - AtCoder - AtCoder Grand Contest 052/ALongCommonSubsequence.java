package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ALongCommonSubsequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        in.rs();
        in.rs();
        in.rs();
        out.append(0);
        for (int i = 0; i < n; i++) {
            out.append(1);
        }
        for (int i = 0; i < n; i++) {
            out.append(0);
        }
        out.println();
    }
}
