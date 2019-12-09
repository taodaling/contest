package on2019_12.on2019_12_08_AtCoder_Beginner_Contest_147.A___Blackjack;





import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        out.println(a + b + c >= 22 ? "bust" : "win");
    }
}
