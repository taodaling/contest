package contest;

import template.algo.IntBinarySearch;
import template.graph.ErdosGallaiTheorem;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.Randomized;
import template.utils.SortUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;

public class ENewYearAndTheAcquaintanceEstimation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        Randomized.randomizedArray(a);
        Arrays.sort(a);
        SequenceUtils.reverse(a, 0, n - 1);
        IntegerList buf = new IntegerList(a.length);
        IntegerList buf2 = new IntegerList(a.length);
        long sum = 0;
        int zeroCnt = 0;
        for(int i = 0; i < n; i++){
            sum += a[i];
            if(a[i] == 0){
                zeroCnt++;
            }
        }
        int parity = (int) (sum % 2);
        int limit = (n - zeroCnt - parity) / 2;

        LongUnaryOperator function = (mid) -> {
            mid = mid * 2 + parity;
            buf.clear();
            buf.addAll(a);
            int[] data = buf.getData();
            for(int i = 0; i < mid; i++){
                data[i]--;
            }
            int[] newData = buf2.getData();
            SortUtils.mergeDescending(data, 0, (int)mid - 1, data, (int)mid, n - 1, newData, 0);
            return ErdosGallaiTheorem.maxOnAllK(newData);
        };

        LongTernarySearch ts = new LongTernarySearch(function);
        int center = ts.find(0, limit);
        if(function.applyAsLong(center) > 0){
            out.println(-1);
            return;
        }
        IntBinarySearch lbs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return function.applyAsLong(mid) <= 0;
            }
        };
        IntBinarySearch rbs = new IntBinarySearch() {
            @Override
            public boolean check(int mid) {
                return function.applyAsLong(mid) > 0;
            }
        };
        int left = lbs.binarySearch(0, center);
        int right = rbs.binarySearch(center, limit);
        if(function.applyAsLong(right) > 0){
            right--;
        }

        for(int i = left; i <= right; i++){
            out.println(i * 2 + parity);
        }
    }
}


class LongTernarySearch {
    private LongUnaryOperator operator;

    public LongTernarySearch(LongUnaryOperator operator) {
        this.operator = operator;
    }

    public int find(int l, int r) {
        while (r - l > 2) {
            int ml = l + DigitUtils.floorDiv(r - l, 3);
            int mr = r - DigitUtils.ceilDiv(r - l, 3);
            if (operator.applyAsLong(ml) >= operator.applyAsLong(mr)) {
                l = ml;
            } else {
                r = mr;
            }
        }
        while (l < r) {
            if (operator.applyAsLong(l) < operator.applyAsLong(r)) {
                r--;
            } else {
                l++;
            }
        }
        return l;
    }
}