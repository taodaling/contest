package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CommonDivisors {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = (int) 1e6;
        int[] occur = new int[m + 1];
        for (int i = 0; i < n; i++) {
            occur[in.readInt()]++;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = i + i; j <= m; j += i) {
                occur[i] += occur[j];
            }
        }

        for(int i = m; i >= 1; i--){
            if(occur[i] > 1){
                out.println(i);
                return;
            }
        }
    }
}
