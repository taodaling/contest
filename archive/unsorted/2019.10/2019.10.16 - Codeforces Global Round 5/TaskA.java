package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();

        int take1 = Math.min(b, c / 2);
        b -= take1;
        int take2 = Math.min(a, b / 2);
        out.println((take1 + take2) * 3);
    }
}
