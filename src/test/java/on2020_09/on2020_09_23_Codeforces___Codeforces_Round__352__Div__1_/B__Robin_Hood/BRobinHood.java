package on2020_09.on2020_09_23_Codeforces___Codeforces_Round__352__Div__1_.B__Robin_Hood;



import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BRobinHood {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] c = new int[n];
        in.populate(c);
        long total = Arrays.stream(c).mapToLong(x -> x).sum();

        IntBinarySearch poor = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                long sum = 0;
                for (int x : c) {
                    sum += Math.max(0, mid - x);
                }
                return sum > k;
            }
        };

        IntBinarySearch rich = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                long sum = 0;
                for (int x : c) {
                    sum += Math.max(0, x - mid);
                }
                return sum <= k;
            }
        };

        int lowest = poor.binarySearch(0, (int) 1e9);
        int highest = rich.binarySearch(0, (int) 1e9);
        if (poor.check(lowest)) {
            lowest--;
        }

        if (lowest >= highest) {
            out.println(total % n == 0 ? 0 : 1);
            return;
        }

        out.println(highest - lowest);
    }
}
