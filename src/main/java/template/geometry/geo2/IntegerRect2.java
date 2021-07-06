package template.geometry.geo2;

import java.util.Arrays;

public class IntegerRect2 {
    public long[] lb = new long[2];
    public long[] rt = new long[2];

    public IntegerRect2(long x0, long y0, long x1, long y1) {
        lb[0] = x0;
        lb[1] = y0;
        rt[0] = x1;
        rt[1] = y1;
    }

    public IntegerRect2() {
    }

    public long area() {
        long ans = 1;
        for (int i = 0; i < 2; i++) {
            ans *= Math.max(0, rt[i] - lb[i]);
        }
        return ans;
    }

    public boolean isEmpty() {
        return area() == 0;
    }

    public boolean valid() {
        return lb[0] <= rt[0] && lb[1] <= rt[1];
    }

    public static IntegerRect2 intersect(IntegerRect2 a, IntegerRect2 b) {
        IntegerRect2 ans = new IntegerRect2();
        for (int i = 0; i < 2; i++) {
            ans.lb[i] = Math.max(a.lb[i], b.lb[i]);
            ans.rt[i] = Math.min(a.rt[i], b.rt[i]);
        }
        return ans;
    }

    @Override
    public String toString() {
        return Arrays.toString(lb) + ":" + Arrays.toString(rt);
    }
}
