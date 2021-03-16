package template.problem;

import com.fasterxml.jackson.databind.util.LinkedNode;
import template.datastructure.LinkedListBeta;
import template.primitve.generated.datastructure.Int2ToIntegerFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.CompareUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


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

    public static int[][] maxSquareContainsAtMostKDistinctNumbers(int[][] mat, int k) {
        int n = mat.length;
        int m = mat[0].length;
        if (k == 0) {
            return new int[n][m];
        }

        int[][] maxSquareSize = new int[n][m];
        IntegerArrayList list = new IntegerArrayList(n * m);
        for (int[] r : mat) {
            list.addAll(r);
        }
        list.unique();
        Point2[][] pts = new Point2[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pts[i][j] = new Point2(i, j, list.binarySearch(mat[i][j]));
            }
        }
        Point2[] registries = new Point2[list.size()];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pts[i][j].left = registries[pts[i][j].v];
                registries[pts[i][j].v] = pts[i][j];
            }
            for (int j = 0; j < m; j++) {
                registries[pts[i][j].v] = null;
            }
        }
        for (int j = 0; j < m; j++) {
            for (int i = n - 1; i >= 0; i--) {
                pts[i][j].bot = registries[pts[i][j].v];
                registries[pts[i][j].v] = pts[i][j];
            }
            for (int i = n - 1; i >= 0; i--) {
                registries[pts[i][j].v] = null;
            }
        }
        LinkedListBeta.Node<Point2>[][] forCols = new LinkedListBeta.Node[n][m];
        LinkedListBeta.Node<Point2>[][] forRows = new LinkedListBeta.Node[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                forCols[i][j] = new LinkedListBeta.Node<>(pts[i][j]);
                forRows[i][j] = new LinkedListBeta.Node<>(pts[i][j]);
            }
        }
        LinkedListBeta<Point2>[] rows = new LinkedListBeta[n];
        LinkedListBeta<Point2>[] cols = new LinkedListBeta[m];
        for (int i = 0; i < n; i++) {
            rows[i] = new LinkedListBeta<>();
        }
        for (int i = 0; i < m; i++) {
            cols[i] = new LinkedListBeta<>();
        }
        DistinctCounter dc = new DistinctCounter(new int[list.size()], 0);
        for (int i = n - 1; i >= 0; i--) {
            //update cols
            for (int j = 0; j < m; j++) {
                Point2 cur = pts[i][j];
                if (cur.bot != null) {
                    LinkedListBeta.Node<Point2> botNode = forCols[cur.bot.x][cur.bot.y];
                    if (!botNode.singleton()) {
                        cols[j].remove(botNode);
                    }
                }
                cols[j].addLast(forCols[cur.x][cur.y]);
                if (cols[j].size() > k + 1) {
                    cols[j].remove(cols[j].begin());
                }
            }

            assert dc.distinct == 0;
            assert Arrays.stream(cols).mapToInt(LinkedListBeta::size).max().orElse(-1) <= k + 1;
            for (int j = 0, r = -1; j < m; j++) {
                int lo = (r - j) + i;
                while (lo + 1 < n && r + 1 < m) {
                    lo++;
                    r++;
                    //add col
                    for (LinkedListBeta.Node<Point2> node = cols[r].begin(); node != cols[r].end(); node = node.next) {
                        Point2 pt = node.val;
                        if (pt.x <= lo) {
                            dc.modify(pt.v, 1);
                        }
                    }
                    //add row
                    for (LinkedListBeta.Node<Point2> node = rows[lo].begin(); node != rows[lo].end(); node = node.next) {
                        Point2 pt = node.val;
                        dc.modify(pt.v, 1);
                    }

                    if (dc.distinct > k) {
                        //revoke

                        //remove col
                        for (LinkedListBeta.Node<Point2> node = cols[r].begin(); node != cols[r].end(); node = node.next) {
                            Point2 pt = node.val;
                            if (pt.x <= lo) {
                                dc.modify(pt.v, -1);
                            }
                        }
                        //remove row
                        for (LinkedListBeta.Node<Point2> node = rows[lo].begin(); node != rows[lo].end(); node = node.next) {
                            Point2 pt = node.val;
                            dc.modify(pt.v, -1);
                        }

                        r--;
                        lo--;
                        break;
                    }

                    //apply
                    for (LinkedListBeta.Node<Point2> node = cols[r].begin(); node != cols[r].end(); node = node.next) {
                        Point2 pt = node.val;
                        //remove prev
                        if (pt.left != null) {
                            LinkedListBeta.Node<Point2> leftNode = forRows[pt.left.x][pt.left.y];
                            if (!leftNode.singleton()) {
                                if (pt.x <= lo) {
                                    dc.modify(pt.v, -1);
                                }
                                rows[pt.x].remove(leftNode);
                            }
                        }
                        rows[pt.x].addLast(forRows[pt.x][pt.y]);
                        if (rows[pt.x].size() > k + 1) {
                            LinkedListBeta.Node<Point2> first = rows[pt.x].begin();
                            assert pt.x > lo;
//                            dc.modify(first.val.v, -1);
                            rows[pt.x].remove(first);
                        }
                    }

                    assert Arrays.stream(rows).mapToInt(LinkedListBeta::size).max().orElse(-1) <= k + 1;
                }
                maxSquareSize[i][j] = r - j + 1;

                //remove col
                for (LinkedListBeta.Node<Point2> node = cols[j].begin(); node != cols[j].end(); node = node.next) {
                    Point2 pt = node.val;
                    if (!forRows[pt.x][pt.y].singleton()) {
                        if (pt.x <= lo) {
                            dc.modify(pt.v, -1);
                        }
                        rows[pt.x].remove(forRows[pt.x][pt.y]);
                    }
                }
                //remove row
                for (LinkedListBeta.Node<Point2> node = rows[lo].begin(); node != rows[lo].end(); node = node.next) {
                    Point2 pt = node.val;
                    dc.modify(pt.v, -1);
                }
            }
        }

        assert dc.distinct == 0;

        return maxSquareSize;
    }

    /**
     * 对所有左上角，找到最大方形，其中包含最多k个不同值。
     * O(knm+nm\log_2nm)
     *
     * @return
     */
    public static int[][] maxSquareContainsAtMostKDistinctNumber(int[][] mat, int k) {
        int n = mat.length;
        int m = mat[0].length;
        if (k == 0) {
            return new int[n][m];
        }
        IntegerArrayList list = new IntegerArrayList(n * m);
        for (int i = 0; i < n; i++) {
            list.addAll(mat[i]);
        }
        list.unique();
        DistinctCounter dc = new DistinctCounter(new int[list.size()], 0);
        int[][] maxSquare = new int[n][m];
        Point[][] pts = new Point[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pts[i][j] = new Point(i, j, list.binarySearch(mat[i][j]));
                pts[i][j].node = new LinkedListBeta.Node<>(pts[i][j]);
            }
        }
        LinkedListBeta.Node<Point>[] registry = new LinkedListBeta.Node[list.size()];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pts[i][j].rep = registry[pts[i][j].v];
                registry[pts[i][j].v] = pts[i][j].node;
            }
            for (int j = 0; j < m; j++) {
                registry[pts[i][j].v] = null;
            }
        }
        for (int j = 0; j < m; j++) {
            for (int i = n - 1; i >= 0; i--) {
                pts[i][j].next = registry[pts[i][j].v];
                registry[pts[i][j].v] = pts[i][j].node;
            }
            for (int i = 0; i < n; i++) {
                registry[pts[i][j].v] = null;
            }
        }
        LinkedListBeta<Point>[] rows = new LinkedListBeta[n];
        for (int i = 0; i < n; i++) {
            rows[i] = new LinkedListBeta<>();
        }
        List<LinkedListBeta.Node<Point>>[] cols = new List[m];
        for (int i = 0; i < m; i++) {
            cols[i] = new ArrayList<>(k + 1);
        }
        for (int i = n - 1; i >= 0; i--) {
            assert dc.distinct == 0;
            assert Arrays.stream(rows).mapToInt(LinkedListBeta::size).sum() == 0;
            for (int j = 0; j < m; j++) {
                Point cur = pts[i][j];
                cols[j].remove(cur.next);
                cols[j].add(cur.node);
                if (cols[j].size() > k + 1) {
                    cols[j].remove(0);
                }
                assert isDistinct(cols[j]);
            }
            for (int j = 0, r = -1; j < m; j++) {
                int lo = i + (r - j);
                assert dc.distinct <= k;
                while (dc.distinct <= k && lo + 1 < n && r + 1 < m) {
                    for (Point pt : rows[lo + 1]) {
                        dc.modify(pt.v, 1);
                    }
                    for (LinkedListBeta.Node<Point> node : cols[r + 1]) {
                        Point pt = node.val;
                        if (pt.x <= lo + 1) {
                            dc.modify(pt.v, 1);
                        }
                    }
                    if (dc.distinct > k) {
                        //revoke
                        for (Point pt : rows[lo + 1]) {
                            dc.modify(pt.v, -1);
                        }
                        for (LinkedListBeta.Node<Point> node : cols[r + 1]) {
                            Point pt = node.val;
                            if (pt.x <= lo + 1) {
                                dc.modify(pt.v, -1);
                            }
                        }
                        break;
                    }
                    r++;
                    lo++;
                    //apply
                    for (LinkedListBeta.Node<Point> node : cols[r]) {
                        Point pt = node.val;
                        if (pt.rep != null && !pt.rep.singleton()) {
                            rows[pt.x].remove(pt.rep);
                            assert pt.rep.val.v == pt.v;
                            assert pt.rep.val.x == pt.x;
                            if (pt.x <= lo) {
                                dc.modify(pt.v, -1);
                            }
                        }
                        rows[pt.x].addLast(pt.node);
                        if (rows[pt.x].size() > k + 1) {
                            assert rows[pt.x].begin().val != pt;
                            if (pt.x <= lo) {
                                dc.modify(rows[pt.x].begin().val.v, -1);
                            }
                            rows[pt.x].remove(rows[pt.x].begin());
                        }
                        assert rows[pt.x].size() <= k + 1;
                    }
                    assert match(pts, j, r, i, lo, dc.distinct);
                }

                assert match(pts, j, r, i, lo, dc.distinct);

                assert dc.distinct <= k;
                maxSquare[i][j] = r - j + 1;
                assert maxSquare[i][j] >= 1;

                assert checkAll(rows, j);
                //delete item
                for (LinkedListBeta.Node<Point> node : cols[j]) {
                    Point pt = node.val;
                    if (pt.node.singleton()) {
                        continue;
                    }
                    assert !pt.node.singleton();
                    assert rows[pt.x].begin() == pt.node;
                    rows[pt.x].remove(pt.node);
                    if (pt.x <= lo) {
                        dc.modify(pt.v, -1);
                    }
                }
                assert lo >= i;
                for (Point pt : rows[lo]) {
                    dc.modify(pt.v, -1);
                }
                assert checkAll(rows, j + 1);
                assert match(pts, j + 1, r, i, lo - 1, dc.distinct);
            }
        }


        assert dc.distinct == 0;
        assert Arrays.stream(rows).mapToInt(LinkedListBeta::size).sum() == 0;
        return maxSquare;
    }

    private static boolean isDistinct(List<LinkedListBeta.Node<Point>> pts) {
        return pts.size() == pts.stream().mapToInt(x -> x.val.v).distinct().count();
    }

    private static boolean match(Point[][] mat, int l, int r, int b, int t, int distinct) {
        DistinctCounter dc = new DistinctCounter(new int[mat.length * mat[0].length], 0);
        for (int i = b; i <= t; i++) {
            for (int j = l; j <= r; j++) {
                dc.modify(mat[i][j].v, 1);
            }
        }
        if (dc.distinct != distinct) {
            return false;
        }
        return true;
    }

    private static boolean checkAll(LinkedListBeta<Point>[] lists, int y) {
        for (LinkedListBeta<Point> list : lists) {
            if (!check(list, y)) {
                return false;
            }
        }
        return true;
    }

    private static boolean check(LinkedListBeta<Point> list, int y) {
        Point last = null;
        for (Point pt : list) {
            if (pt.y < y) {
                return false;
            }
            if (last != null) {
                if (last.x != pt.x) {
                    return false;
                }
                if (last.y >= pt.y) {
                    return false;
                }
            }
            last = pt;
        }
        return true;
    }

    private static class Point2 {
        int x;
        int y;
        int v;
        Point2 left;
        Point2 bot;

        public Point2(int x, int y, int v) {
            this.x = x;
            this.y = y;
            this.v = v;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d)", x, y, v);
        }

    }

    private static class Point {
        int x;
        int y;
        int v;
        LinkedListBeta.Node<Point> rep;
        LinkedListBeta.Node<Point> node;
        LinkedListBeta.Node<Point> next;

        public Point(int x, int y, int v) {
            this.x = x;
            this.y = y;
            this.v = v;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d)", x, y, v);
        }
    }

    private static class DistinctCounter {
        int[] occur;
        int distinct;

        public DistinctCounter(int[] occur, int distinct) {
            this.occur = occur;
            this.distinct = distinct;
        }

        public void modify(int i, int x) {
            if (occur[i] > 0) {
                distinct--;
            }
            occur[i] += x;
            assert occur[i] >= 0;
            if (occur[i] > 0) {
                distinct++;
            }
        }

        @Override
        public String toString() {
            StringBuilder ans = new StringBuilder();
            for (int i = 0; i < occur.length; i++) {
                if (occur[i] != 0) {
                    ans.append(i).append(',');
                }
            }
            if (ans.length() > 0) {
                ans.setLength(ans.length() - 1);
            }
            return ans.toString();
        }
    }
}
