package template.problem;

import template.datastructure.LinkedListBeta;
import template.primitve.generated.datastructure.Int2ToIntegerFunction;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;


public class RectOnGridProblem {
    /**
     * /**
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
    public static int[] maximumRectArea(Int2ToIntegerFunction mat, int n, int m) {
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

    /**
     * <pre>
     * 统计不同大小的所有可行矩阵的数量，其中一个可行矩阵只能覆盖值为0的单元格
     * </pre>
     * <pre>
     * 时间复杂度O(nm)
     * </pre>
     */
    public static long[][] countAvailableRect(Int2ToIntegerFunction mat, int n, int m) {
        long[][] tag = new long[n + 1][m + 1];
        int[] low = new int[m];
        boolean[] active = new boolean[m];
        int[] left = new int[m];
        int[] right = new int[m];
        Arrays.fill(low, n);
        LinkedListBeta<Integer> list = new LinkedListBeta<>();
        LinkedListBeta.Node<Integer>[] nodes = new LinkedListBeta.Node[m];
        for (int i = 0; i < m; i++) {
            nodes[i] = list.addLast(i);
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                if (mat.apply(i, j) != 0) {
                    list.remove(nodes[j]);
                    list.addLast(nodes[j]);
                    low[j] = i;
                }
            }
            Arrays.fill(active, false);
            for (int j : list) {
                int row = low[j] - 1;
                int high = row - i + 1;
                active[j] = true;
                int l = j;
                int r = j;
                tag[high][1]++;
                while (l > 0 && active[l - 1]) {
                    //merge
                    int lr = l - 1;
                    int ll = left[lr];
                    tag[high][lr - ll + 1]--;
                    tag[high][r - l + 1]--;
                    l = ll;
                    tag[high][r - l + 1]++;
                }
                while (r + 1 < m && active[r + 1]) {
                    //merge
                    int rl = r + 1;
                    int rr = right[rl];
                    tag[high][rr - rl + 1]--;
                    tag[high][r - l + 1]--;
                    r = rr;
                    tag[high][r - l + 1]++;
                }
                left[l] = left[r] = l;
                right[r] = right[l] = r;
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= m; j++) {
                tag[i][j] += tag[i + 1][j];
            }
        }
        for (int i = 1; i <= n; i++) {
            long cc = 0;
            long last = 0;
            for (int j = m; j >= 0; j--) {
                cc += tag[i][j];
                last += cc;
                tag[i][j] = last;
            }
        }

        for (int i = 0; i <= m; i++) {
            tag[0][i] = 0;
        }

        for (int i = 0; i <= n; i++) {
            tag[i][0] = 0;
        }

        return tag;
    }
}
