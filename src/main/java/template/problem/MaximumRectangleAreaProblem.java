package template.problem;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.Int2ToIntegerFunction;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;


public class MaximumRectangleAreaProblem {
    /**
     * <pre>
     * 给定二维矩阵g，找到最大面积的子矩形，要求矩形中不能包含任意的非0整数
     * </pre>
     * <pre>
     * return area, l, r, b, t
     * </pre>
     * <pre>
     * 时间复杂度为O(n)，n为mat单元格数
     * </pre>
     */
    public static int[] solve(Int2ToIntegerFunction mat, int n, int m) {
        int[] low = new int[m];
        int[] lb = new int[m];
        int[] rb = new int[m];
        IntegerDequeImpl dq = new IntegerDequeImpl(m);

        int best = 0;
        int left = 0;
        int right = -1;
        int up = -1;
        int bottom = 0;
        Arrays.fill(low, n);
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                if (mat.apply(i, j) != 0) {
                    low[j] = i;
                }
            }
            dq.clear();
            for (int j = 0; j < m; j++) {
                while (!dq.isEmpty() && low[dq.peekLast()] >= low[j]) {
                    dq.removeLast();
                }
                lb[j] = dq.isEmpty() ? 0 : dq.peekLast() + 1;
                dq.addLast(j);
            }
            dq.clear();
            for (int j = m - 1; j >= 0; j--) {
                while (!dq.isEmpty() && low[dq.peekFirst()] >= low[j]) {
                    dq.removeFirst();
                }
                rb[j] = dq.isEmpty() ? m - 1 : dq.peekFirst() - 1;
                dq.addFirst(j);
            }
            for (int j = 0; j < m; j++) {
                int cur = (rb[j] - lb[j] + 1) * (low[j] - i);
                if (cur > best) {
                    best = cur;
                    left = lb[j];
                    right = rb[j];
                    up = i;
                    bottom = low[j] - 1;
                }
            }
        }

        return new int[]{best, left, right, bottom, up};
    }

    private static long greaterPoint(long h1, long l1, long h2, long l2) {
        return greaterPoint0(h1, (1 - l1) * h1, h2, (1 - l2) * h2);
    }

    /**
     * Find min x that ax+b<=cx+d while a<c
     */
    private static long greaterPoint0(long a, long b, long c, long d) {
        //(c-a)x>=b-d
        return DigitUtils.ceilDiv(b - d, c - a);
    }
}
