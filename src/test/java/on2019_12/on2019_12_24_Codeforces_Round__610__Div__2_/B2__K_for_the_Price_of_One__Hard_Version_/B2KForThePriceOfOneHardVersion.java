package on2019_12.on2019_12_24_Codeforces_Round__610__Div__2_.B2__K_for_the_Price_of_One__Hard_Version_;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerPreSum;
import template.primitve.generated.LongPreSum;
import template.rand.Randomized;

import java.util.Arrays;

public class B2KForThePriceOfOneHardVersion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long p = in.readInt();
        int k = in.readInt();

        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        Randomized.randomizedArray(a);
        Arrays.sort(a);

        int since = 0;
        long sum = 0;
        for (; since + k - 1 < n; since += k) {
            sum += a[since + k - 1];
            if (sum > p) {
                break;
            }
        }
        if(since >= n){
            out.println(n);
            return;
        }

        IntBinarySearch ibs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                long sum = 0;
                for (int i = mid; i >= 0; i--) {
                    sum += a[i];
                    if (i >= k - 1) {
                        i -= k - 1;
                    }
                }
                return sum > p;
            }
        };

        int r = Math.min(since + k - 1, n - 1);
        int ans = ibs.binarySearch(since, r);
        if (ibs.check(ans)) {
            ans--;
        }
        out.println(ans + 1);
    }
}
