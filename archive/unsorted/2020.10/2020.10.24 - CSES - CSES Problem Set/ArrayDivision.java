package contest;

import template.algo.IntBinarySearch;
import template.algo.LongBinarySearch;
import template.io.FastInput;

import java.io.PrintWriter;

public class ArrayDivision {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] x = new int[n];
        in.populate(x);
        LongBinarySearch ibs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                int req = 0;
                for (int i = 0; i < n; i++) {
                    int j = i;
                    if (x[i] > mid) {
                        return false;
                    }
                    long sum = x[i];
                    while (j + 1 < n && sum + x[j + 1] <= mid) {
                        j++;
                        sum += x[j];
                    }
                    req++;
                    i = j;
                }
                return req <= k;
            }
        };

        long ans = ibs.binarySearch(0, (long) 1e18);
        out.println(ans);
    }
}
