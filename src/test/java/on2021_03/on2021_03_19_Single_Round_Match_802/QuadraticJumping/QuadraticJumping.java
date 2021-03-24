package on2021_03.on2021_03_19_Single_Round_Match_802.QuadraticJumping;



import template.algo.BinarySearch;
import template.algo.IntBinarySearch;
import template.math.IntMath;

import java.util.function.IntPredicate;

public class QuadraticJumping {
    boolean[][] possible = new boolean[100 + 1][10000 + 1];

    int limit = 1442251;
    long[] sum = new long[limit + 10];
    public boolean g(int i, long j) {
        if(sum[i] < j){
            return false;
        }
        if (i == 0) {
            return j == 0;
        }
        if (i <= 100 && j <= 10000) {
            return possible[(int) i][(int) j];
        }
        //greedy
        int k = IntMath.floorSqrt(j);
        if(i == k){
            IntPredicate predicate = mid -> {
                return sum[i] - sum[mid - 1] <= j;
            };
            k = BinarySearch.lastTrue(predicate, 1, i);
        }

        k = Math.min(k, i);
        return g(k - 1, j - (long) k * k);
    }

    public long jump(long goal) {
        possible[0][0] = true;
        for (int i = 1; i <= 100; i++) {
            for (int j = 0; j <= 10000; j++) {
                possible[i][j] = possible[i - 1][j];
                if (j - i * i >= 0 && possible[i - 1][j - i * i]) {
                    possible[i][j] = true;
                }
            }
        }

        long[] sum = new long[limit + 10];
        for (int i = 1; i < limit + 10; i++) {
            sum[i] = sum[i - 1] + (long) i * i;
        }

        IntBinarySearch bs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                for (int i = 0; i < 8; i++) {
                    long back = sum[mid + i] - goal;
                    if (back < 0) {
                        continue;
                    }
                    if(back % 2 != 0){
                        continue;
                    }
                    if (g(mid + i, back / 2)) {
                        return true;
                    }
                }
                return false;
            }
        };

        int mid = bs.binarySearch(1, limit);
        for (int i = 0; i < 8; i++) {
            long back = sum[mid + i] - goal;
            if (back < 0) {
                continue;
            }
            if(back % 2 != 0){
                continue;
            }
            if (g(mid + i, back / 2)) {
                return mid + i;
            }
        }
        assert false;
        return -1;
    }
}
