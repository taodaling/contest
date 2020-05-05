package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AMaximumSplitting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int cnt = n / 4;

        int remain = n % 4;

        int extra = 0;
        if (remain % 2 == 1) {
            cnt -= 2;
            extra++;
        }

        if (remain >= 2) {
            cnt--;
            extra++;
        }

        if(cnt < 0){
            out.println(-1);
            return;
        }

        out.println(cnt + extra);
    }
}
