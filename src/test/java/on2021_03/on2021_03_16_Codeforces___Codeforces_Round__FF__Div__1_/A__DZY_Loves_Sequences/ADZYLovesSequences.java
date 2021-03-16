package on2021_03.on2021_03_16_Codeforces___Codeforces_Round__FF__Div__1_.A__DZY_Loves_Sequences;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ADZYLovesSequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] pre = new int[n];
        int[] post = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                pre[i] = 1;
            } else {
                pre[i] = a[i] <= a[i - 1] ? 1 : pre[i - 1] + 1;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1) {
                post[i] = 1;
            } else {
                post[i] = a[i] >= a[i + 1] ? 1 : post[i + 1] + 1;
            }
        }
        int ans = Arrays.stream(pre).max().getAsInt();
        for (int i = 0; i < n; i++) {
            //larger than last
            if (i > 0) {
                int h = a[i - 1] + 1;
                int cand = pre[i - 1] + 1;
                if (i + 1 < n && a[i + 1] > h) {
                    cand += post[i + 1];
                }
                ans = Math.max(ans, cand);
            }
            if(i + 1 < n){
                int cand = post[i + 1] + 1;
                ans = Math.max(ans, cand);
            }
        }
        out.println(ans);
    }
}
