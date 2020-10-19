package on2020_10.on2020_10_18_AtCoder___AtCoder_Grand_Contest_048.C___Penguin_Skating;



import template.io.FastInput;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CPenguinSkating {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int L = in.readInt();
        int[] a = new int[n + 2];
        int[] b = new int[n + 2];
        b[0] = a[0] = 0;
        b[n + 1] = a[n + 1] = L + 1;
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 1; i <= n; i++) {
            b[i] = in.readInt();
        }
        long ans = 0;
        List<Interval> list = new ArrayList<>(n);
        for (int i = 0; i <= n + 1; i++) {
            if (a[i] == b[i]) {
                continue;
            }
            int j = i;
            while (j + 1 <= n + 1 && Integer.compare(a[i], b[i]) == Integer.compare(a[j + 1], b[j + 1])) {
                j++;
            }
            list.add(new Interval(i, j, Integer.compare(a[i], b[i])));
            i = j;
        }
        if (list.isEmpty()) {
            out.println(0);
            return;
        }
        debug.debug("list", list);

        for (int i = 1; i < list.size(); i += 2) {
            Interval left = list.get(i - 1);
            Interval right = list.get(i);
            if (left.r + 1 == right.l && left.sign == -1 && right.sign == 1) {
                out.println(-1);
                return;
            }
        }

        for (Interval interval : list) {

            int l = interval.l;
            int r = interval.r;
            if (interval.sign < 0) {
                //right

                int rely = l - 1;
                for (int i = l; i <= r; i++) {
                    int cur = a[i];
                    if (rely > i) {
                        cur = a[rely] - (rely - i);
                    }
                    if (cur == b[i]) {
                        continue;
                    }
                    rely = Math.max(rely, i);
                    while (rely + 1 <= r + 1 && a[rely] - (rely - i) != b[i]) {
                        rely++;
                    }
                    if (a[rely] - (rely - i) != b[i]) {
                        out.println(-1);
                        return;
                    }
                    ans += rely - i;
                }

            } else {
                int rely = r + 1;
                for (int i = r; i >= l; i--) {
                    int cur = a[i];
                    if (rely < i) {
                        cur = a[rely] + (i - rely);
                    }
                    if (cur == b[i]) {
                        continue;
                    }
                    rely = Math.min(rely, i);
                    while (rely - 1 >= l - 1 && a[rely] + (i - rely) != b[i]) {
                        rely--;
                    }
                    if (a[rely] + (i - rely) != b[i]) {
                        out.println(-1);
                        return;
                    }
                    ans += i - rely;
                }
            }
        }

        out.println(ans);
    }
}

class Interval {
    int l;
    int r;
    int sign;

    public Interval(int l, int r, int sign) {
        this.l = l;
        this.r = r;
        this.sign = sign;
    }

    @Override
    public String toString() {
        return String.format("[%d, %d] => %d", l, r, sign);
    }
}