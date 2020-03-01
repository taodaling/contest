package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class APhoneNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int eight = 0;
        for (int i = 0; i < n; i++) {
            if (in.readChar() == '8') {
                eight++;
            }
        }
        int ans = Math.min(n / 11, eight);
        out.println(ans);
    }
}
