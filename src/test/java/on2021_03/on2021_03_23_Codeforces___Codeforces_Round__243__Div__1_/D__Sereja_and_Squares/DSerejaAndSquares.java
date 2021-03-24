package on2021_03.on2021_03_23_Codeforces___Codeforces_Round__243__Div__1_.D__Sereja_and_Squares;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.LongHashSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DSerejaAndSquares {
    long id(int i, int j) {
        return DigitUtils.asLong(i, j);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] pts = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                pts[i][j] = in.ri();
            }
        }
        IntegerHashMap map = new IntegerHashMap(n, false);
        LongHashSet set = new LongHashSet(n, false);
        for (int i = 0; i < n; i++) {
            set.add(id(pts[i][0], pts[i][1]));
            map.modify(pts[i][0], 1);
        }
        int sqrt = IntMath.floorSqrt(n);
        List<int[]> forRow = new ArrayList<>(n);
        List<int[]> forCol = new ArrayList<>(n);
        for (int[] pt : pts) {
            if (map.get(pt[0]) <= sqrt) {
                forRow.add(pt);
            } else {
                forCol.add(pt);
            }
        }
        forRow.sort(Comparator.<int[]>comparingInt(x -> x[0]).thenComparingInt(x -> x[1]));
        forCol.sort(Comparator.<int[]>comparingInt(x -> x[1]).thenComparingInt(x -> x[0]));
        int[][] forRowData = forRow.toArray(new int[0][]);
        int[][] forColData = forCol.toArray(new int[0][]);
        int ans = 0;
        for (int i = 0; i < forRowData.length; i++) {
            int l = i;
            int r = i;
            while (r + 1 < forRowData.length && forRowData[r + 1][0] == forRowData[i][0]) {
                r++;
            }
            i = r;
            for (int j = l; j <= r; j++) {
                for (int k = j + 1; k <= r; k++) {
                    int[] a = forRowData[j];
                    int[] b = forRowData[k];
                    int d = b[1] - a[1];
                    if (set.contain(id(a[0] - d, a[1])) && set.contain(id(b[0] - d, b[1]))) {
                        ans++;
                    }
                    if (set.contain(id(a[0] + d, a[1])) && set.contain(id(b[0] + d, b[1]))) {
                        ans++;
                    }
                }
            }
            for (int j = l; j <= r; j++) {
                int[] a = forRowData[j];
                set.remove(id(a[0], a[1]));
            }
        }

        for (int i = 0; i < forColData.length; i++) {
            int l = i;
            int r = i;
            while (r + 1 < forColData.length && forColData[r + 1][1] == forColData[i][1]) {
                r++;
            }
            i = r;
            for (int j = l; j <= r; j++) {
                for (int k = j + 1; k <= r; k++) {
                    int[] a = forColData[j];
                    int[] b = forColData[k];
                    int d = b[0] - a[0];
                    if (set.contain(id(a[0], a[1] - d)) && set.contain(id(b[0], b[1] - d))) {
                        ans++;
                    }
                    if (set.contain(id(a[0], a[1] + d)) && set.contain(id(b[0], b[1] + d))) {
                        ans++;
                    }
                }
            }
            for (int j = l; j <= r; j++) {
                int[] a = forColData[j];
                set.remove(id(a[0], a[1]));
            }
        }

        out.println(ans);
    }
}
