package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ABarkToUnlock {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        String pwd = in.readString();
        int n = in.readInt();
        String[] can = new String[n];
        for (int i = 0; i < n; i++) {
            can[i] = in.readString();
        }

        for (String s : can) {
            for (String t : can) {
                if ((s + t).indexOf(pwd) >= 0) {
                    out.println("YES");
                    return;
                }
            }
        }

        out.println("NO");
    }
}
