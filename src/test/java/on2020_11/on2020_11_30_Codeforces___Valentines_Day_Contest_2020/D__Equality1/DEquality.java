package on2020_11.on2020_11_30_Codeforces___Valentines_Day_Contest_2020.D__Equality1;




import template.io.FastInput;
import template.io.FastOutput;
import template.math.FloorDivisionOptimizer;
import template.math.GenericModLog;
import template.primitve.generated.datastructure.IntegerIntervalMap;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class DEquality {
    Debug debug = new Debug(true);
    IntegerIntervalMap map = new IntegerIntervalMap();

    int threshold2 = (int) 1e5;
    int[] valueA = new int[threshold2];
    int[] valueB = new int[threshold2];
    int[] fromA = new int[threshold2];
    int[] fromB = new int[threshold2];
    int[] toA = new int[threshold2];
    int[] toB = new int[threshold2];

    public int gen(int a, int limit, int plus, int rightshift, int[] value, int[] from, int[] to) {
        int wpos = 0;
        for (int i = 1, r; i <= limit; i = r + 1) {
            int v = a / i;
            r = v == 0 ? limit : Math.min(a / v, limit);
            value[wpos] = (v + plus) >> rightshift;
            from[wpos] = i;
            to[wpos] = r;
            wpos++;
        }
        return wpos;
    }

    public void gen(int a, int b, int limit, int plus, int rightShift) {
        int n = gen(a, limit, plus, rightShift, valueA, fromA, toA);
        int m = gen(b, limit, plus, rightShift, valueB, fromB, toB);
        int x = 0;
        int y = 0;

        int last = 0;
        while (x < n && y < m) {
            if (valueA[x] < valueB[y]) {
                y++;
            } else if (valueA[x] > valueB[y]) {
                x++;
            } else {
                int l = Math.max(fromA[x], fromB[y]);
                int r = Math.min(toA[x], toB[y]);
                if (l <= r) {
                    map.remove(last + 1, l);
                    last = r;
                }
                if (toA[x] == r) {
                    x++;
                }
                if (toB[y] == r) {
                    y++;
                }
            }
        }
        map.remove(last + 1, limit + 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        map.add(1, n + 1);
        int last = 0;
        for (int i = 0; i < x; i++) {
            int l = in.ri();
            int r = in.ri();
            if (last < l - 1) {
                gen(last, l - 1, n, 1, 1);
            }
            last = r;
        }
        if (n > last) {
            gen(last, n, n, 1, 1);
        }


        int y = in.ri();
        last = 0;
        for (int i = 0; i < y; i++) {
            int l = in.ri();
            int r = in.ri();
            if (last < l - 1) {
                gen(last, l - 1, n, 0, 1);
            }
            last = r;
        }
        if (n > last) {
            gen(last, n, n, 0, 1);
        }

        out.println(map.total());
    }

}