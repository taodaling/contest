package template.geometry.geo3;

import java.util.Arrays;

public class IntegerRect3 {
    public long[] lb = new long[3];
    public long[] rt = new long[3];

    public IntegerRect3(long x0, long y0, long z0, long x1, long y1, long z1) {
        lb[0] = x0;
        lb[1] = y0;
        lb[2] = z0;
        rt[0] = x1;
        rt[1] = y1;
        rt[2] = z1;
    }

    public IntegerRect3() {
    }

    public long volume() {
        long ans = 1;
        for (int i = 0; i < 3; i++) {
            ans *= Math.max(0, rt[i] - lb[i]);
        }
        return ans;
    }

    public boolean isEmpty() {
        return volume() == 0;
    }

    public static IntegerRect3 intersect(IntegerRect3 a, IntegerRect3 b) {
        IntegerRect3 ans = new IntegerRect3();
        for (int i = 0; i < 3; i++) {
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