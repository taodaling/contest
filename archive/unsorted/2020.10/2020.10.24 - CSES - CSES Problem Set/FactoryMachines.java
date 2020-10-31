package contest;

import template.algo.LongBinarySearch;
import template.io.FastInput;

import java.io.PrintWriter;

public class FactoryMachines {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long t = in.readLong();
        int[] req = new int[n];
        in.populate(req);
        long inf = (long) 1e18;
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                long sum = 0;
                for (int r : req) {
                    sum += mid / r;
                    sum = Math.min(sum, inf);
                }
                return sum >= t;
            }
        };

        long ans = lbs.binarySearch(0, inf, false);
        out.println(ans);
    }
}
