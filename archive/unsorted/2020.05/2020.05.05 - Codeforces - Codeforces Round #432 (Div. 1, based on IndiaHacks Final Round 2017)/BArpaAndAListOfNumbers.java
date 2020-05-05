package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongPreSum;

public class BArpaAndAListOfNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long x = in.readLong();
        long y = in.readLong();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        long ans = n * x;

        int limit = 2000000;
        int[] cnts = new int[limit + 1];
        long[] sum = new long[limit + 1];
        for (int v : a) {
            cnts[v]++;
            sum[v] += v;
        }

        IntegerPreSum cPs = new IntegerPreSum(i -> cnts[i], limit + 1);
        LongPreSum sPs = new LongPreSum(i -> sum[i], limit + 1);

        for (int i = 2; i <= limit; i++) {
            //ky >= x => k >= x / y
            int k = (int) DigitUtils.ceilDiv(x, y);

            long req = 0;
            for (int j = i; j <= limit; j += i) {
                int l = j - i + 1;
                int r = j;
                int threshold = r - k;
                if (l <= threshold) {
                    req += x * cPs.intervalSum(l, threshold);
                    req += y * ((long) cPs.intervalSum(threshold + 1, r) * j - sPs.intervalSum(threshold + 1, r));
                }else{
                    req += y * ((long) cPs.intervalSum(l, r) * j - sPs.intervalSum(l, r));
                }
            }

            ans = Math.min(ans, req);
        }

        out.println(ans);
    }
}
