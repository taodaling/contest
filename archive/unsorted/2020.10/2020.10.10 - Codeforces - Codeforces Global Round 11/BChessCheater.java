package contest;

import template.io.FastInput;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BChessCheater {

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);

        List<Interval> list = new ArrayList<>(n);
        for (int i = 1; i < n; i++) {
            if (s[i] == 'W' && s[i - 1] == 'L') {
                int j = i - 1;
                while (j > 0 && s[j - 1] == 'L') {
                    j--;
                }
                if (j > 0) {
                    list.add(new Interval(j, i - 1));
                }
            }
        }

        list.sort((a, b) -> Integer.compare(a.r - a.l, b.r - b.l));
        for (Interval interval : list) {
            for (int i = interval.l; k > 0 && i <= interval.r; i++) {
                s[i] = 'W';
                k--;
            }
        }

        int lastW = n - 1;
        while (lastW >= 0 && s[lastW] != 'W') {
            lastW--;
        }
        if (lastW >= 0) {
            for (int i = lastW + 1; i < n && k > 0; i++) {
                if (s[i] == 'L') {
                    s[i] = 'W';
                    k--;
                }
            }
        }

        for (int i = n - 1; i >= 0 && k > 0; i--) {
            if (s[i] == 'L') {
                s[i] = 'W';
                k--;
            }
        }

        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (s[i] == 'W') {
                int contrib = i > 0 && s[i - 1] == 'W' ? 2 : 1;
                sum += contrib;
            }
        }

        out.println(sum);
        debug.debug("k", k);
        debug.run(() -> {
            debug.debug("s", new String(s));
        });
    }
}

class Interval {
    int l;
    int r;

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }
}
