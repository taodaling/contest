package contest;

import template.algo.IntBinarySearch;
import template.algo.LongBinarySearch;
import template.algo.LongTernarySearch;
import template.algo.PreSum;
import template.datastructure.PrefixIncrementOnePriorityQueue;
import template.graph.ErdosGallaiTheorem;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.LongUnaryOperator;

public class ENewYearAndTheAcquaintanceEstimation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[1 + n];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        Randomized.randomizedArray(a, 1, n + 1);
        Arrays.sort(a, 1, n + 1);
        PreSum ps = new PreSum(a);
        int offset = (int) (ps.intervalSum(1, n) % 2);
        long l = 0;
        long r = (n - offset) / 2;


        for (int k = 1; k <= n; k++) {
            int finalK = k;
            LongUnaryOperator operator = (mid) -> {
                mid = mid * 2 + offset;
                long left = ps.post(n - finalK + 1);
                left -= Math.min(finalK, mid);

                int firstIndex = n - finalK;
                int lastIndex = (int) (n - mid + 1);
                int leftBound = SequenceUtils.leftBound(a, finalK, 1, n - finalK);
                int rightBound = SequenceUtils.rightBound(a, finalK, 1, n - finalK);
                long right = 0;
                if (n - finalK < 1) {
                    right = 0;
                } else if (a[rightBound] > finalK) {
                    right = (long) finalK * firstIndex;
                } else if (a[leftBound] < finalK) {
                    right = ps.intervalSum(1, firstIndex) - (firstIndex - lastIndex + 1);
                } else if (leftBound > rightBound) {
                    if (lastIndex > rightBound) {
                        right = ps.intervalSum(1, rightBound) + (long) finalK * (firstIndex - rightBound);
                    } else {
                        right = ps.intervalSum(1, rightBound) + (long) finalK * (firstIndex - rightBound)
                                - (rightBound - lastIndex + 1);
                    }
                } else {
                    if (lastIndex > rightBound) {
                        right = ps.intervalSum(1, rightBound) + (long) finalK * (firstIndex - rightBound);
                    } else {
                        right = ps.intervalSum(1, rightBound) + (long) finalK * (firstIndex - rightBound)
                                - (rightBound - lastIndex + 1);
                    }
                }


//                // if(mid > 10)
//                {
//                    IntegerList list = new IntegerList();
//                    list.addAll(a, 1, n);
//                    int[] b = list.toArray();
//                    Arrays.sort(b);
//                    SequenceUtils.reverse(b, 0, n - 1);
//                    for (int i = 0; i < mid; i++) {
//                        b[i]--;
//                    }
//                    boolean p = ErdosGallaiTheorem.possible(b, finalK);
//                    if (p != left - right - (long) finalK * (finalK - 1) <= 0) {
//                        throw new RuntimeException();
//                    }
//                }
                return left - right - (long) finalK * (finalK - 1);
            };
            LongTernarySearch ts = new LongTernarySearch(operator);

            long center = ts.find(l, r);
            if (operator.applyAsLong(center) > 0) {
                out.println(-1);
                return;
            }
            LongBinarySearch lbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    return operator.applyAsLong(mid) <= 0;
                }
            };
            LongBinarySearch rbs = new LongBinarySearch() {
                @Override
                public boolean check(long mid) {
                    return operator.applyAsLong(mid) > 0;
                }
            };
            l = lbs.binarySearch(l, center);
            r = rbs.binarySearch(center, r);
            if (rbs.check(r)) {
                r--;
            }
        }

        if (l > r) {
            out.println(-1);
            return;
        }

        for (long i = l; i <= r; i++) {
            out.append(i * 2 + offset).append(' ');
        }
    }

}
