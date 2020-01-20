package on2020_01.on2020_01_19_Codeforces_Round__614__Div__1_.B__Aroma_s_Search;



import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class BAromasSearch {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        List<long[]> pts = new ArrayList<>();
        pts.add(new long[]{in.readLong(), in.readLong()});
        int ax = in.readInt();
        int ay = in.readInt();
        int bx = in.readInt();
        int by = in.readInt();
        long inf = (long) 1e17;
        while (true) {
            long[] last = pts.get(pts.size() - 1);
            if (DigitUtils.isMultiplicationOverflow(last[0], ax, inf) ||
                    DigitUtils.isMultiplicationOverflow(last[1], ay, inf)) {
                break;
            }
            long[] next = new long[]{ax * last[0] + bx, ay * last[1] + by};
            pts.add(next);
        }

        long[] src = new long[]{in.readLong(), in.readLong()};
        long t = in.readLong();

        long ans = solve(pts, src, t);
        SequenceUtils.reverse(pts);
        ans = Math.max(ans, solve(pts, src, t));

        out.println(ans);
    }

    public long solve(List<long[]> pts, long[] src, long t) {
        long ans = 0;
        for (int i = 0; i < pts.size(); i++) {
            long[] from = pts.get(i);
            long remain = t - dist(src, from);
            if (remain < 0) {
                continue;
            }
            int cnt = 1;
            for (int j = i + 1; j < pts.size(); j++) {
                long[] next = pts.get(j);
                remain -= dist(from, next);
                if (remain < 0) {
                    break;
                }
                cnt++;
                from = next;
            }
            ans = Math.max(cnt, ans);
        }
        return ans;
    }

    public long dist(long[] a, long[] b) {
        return Math.abs(a[0] - b[0] + a[1] - b[1]);
    }
}
