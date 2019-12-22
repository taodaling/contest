package on2019_12.on2019_12_22_AtCoder_Beginner_Contest_148.D___Brick_Break;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (in.readInt() == r + 1) {
                r++;
            }
        }
        if (r == 0) {
            out.println(-1);
        } else {
            out.println(n - r);
        }
    }
}
