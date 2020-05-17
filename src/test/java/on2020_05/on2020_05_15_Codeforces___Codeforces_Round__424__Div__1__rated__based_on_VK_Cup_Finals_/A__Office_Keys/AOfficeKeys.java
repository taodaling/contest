package on2020_05.on2020_05_15_Codeforces___Codeforces_Round__424__Div__1__rated__based_on_VK_Cup_Finals_.A__Office_Keys;



import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class AOfficeKeys {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int p = in.readInt();
        int[] a = new int[n];
        int[] b = new int[k];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < k; i++) {
            b[i] = in.readInt();
        }
        Randomized.shuffle(a);
        Randomized.shuffle(b);
        Arrays.sort(a);
        Arrays.sort(b);

        int ans = Integer.MAX_VALUE;
        for (int j = 0; j + n <= k; j++) {
            int time = 0;
            for (int i = 0; i < n; i++) {
                int local = Math.abs(a[i] - b[j + i]) + Math.abs(b[j + i] - p);
                time = Math.max(time, local);
            }
            ans = Math.min(ans, time);
        }

        out.println(ans);
    }
}
