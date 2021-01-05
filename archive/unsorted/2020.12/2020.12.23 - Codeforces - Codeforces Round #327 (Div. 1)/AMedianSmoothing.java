package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class AMedianSmoothing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n + 2];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        a[0] = a[1];
        a[n + 1] = a[n];
        List<Interval> list = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            int r = i;
            while (r + 1 < a.length && a[r] == a[r + 1]) {
                r++;
            }
            if (r > i)
                list.add(new Interval(a[i], i, r));
            i = r;
        }
        int time = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            Interval left = list.get(i);
            Interval right = list.get(i + 1);
            int l = left.r + 1;
            int r = right.l - 1;
            if (left.v == right.v) {
                time = Math.max(time, DigitUtils.ceilDiv(r - l + 1, 2));
            } else {
                time = Math.max(time, (r - l + 1) / 2);
            }
            for (int j = l; j <= r; j++) {
                if (j - l <= r - j) {
                    a[j] = left.v;
                } else {
                    a[j] = right.v;
                }
            }
        }
        out.println(time);
        for (int i = 1; i <= n; i++) {
            out.append(a[i]).append(' ');
        }
    }
}

class Interval {
    int v;
    int l;
    int r;

    public Interval(int v, int l, int r) {
        this.v = v;
        this.l = l;
        this.r = r;
    }
}


