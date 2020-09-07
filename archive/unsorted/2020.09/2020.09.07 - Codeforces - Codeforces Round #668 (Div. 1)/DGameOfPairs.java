package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DGameOfPairs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        out.println("First");
        int[] ans = new int[n * 2];


        if(n % 2 == 0){
            for (int i = 0; i < n * 2; i++) {
                if (i < n) {
                    ans[i] = i;
                } else {
                    ans[i] = i - n;
                }
            }
        }
        else {
        }


        for (int i = 0; i < n * 2; i++) {
            out.append(ans[i] + 1).append(' ');
        }

        out.flush();
    }
}
