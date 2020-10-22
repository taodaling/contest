package contest;

import template.algo.MatroidIndependentSet;
import template.algo.MatroidIntersect;
import template.binary.Bits;
import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;

import java.io.PrintWriter;
import java.util.Arrays;

public class TaskD {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        long[] a = new long[n];
        in.populate(a);


        IntegerArrayList colorList = new IntegerArrayList();
        LongArrayList numList = new LongArrayList();
        int m = in.readInt();
        for (int i = 0; i < m; i++) {
            int k = in.readInt();
            for (int j = 0; j < k; j++) {
                colorList.add(i);
                numList.add(in.readLong());
            }
        }
        for (int i = 0; i < n; i++) {
            colorList.add(m + i);
            numList.add(a[i]);

        }

        int[] colors = colorList.toArray();
        long[] nums = numList.toArray();

        MatroidIntersect mi = new MatroidIntersect(nums.length);
        boolean[] ans = mi.intersect(MatroidIndependentSet.ofColor(colors), MatroidIndependentSet.ofLinearBasis(nums));

        int size = 0;
        for (int i = 0; i < nums.length; i++) {
            if (ans[i]) {
                size++;
            }
        }
        assert size <= n + m;
        if (size != m + n) {
            out.println(-1);
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (ans[i] && colors[i] < m) {
                out.println(nums[i]);
            }
        }
    }
}

