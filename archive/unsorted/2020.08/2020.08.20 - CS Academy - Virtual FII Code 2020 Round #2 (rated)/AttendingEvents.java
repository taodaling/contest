package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AttendingEvents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int next = 0;
        int ans = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt() - 1;
            if (x == next) {
                ans++;
                next = (next + 1) % 3;
            }
        }
        out.println(ans);
    }
}
