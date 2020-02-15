package on2020_02.on2020_02_09_Codeforces_Round__618__Div__1_.C__Water_Balance;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerPreSum;
import template.primitve.generated.LongPreSum;

import java.text.DecimalFormat;
import java.util.Arrays;

public class CWaterBalance {
    LongPreSum lps;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readLong();
        }
        lps = new LongPreSum(a);
        int[] right = new int[n];
        double[] ans = new double[n];
        right[n - 1] = n - 1;
        for (int i = n - 1; i >= 0; i--) {
            right[i] = i;
            double avg = avg(i, i);
            while (right[i] + 1 < n && avg > avg(i, right[right[i] + 1])) {
                avg = avg(i, right[right[i] + 1]);
                right[i] = right[right[i] + 1];
            }
        }
        for (int i = 0; i < n; i = right[i] + 1) {
            double avg = avg(i, right[i]);
            for (int j = i; j <= right[i]; j++) {
                ans[j] = avg;
            }
        }

        //System.err.println(Arrays.toString(right));
        DecimalFormat df = new DecimalFormat("0.000000000000000");
        for (int i = 0; i < n; i++) {
            out.println(df.format(ans[i]));
        }
    }

    public double avg(int l, int r) {
        return lps.intervalSum(l, r) / (double) (r - l + 1);
    }

}
