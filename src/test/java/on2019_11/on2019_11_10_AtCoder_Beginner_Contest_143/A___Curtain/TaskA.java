package on2019_11.on2019_11_10_AtCoder_Beginner_Contest_143.A___Curtain;



import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();

        out.println(Math.max(0, a - 2 * b));
    }
}
