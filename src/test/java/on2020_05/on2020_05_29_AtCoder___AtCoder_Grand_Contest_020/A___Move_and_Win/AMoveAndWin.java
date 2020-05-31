package on2020_05.on2020_05_29_AtCoder___AtCoder_Grand_Contest_020.A___Move_and_Win;



import template.io.FastInput;
import template.io.FastOutput;

public class AMoveAndWin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        int between = Math.abs(a - b) - 1;
        if (between % 2 == 0) {
            out.println("Borys");
        } else {
            out.println("Alice");
        }
    }
}
