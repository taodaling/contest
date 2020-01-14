package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BYetAnotherMemeProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long b = in.readInt();
        int cnt = 0;
        for (int i = 9; i <= b; i = i * 10 + 9) {
            cnt++;
        }
        out.println(cnt * a);
    }
}
