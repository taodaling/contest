package on2019_11.on2019_11_15_Educational_Codeforces_Round_76__Rated_for_Div__2_.F___Make_Them_Similar;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import template.FastInput;
import template.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }

        Map<Point, Integer> low = new HashMap<>(1 << 15);

        int lowMask = (1 << 15) - 1;
        int upMask = (1 << 30) - 1 - lowMask;
        for (int i = 0; i < 1 << 15; i++) {
            Point pt = new Point();
            pt.differ = new int[n];
            for (int j = 0; j < n; j++) {
                pt.differ[j] = Integer.bitCount(lowMask & (i ^ a[j])) - Integer.bitCount(lowMask & (i ^ a[0]));
            }
            low.put(pt, i);
        }

        for (int i = 0; i < 1 << 15; i++) {
            Point pt = new Point();
            pt.differ = new int[n];
            int t = i << 15;
            for (int j = 0; j < n; j++) {
                pt.differ[j] = -(Integer.bitCount(upMask & (t ^ a[j])) - Integer.bitCount(upMask & (t ^ a[0])));
            }
            if (low.containsKey(pt)) {
                out.println(t + low.get(pt));
                return;
            }
        }

        out.println(-1);
    }
}


class Point {
    int[] differ;

    @Override
    public int hashCode() {
        return Arrays.hashCode(differ);
    }

    @Override
    public boolean equals(Object obj) {
        Point pt = (Point) obj;
        return Arrays.equals(differ, pt.differ);
    }
}
