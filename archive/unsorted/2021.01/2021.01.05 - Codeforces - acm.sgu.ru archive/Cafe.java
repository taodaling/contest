package contest;


import template.algo.BinarySearch;
import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.stream.Collectors;

public class Cafe {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int r = in.ri();
        int[] a = new int[n];
        in.populate(a);
        long two = 0;
        long three = 0;
        for (int x : a) {
            if (x % 2 == 1) {
                three++;
                x -= 3;
            }
            two += x / 2;
        }

        long finalThree = three;
        long finalTwo = two;
        LongPredicate predicate = new LongPredicate() {
            @Override
            public boolean test(long mid) {
                long two = finalTwo;
                long three = finalThree;

                long sumRemain = 0;
                long threeTwo = 0;
                long twoTwo = 0;
                if (r % 2 == 1) {
                    long cast = Math.min(three, mid);
                    sumRemain = (mid - cast) * (r / 2) + cast * ((r - 3) / 2);
                    threeTwo = (mid - cast) * (r / 6) + cast * ((r - 3) / 6);
                    twoTwo = (mid - cast) * (r / 4) + cast * ((r - 3) / 4);
                    three -= cast;
                } else {
                    sumRemain = mid * (r / 2);
                    threeTwo = mid * (r / 6);
                    twoTwo = mid * (r / 4);
                }

                long cast32 = Math.min(three / 2, threeTwo);
                three -= cast32 * 2;
                sumRemain -= cast32 * 3;
                long cast22 = Math.min(three, twoTwo);
                three -= cast22;
                sumRemain -= cast22 * 2;
                return two <= sumRemain && three <= 0;
            }
        };

        long ans = BinarySearch.firstTrue(predicate, 1, (int) 1e9);
        out.println(ans);
    }
}
