package on2020_02.on2020_02_21_Codeforces_Round__468__Div__1__based_on_Technocup_2018_Final_Round_.D__Game_with_Tokens;



import dp.Lis;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DGameWithTokens {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Point[] pts = new Point[n];
        List<Point>[] lists = new List[]{new ArrayList(n), new ArrayList(n)};
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            int x = in.readInt();
            int y = in.readInt();
            pts[i].x = y - x;
            pts[i].y = y + x;
            lists[pts[i].y & 1].add(pts[i]);
        }
        long sum1 = count(lists[0].toArray(new Point[0]), 0);
        long sum2 = count(lists[1].toArray(new Point[0]), 1);
        out.println(sum1 + sum2);
    }

    public long count(Point[] pts, int odd) {
        Arrays.sort(pts, (a, b) -> Integer.compare(a.y, b.y));
        int limit = 300000;

        SimplifiedDeque<Point> dq = new Range2DequeAdapter<>(i -> pts[i], 0, pts.length - 1);
        int[] left = new int[limit * 2 + 1];
        int[] right = new int[limit * 2 + 1];
        int l = limit;
        int r = -limit;
        for (int i = limit; i >= -limit; i--) {
            while (!dq.isEmpty() && dq.peekLast().y > i) {
                Point head = dq.removeLast();
                l = Math.min(l, head.x);
                r = Math.max(r, head.x);
            }
            left[i + limit] = l;
            right[i + limit] = r;
        }

        l = limit;
        r = -limit;
        dq = new Range2DequeAdapter<>(i -> pts[i], 0, pts.length - 1);
        for (int i = -limit; i <= limit; i++) {
            while (!dq.isEmpty() && dq.peekFirst().y < i) {
                Point head = dq.removeFirst();
                l = Math.min(l, head.x);
                r = Math.max(r, head.x);
            }
            left[i + limit] = Math.max(left[i + limit], l);
            right[i + limit] = Math.min(right[i + limit], r);
        }

        long sum = 0;
        for (int i = 0; i < left.length; i++) {
            int y = i - limit;
            if ((y & 1) == (odd & 1)) {
                continue;
            }
            if ((left[i] & 1) == (y & 1)) {
                left[i]++;
            }
            if ((right[i] & 1) == (y & 1)) {
                right[i]--;
            }
            if (right[i] <= left[i]) {
                continue;
            }
            sum += (right[i] - left[i]) / 2;
        }
        return sum;
    }
}

class Point {
    int x;
    int y;
}