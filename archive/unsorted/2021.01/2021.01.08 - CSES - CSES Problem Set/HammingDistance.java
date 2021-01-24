package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class HammingDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                a[i] = (a[i] << 1) | (in.rc() - '0');
            }
        }
        int ans = k;
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                ans = Math.min(ans, Integer.bitCount(a[i] ^ a[j]));
            }
        }
        out.println(ans);
    }
}
