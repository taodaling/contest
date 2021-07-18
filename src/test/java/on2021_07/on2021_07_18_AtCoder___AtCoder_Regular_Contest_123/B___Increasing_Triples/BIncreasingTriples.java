package on2021_07.on2021_07_18_AtCoder___AtCoder_Regular_Contest_123.B___Increasing_Triples;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class BIncreasingTriples {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        int[] c = in.ri(n);
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Randomized.shuffle(c);
        Arrays.sort(a);
        Arrays.sort(b);
        Arrays.sort(c);
        int ans = 0;
        for (int i = 0, j = 0, k = 0; i < n; i++) {
            while (j < n && b[j] <= a[i]) {
                j++;
            }
            if(j == n){
                break;
            }
            while(k < n && c[k] <= b[j]){
                k++;
            }
            if(k == n){
                break;
            }
            ans++;
            j++;
            k++;
        }
        out.println(ans);
    }
}
