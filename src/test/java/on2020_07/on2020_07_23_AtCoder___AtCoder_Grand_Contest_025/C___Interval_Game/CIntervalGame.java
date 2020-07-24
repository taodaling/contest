package on2020_07.on2020_07_23_AtCoder___AtCoder_Grand_Contest_025.C___Interval_Game;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class CIntervalGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        LR[] lrs = new LR[n];
        for (int i = 0; i < n; i++) {
            lrs[i] = new LR();
            lrs[i].l = in.readInt();
            lrs[i].r = in.readInt();
            lrs[i].id = i;
        }
        long ans1 = go(lrs);
        for (int i = 0; i < n; i++) {
            int tmp = lrs[i].l;
            lrs[i].l = -lrs[i].r;
            lrs[i].r = -tmp;
        }
        long ans2 = go(lrs);

        long ans = Math.max(ans1, ans2);
        out.println(ans);
    }

    public long go(LR[] lrs) {
        TreeSet<LR> sortByL = new TreeSet<>((a, b) -> a.l == b.l ? Integer.compare(a.id, b.id) : Integer.compare(a.l, b.l));
        TreeSet<LR> sortByR = new TreeSet<>((a, b) -> a.r == b.r ? Integer.compare(a.id, b.id) : Integer.compare(a.r, b.r));
        sortByL.addAll(Arrays.asList(lrs));
        sortByR.addAll(Arrays.asList(lrs));

        int cur = 0;
        long ans = 0;
        while (!sortByL.isEmpty()) {
            {
                LR head = sortByL.pollLast();
                sortByR.remove(head);
                if (head.l > cur) {
                    ans += head.l - cur;
                    cur = head.l;
                }
            }
            if (!sortByR.isEmpty()) {
                LR tail = sortByR.pollFirst();
                sortByL.remove(tail);
                if (tail.r < cur) {
                    ans += cur - tail.r;
                    cur = tail.r;
                }
            }
        }

        return ans + Math.abs(cur);
    }
}

class LR {
    int l;
    int r;
    int id;
}
