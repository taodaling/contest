package on2020_05.on2020_05_06_.Task;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Nimber;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long b = in.readLong();

        long ans = Nimber.product(a, b);
        out.println(ans);
    }
}
