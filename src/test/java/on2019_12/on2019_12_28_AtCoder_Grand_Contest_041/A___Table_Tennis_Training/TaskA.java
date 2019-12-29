package on2019_12.on2019_12_28_AtCoder_Grand_Contest_041.A___Table_Tennis_Training;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long a = in.readLong();
        long b = in.readLong();
        if (a > b) {
            long tmp = a;
            a = b;
            b = tmp;
        }
        if ((b - a) % 2 == 0) {
            out.println((b - a) / 2);
            return;
        }
        long min = Math.min(a + (b - a - 1) / 2,
                (n + 1 - b) + (n - (a + (n + 1 - b))) / 2);
        out.println(min);
    }
}
