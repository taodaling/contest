package on2019_11.on2019_11_16_Codeforces_Round__600__Div__2_.C___Sweets_Eating;



import template.FastInput;
import template.FastOutput;
import template.PreSum;
import template.Randomized;

import java.util.Arrays;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        Randomized.randomizedArray(a, 0, n);
        Arrays.sort(a);

        long[] helper = new long[n];
        for (int i = 0; i < n; i++) {
            helper[i] = a[i];
            if (i >= m) {
                helper[i] += helper[i - m];
            }
        }

        long[] ans = new long[n];
        long sum = 0;
        for (int i = n - 1; i >= 0; i--) {
            long d = (n - 1 - i) / m + 1;
            sum += d * a[i];
        }

        ans[n - 1] = sum;
        for (int i = n - 2; i >= 0; i--) {
            ans[i] = ans[i + 1] - helper[i + 1];
        }

        for (int i = 0; i < n; i++) {
            out.println(ans[i]);
        }
    }
}
